package vn.hoidanit.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ItemController {

    private final ProductService productService;

    public ItemController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public String getProductPage(Model model, @PathVariable long id) {
        // lấy thông tin sp bằng id
        // có .get là do có optional
        // lấy thông tin đối tượng truyền qua view
        Product pr = this.productService.fetchProductById(id).get();
        model.addAttribute("product", pr);
        model.addAttribute("id", id);
        return "client/product/detail";
    }

    // truyền vô session thì log out mới chạy dc

    // Đoạn code này là xử lý khi người dùng nhấn nút "Thêm vào giỏ hàng" trên
    // website.

    // Spring sẽ tự động lấy giá trị id từ URL.
    // Ví dụ: Khi bạn gửi POST tới /add-product-to-cart/5, thì {id} sẽ là 5.
    @PostMapping("add-product-to-cart/{id}")

    // Annotation @PathVariable long id giúp Spring lấy giá trị id từ URL và gán vào
    // biến id trong method.

    // HttpServletRequest request : là đối tượng đại diện cho request (yêu cầu) HTTP
    // mà trình duyệt gửi lên server.

    // Chức năng:
    // Trong code của bạn, thường dùng để:
    // Lấy session hiện tại: HttpSession session = request.getSession(false);
    // Lấy thông tin user đang đăng nhập từ session.

    public String postMethodName(@PathVariable long id, HttpServletRequest request) {

        // HttpSession session = request.getSession(false);:
        // Lấy session hiện tại của user (để lấy thông tin user đang đăng nhập).
        HttpSession session = request.getSession(false);

        // long productId = id;: Lấy id sản phẩm từ URL.
        long productId = id;

        // String email = (String) session.getAttribute("email");:
        // Lấy email user đã lưu trong session (để xác định user nào đang thêm sản
        // phẩm).
        String email = (String) session.getAttribute("email");

        // this.productService.handleAddProductToCart(email, productId, session);:
        // Gọi hàm xử lý thêm sản phẩm vào giỏ hàng cho user này.
        this.productService.handleAddProductToCart(email, productId, session);

        return "redirect:/";
    }

    // Chi tiết giỏ hàng
    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {

        // Tạo một đối tượng User mới (chỉ là object rỗng ban đầu).
        User currentUser = new User();

        // Lấy session hiện tại của user (nếu chưa có session thì trả về null).
        // Session này chứa các thông tin đã lưu khi user đăng nhập (ví dụ: id,
        // email...).
        HttpSession session = request.getSession(false);

        // Lấy thuộc tính "id" từ session, đây là id của user đang đăng nhập.
        long id = (long) session.getAttribute("id");

        // Gán id vừa lấy được vào đối tượng User vừa tạo.
        // Bây giờ, currentUser đại diện cho user đang đăng nhập (ít nhất là về mặt id).
        currentUser.setId(id);

        // Lấy giỏ hàng (Cart) của user hiện tại từ database.
        // tìm bản ghi giỏ hàng (Cart) ứng với user truyền vào
        Cart cart = this.productService.fetchByUser(currentUser);

        // Từ đối tượng giỏ hàng vừa lấy được, lấy ra danh sách các sản phẩm mà user đã
        // thêm vào giỏ.
        // Mỗi phần tử trong danh sách này là một CartDetail (chi tiết giỏ hàng), chứa
        // thông tin: sản phẩm nào, số lượng bao nhiêu, giá lúc thêm vào...

        // Lúc này có 1 cái bug là lỡ như cái cart là null (user chưa có giỏ hàng) thì
        // khi null mà gọi đến phương thức .getCartDetails() sẽ bị lỗi

        // Cách xử lí : xài if/else
        // Nếu cart là null (tức là user chưa có giỏ hàng, chưa từng thêm sản phẩm nào),
        // thì gán cartDetails là một danh sách rỗng (new ArrayList<CartDetail>()).
        // Nếu cart khác null (user đã có giỏ hàng),
        // thì lấy danh sách sản phẩm trong giỏ: cart.getCartDetails().

        // Tại sao dùng ArrayList :
        // Bạn khai báo kiểu biến là List<CartDetail> để code linh hoạt.
        // Nhưng khi tạo mới, bạn phải dùng một lớp cụ thể như ArrayList để tạo đối
        // tượng thực tế.

        // Ở đây dùng new ArrayList<CartDetail>() vì:
        // ArrayList là một lớp cụ thể (concrete class) để tạo ra một danh sách rỗng.
        // List là một interface (giao diện), bạn không thể tạo đối tượng trực tiếp từ
        // interface.
        // List<CartDetail> list1 = new ArrayList<>(); // Đúng
        // List<CartDetail> list2 = new List<>(); // Sai, vì List là interface

        // TÍnh đa hình nó tự động convert qua lại
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        // Khởi tạo biến totalPrice để tính tổng tiền.
        // Duyệt qua từng sản phẩm trong giỏ (cartDetails):
        // Lấy giá sản phẩm (cd.getProduct().getPrice())
        // Nhân với số lượng sản phẩm đó trong giỏ (cd.getQuantity())
        // Cộng dồn vào totalPrice.
        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getProduct().getPrice() * cd.getQuantity();
        }

        // Đưa danh sách các sản phẩm trong giỏ hàng (cartDetails - kiểu
        // List<CartDetail>) sang view.
        // Trong JSP, bạn sẽ dùng ${cartDetails} để lặp qua và hiển thị từng sản phẩm
        // trong giỏ.
        model.addAttribute("cartDetails", cartDetails);

        // Đưa tổng tiền của giỏ hàng (totalPrice) sang view.
        // Trong JSP, bạn sẽ dùng ${totalPrice} để hiển thị tổng tiền mà user phải thanh
        // toán.
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cart", cart);

        return "client/cart/show";
    }
    // ================================================================

    // Truyền id để biết cần xóa dòng nào trong giỏ hàng (mỗi dòng là một
    // CartDetail, có id riêng).
    // Nếu không truyền id, bạn sẽ không biết user muốn xóa sản phẩm nào.

    @PostMapping("/delete-cart-product/{id}")
    public String deleteCartDetail(@PathVariable long id, HttpServletRequest request) {

        // Truyền vào id để biết xóa cartDetail nào, truyền session để hiện số sản phẩm
        // có trong giỏ
        HttpSession session = request.getSession(false);
        long CartDetailId = id;

        // Logic bên service xử lí
        this.productService.handleRemoveCartDetail(CartDetailId, session);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String getCheckOutPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = this.productService.fetchByUser(currentUser);

        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/checkout";
    }

    @PostMapping("/confirm-checkout")

    // @ModelAttribute("cart"): Spring tự động bind data từ form ẩn vào object Cart
    // (chứa list CartDetail đã được JS cập nhật)
    public String getCheckOutPage(@ModelAttribute("cart") Cart cart) {

        // Giải thích 1 chút về cái path
        // Form JSP của bạn có:

        // <form:input
        // path="cartDetails[${status.index}].quantity"
        // id="cartDetails${status.index}__quantity" />

        // → Khi submit form, dữ liệu gửi lên server sẽ có dạng:

        // cartDetails[0].id=1&cartDetails[0].quantity=2
        // cartDetails[1].id=2&cartDetails[1].quantity=1

        // Controller nhận dữ liệu nhờ @ModelAttribute:
        // public String getCheckOutPage(@ModelAttribute("cart") Cart cart) {
        // // Spring tự động làm những việc sau:
        // // 1. Tạo đối tượng Cart
        // // 2. Bind dữ liệu từ form vào Cart theo CẤU TRÚC PATH
        // // cartDetails[0].quantity → cart.getCartDetails().get(0).setQuantity()
        // // cartDetails[1].quantity → cart.getCartDetails().get(1).setQuantity()
        // }

        // Phòng trường hợp giỏ hàng rỗng: Nếu cart null sẽ tạo list rỗng thay vì bị NPE
        // cart.getCartDetails(): Lấy danh sách sản phẩm đã được cập nhật số lượng từ FE
        // (chỉ mới fe thôi)
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        // Gọi service để:
        // Cập nhật quantity mới vào database
        // Thực hiện các kiểm tra nghiệp vụ (nếu có)
        this.productService.handleUpdateCartBeforeCheckout(cartDetails);

        // Sau khi xử lý xong, chuyển hướng sang trang thanh toán thực sự
        return "redirect:/checkout";
    }

    @PostMapping("/place-order")
    public String handlePlaceOrder(
            HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone) {
        HttpSession session = request.getSession(false);

        return "redirect:/";
    }
}