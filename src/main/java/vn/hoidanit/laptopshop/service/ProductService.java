package vn.hoidanit.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetails;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Product createProduct(Product pr) {
        return this.productRepository.save(pr);
    }

    // lấy danh sách tất cả sản phẩm
    public List<Product> fetchProducts() {
        return this.productRepository.findAll();
    }

    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    // =============================================================================
    // Đây là hàm xử lý logic khi người dùng nhấn "Thêm vào giỏ hàng"
    // Nó sẽ đảm bảo mỗi user có 1 giỏ hàng, và mỗi lần thêm sản phẩm sẽ cập nhật
    // đúng vào giỏ hàng của user đó.

    // truyền vô email để biết ng dùng là ai
    public void handleAddProductToCart(String email, long productId, HttpSession session) {

        // Lấy user từ email. Xác định user đang thao tác.
        User user = this.userService.getUserByEmail(email);

        if (user != null) {
            // Kiểm tra user đã có giỏ hàng chưa:
            Cart cart = this.cartRepository.findByUser(user);

            // Nếu chưa có (cart == null):
            // Tạo mới một đối tượng Cart (giỏ hàng) cho user này.
            // Gán user cho cart, set tổng số sản phẩm (sum) ban đầu là 0.
            // Lưu cart mới vào database.

            // Nếu đã có, dùng lại giỏ hàng cũ.
            if (cart == null) {
                // tạo mới cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);

                cart = this.cartRepository.save(otherCart);
            }

            // Optional<Product> productOptional =
            // this.productRepository.findById(productId);
            // Tìm sản phẩm trong database theo productId.
            Optional<Product> productOptional = this.productRepository.findById(productId);

            // if (productOptional.isPresent()) { ... }
            // Kiểm tra xem sản phẩm có tồn tại không.
            // Nếu có, lấy ra đối tượng Product thực tế:
            // Product realProduct = productOptional.get();
            if (productOptional.isPresent()) {
                Product realProduct = productOptional.get();

                // CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart,
                // realProduct);

                // Kiểm tra xem trong giỏ hàng (cart) đã có sản phẩm này (realProduct) chưa
                // Nếu oldDetail == null: sản phẩm chưa có trong giỏ, sẽ thêm mới
                // Nếu oldDetail != null: sản phẩm đã có, sẽ tăng số lượng
                CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);

                // trường hợp sản phẩm chưa có trong giỏ hàng
                if (oldDetail == null) {

                    CartDetail cd = new CartDetail();
                    cd.setCart(cart); // Gán giỏ hàng hiện tại cho chi tiết giỏ hàng này
                    cd.setProduct(realProduct); // Gán sản phẩm thực tế mà user vừa chọn
                    cd.setPrice(realProduct.getPrice()); // Lưu giá sản phẩm tại thời điểm thêm vào giỏ
                    cd.setQuantity(1); // Số lượng sản phẩm là 1 (vì mới thêm lần đầu)

                    this.cartDetailRepository.save(cd); // Lưu chi tiết giỏ hàng này vào database

                    // cart.getSum() + 1: Lấy tổng số sản phẩm hiện tại trong giỏ và tăng lên 1 (vì
                    // vừa thêm sản phẩm mới).
                    int s = cart.getSum() + 1;

                    // cart.setSum(s): Gán lại tổng số sản phẩm mới cho đối tượng giỏ hàng.
                    cart.setSum(s);

                    // this.cartRepository.save(cart): Lưu lại giỏ hàng vào database để cập nhật
                    // tổng số sản phẩm.
                    this.cartRepository.save(cart);

                    // cập nhật session, cái giỏ hàng ko cần out ra để nó cập nhật giỏ hàng
                    // sẽ lưu tên sum vào session.

                    session.setAttribute("sum", s);
                }
                // xử lý trường hợp sản phẩm đã có trong giỏ hàng của user:
                else {

                    // Lấy số lượng hiện tại của sản phẩm đó trong giỏ (oldDetail.getQuantity()).
                    // Tăng số lượng lên 1 (oldDetail.setQuantity(oldDetail.getQuantity() + 1);).
                    oldDetail.setQuantity(oldDetail.getQuantity() + 1);

                    // Lưu lại bản ghi CartDetail này vào database
                    // (this.cartDetailRepository.save(oldDetail);).
                    this.cartDetailRepository.save(oldDetail);

                }
            }

        }
    }

    // ============================================================================
    // tìm giỏ hàng theo user
    public Cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    // ============================================================================
    // Hàm này xóa một sản phẩm khỏi giỏ hàng, cập nhật lại tổng số sản phẩm và
    // session.

    public void handleRemoveCartDetail(long CartDetailId, HttpSession session) {

        // Tìm bản ghi chi tiết giỏ hàng (CartDetail) theo id. Nếu tìm thấy, lấy ra đối
        // tượng cartDetail.
        Optional<CartDetail> cartDetaiOptional = this.cartDetailRepository.findById(CartDetailId);
        if (cartDetaiOptional.isPresent()) {
            CartDetail cartDetail = cartDetaiOptional.get();

            // Lấy đối tượng giỏ hàng (Cart) chứa sản phẩm này.
            Cart currentCart = cartDetail.getCart();

            // Xóa CartDetail khỏi database. Xóa sản phẩm này khỏi giỏ hàng trong database.
            this.cartDetailRepository.deleteById(CartDetailId);

            // Cập nhật lại tổng số sản phẩm trong giỏ hàng

            // Nếu giỏ hàng còn nhiều hơn 1 sản phẩm:
            // Giảm tổng số sản phẩm (sum) đi 1.
            // Lưu lại giỏ hàng vào database.
            // Cập nhật lại số lượng sản phẩm trong session để giao diện hiển thị đúng
            if (currentCart.getSum() > 1) {

                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);
                session.setAttribute("sum", s);
                this.cartRepository.save(currentCart);

                // Nếu chỉ còn 1 sản phẩm (sau khi xóa là hết)
                // Nếu đây là sản phẩm cuối cùng:
                // Xóa luôn giỏ hàng khỏi database.
                // Đặt số lượng sản phẩm trong session về 0.
            } else {

                this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("sum", 0);
            }
        }
    }
    // ==========================================================================
    // FE (JSP/JavaScript): Đã xử lý tăng/giảm số lượng (quantity) và cập nhật
    // real-time trên giao diện
    // Form ẩn: Chứa các giá trị quantity mới sau khi người dùng thao tác
    // Method này: Là bước cuối cùng để đồng bộ dữ liệu từ FE xuống database trước
    // khi checkout

    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
        for (CartDetail cartDetail : cartDetails) {
            // 1. Tìm CartDetail trong database theo ID từ dữ liệu FE gửi lên
            // Truy vấn database để lấy bản ghi CartDetail hiện tại bằng ID
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());

            // Kiểm tra nếu bản ghi tồn tại trong database
            // Lấy ra đối tượng CartDetail thực sự từ Optional
            if (cdOptional.isPresent()) {
                CartDetail currentCartDetail = cdOptional.get();

                // Cập nhật số lượng mới từ dữ liệu FE vào bản ghi database
                currentCartDetail.setQuantity(cartDetail.getQuantity());

                // Lưu thay đổi vào database
                // Đảm bảo số lượng mới sẽ được dùng cho quy trình thanh toán tiếp theo
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }

    // ========================================================================

    // 🎯 Mục tiêu của đoạn code:
    // Khi người dùng nhấn nút “Đặt hàng”, hệ thống sẽ:
    // Tạo đơn hàng mới (Order)
    // Lưu chi tiết các sản phẩm trong giỏ vào bảng OrderDetails
    // Xóa giỏ hàng cũ (Cart và CartDetail)
    // Chuyển hướng sang trang “Cảm ơn”

    public void handlePlaceOrder(User user, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone) {
        // create order
        // Lưu đơn hàng mới vào bảng Order, gắn user và thông tin người nhận
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverAddress(receiverAddress);
        order.setReceiverPhone(receiverPhone);
        order = this.orderRepository.save(order);

        // create orderDetail
        // step 1: get cart by user
        // ➡️ Tìm giỏ hàng của user theo userId
        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetail = cart.getCartDetails();

            // 🔹 c. Duyệt các món hàng trong giỏ → tạo từng OrderDetail
            // 🧠 Mỗi sản phẩm trong giỏ hàng sẽ trở thành 1 dòng trong bảng OrderDetails.
            if (cartDetail != null) {
                for (CartDetail cd : cartDetail) {
                    OrderDetails orderDetail = new OrderDetails();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cd.getProduct());
                    orderDetail.setPrice(cd.getPrice());
                    orderDetail.setQuantity(cd.getQuantity());
                    this.orderDetailRepository.save(orderDetail);
                }
                // step 2 delete cart_detail and cart
                // 👉 Sau khi tạo xong order, ta xóa giỏ hàng và các chi tiết của nó
                for (CartDetail cd : cartDetail) {
                    this.cartDetailRepository.deleteById(cd.getId());

                }
                this.cartRepository.deleteById(cart.getId());

                // step 3
                // Đặt lại tổng tiền (sum) trong session về 0
                session.setAttribute("sum", 0);

            }
        }
    }
}
