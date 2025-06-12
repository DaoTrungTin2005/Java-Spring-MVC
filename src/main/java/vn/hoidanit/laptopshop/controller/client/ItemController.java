package vn.hoidanit.laptopshop.controller.client;

import java.util.List;

import javax.swing.Spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
        List<CartDetail> cartDetails = cart.getCartDetails();

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

        return "client/cart/show";
    }

}
