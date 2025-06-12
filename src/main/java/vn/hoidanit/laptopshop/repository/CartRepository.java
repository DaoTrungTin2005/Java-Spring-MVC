package vn.hoidanit.laptopshop.repository;

import javax.swing.Spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // cartRepository.findByUser(user) là một method của Spring Data JPA, sẽ tìm bản
    // ghi giỏ hàng (Cart) ứng với user truyền vào
    Cart findByUser(User user);
}
