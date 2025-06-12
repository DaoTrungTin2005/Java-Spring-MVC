package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    // Kiểm tra xem trong giỏ hàng (cart) đã có sản phẩm (product) này chưa.
    // Kết quả:
    // Trả về true nếu đã có bản ghi CartDetail với cart và product này.
    // Trả về false nếu chưa có.
    boolean existsByCartAndProduct(Cart cart, Product product);

    // Tìm và trả về bản ghi CartDetail ứng với cart và product truyền vào.
    // Kết quả:
    // Trả về đối tượng CartDetail nếu tìm thấy.
    // Trả về null nếu không có.
    CartDetail findByCartAndProduct(Cart cart, Product product);

}
