package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hoidanit.laptopshop.domain.OrderDetails;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {

}
