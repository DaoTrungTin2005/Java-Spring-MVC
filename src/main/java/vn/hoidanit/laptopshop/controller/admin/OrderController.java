package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetails;
import vn.hoidanit.laptopshop.service.OrderService;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Dashboard
    @GetMapping("/admin/order")
    // Hàm này sẽ được Spring gọi khi URL được truy cập. model là đối tượng để
    // truyền dữ liệu từ Controller xuống View (.jsp).
    public String getDashboard(Model model) {

        // Gọi đến tầng service để lấy danh sách tất cả các đơn hàng từ database
        List<Order> orders = this.orderService.fetchAllOrders();

        // Đưa danh sách đơn hàng vào model với key "orders" để JSP có thể sử dụng.Đưa
        // danh sách đơn hàng vào model với key "orders" để JSP có thể sử dụng.
        model.addAttribute("orders", orders);
        return "admin/order/show";
    }

    // View
    // Khi người dùng truy cập ví dụ /admin/order/3, controller sẽ nhận id = 3.
    @GetMapping("/admin/order/{id}")

    // @PathVariable long id Gán giá trị {id} trong URL vào biến id.
    public String getOrderDetailPage(Model model, @PathVariable long id) {

        // Gọi service để tìm đơn hàng theo ID. .get() dùng vì fetchOrderById trả về
        // Optional<Order>.
        Order order = this.orderService.fetchOrderById(id).get();

        // Truyền đối tượng order xuống view để hiển thị thông tin chung như: người
        // nhận, địa chỉ, trạng thái, tổng tiền.
        model.addAttribute("order", order);

        // Truyền riêng id nếu view cần in cụ thể id.
        model.addAttribute("id", id);

        // Lấy danh sách OrderDetails của đơn hàng và truyền xuống view để hiển thị từng
        // sản phẩm đã đặt.
        model.addAttribute("orderDetails", order.getOrderDetails());
        return "admin/order/detail";

    }

    @GetMapping("/admin/order/delete/{id}")
    // Khi người dùng mở trang xóa đơn hàng, controller:
    // Truyền id đơn hàng vào model.
    // Truyền newOrder (đối tượng rỗng) để bind với form modelAttribute="newOrder"
    // trong JSP.
    public String getDeleteOrderPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newOrder", new Order());
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    // Sau khi người dùng nhấn "Confirm", dữ liệu từ form (id đơn hàng) được bind
    // vào Order order.
    // Gọi hàm deleteOrderById(order.getId()) để thực hiện xóa.
    public String postDeleteOrder(@ModelAttribute("newOrder") Order order) {
        this.orderService.deleteOrderById(order.getId());
        return "redirect:/admin/order";
    }

    // Cho phép quản trị viên cập nhật trạng thái (status) của một đơn hàng (Order),
    // ví dụ: "PENDING", "SHIPPING", "DELIVERED"...

    // Khi admin truy cập đường dẫn này (ví dụ /admin/order/update/5), sẽ hiển thị
    // form update.

    // @PathVariable long id Lấy id từ URL.
    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id) {

        // orderService.fetchOrderById(id) Gọi service để lấy ra đơn hàng cần cập nhật.
        // Trả về Optional<Order>.
        Optional<Order> currentOrder = this.orderService.fetchOrderById(id);

        // Gửi dữ liệu Order ra giao diện JSP để hiển thị sẵn giá trị.
        model.addAttribute("newOrder", currentOrder.get());
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")

    // @ModelAttribute("newOrder") Order order Nhận lại dữ liệu Order từ form gửi
    // lên.
    public String handleUpdateOrder(@ModelAttribute("newOrder") Order order) {

        // orderService.updateOrder(order) Gọi hàm trong service để lưu thay đổi.
        this.orderService.updateOrder(order);
        return "redirect:/admin/order";
    }
}