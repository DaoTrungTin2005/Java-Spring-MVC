package vn.hoidanit.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.persistence.OneToMany;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetails;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
    }

    // lấy toàn bộ danh sách đơn hàng từ bảng orders trong database
    public List<Order> fetchAllOrders() {
        return this.orderRepository.findAll();
    }

    // tìm một đơn hàng theo id từ cơ sở dữ liệu.
    public Optional<Order> fetchOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    // Xóa một đơn hàng (Order) theo ID, đồng thời đảm bảo xóa luôn các OrderDetails
    // liên quan để tránh lỗi ràng buộc (foreign key).
    public void deleteOrderById(long id) {

        // Gọi hàm fetchOrderById(id) để tìm đơn hàng theo ID.

        // Trả về kiểu Optional<Order> để xử lý trường hợp không tồn tại đơn hàng một
        // cách an toàn (tránh NullPointerException).

        Optional<Order> orderOptional = this.fetchOrderById(id);

        // Kiểm tra xem đơn hàng có tồn tại không.
        if (orderOptional.isPresent()) {

            // Lấy đối tượng Order ra từ Optional.
            Order order = orderOptional.get();

            // Lấy danh sách các OrderDetails (chi tiết đơn hàng) của đơn hàng.

            // Quan hệ này được khai báo trong entity Order:
            // @OneToMany(mappedBy = "order")
            // private List<OrderDetails> orderDetails;
            List<OrderDetails> orderDetails = order.getOrderDetails();

            // Duyệt qua từng dòng chi tiết đơn hàng của đơn hàng đang cần xóa.
            for (OrderDetails orderDetail : orderDetails) {

                // Xóa từng OrderDetail theo id của nó.
                // Cần làm việc này trước khi xóa Order để tránh lỗi foreign key constraint từ
                // database.
                this.orderDetailRepository.deleteById(orderDetail.getId());
            }
        }

        // Cuối cùng, sau khi đã xóa hết các dòng OrderDetails liên quan, ta xóa Order.
        this.orderRepository.deleteById(id);
    }

    // Update
    public void updateOrder(Order order) {

        // Tìm lại Order hiện tại trong database.
        Optional<Order> orderOptional = this.fetchOrderById(order.getId());

        // Nếu tìm thấy thì thực hiện cập nhật.
        if (orderOptional.isPresent()) {

            // Lấy bản ghi thực tế ra.
            Order currentOrder = orderOptional.get();

            // Chỉ cập nhật status (trạng thái), không thay đổi các thông tin khác.
            currentOrder.setStatus(order.getStatus());

            // Lưu lại vào DB.
            this.orderRepository.save(currentOrder);
        }
    }
}