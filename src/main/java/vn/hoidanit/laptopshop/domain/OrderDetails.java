package vn.hoidanit.laptopshop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_detail")

public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long quantity;
    private double price;

    //order_id long
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order ;

    // quan hệ 1 chiều ko cần ấy bên bảng product ???? 
    //product_id long
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    

}
