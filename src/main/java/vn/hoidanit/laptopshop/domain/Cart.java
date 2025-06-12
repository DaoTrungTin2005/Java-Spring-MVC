package vn.hoidanit.laptopshop.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

// 1. Bảng Cart (Giỏ hàng)
// Mỗi user chỉ có 1 giỏ hàng.
// Lưu thông tin tổng quát về giỏ hàng của user (ai là chủ giỏ, tổng số sản phẩm...).


// 2. Bảng CartDetail (Chi tiết giỏ hàng)
// Mỗi bản ghi là 1 sản phẩm trong giỏ hàng của user.
// Lưu thông tin từng sản phẩm mà user đã thêm vào giỏ.

// 1. Cart (Giỏ hàng)
// Đại diện cho giỏ hàng của 1 user.
// Mỗi user chỉ có 1 giỏ hàng.
// Chỉ lưu thông tin tổng quát:
// Ai là chủ giỏ hàng (user nào)
// Tổng số sản phẩm trong giỏ (sum)

// 2. CartDetail (Chi tiết giỏ hàng)
// Đại diện cho từng sản phẩm cụ thể mà user đã thêm vào giỏ.
// Một giỏ hàng (Cart) có thể có nhiều CartDetail (mỗi CartDetail là 1 sản phẩm, có thể khác nhau).
// Lưu thông tin chi tiết:
// Sản phẩm nào (product)
// Số lượng bao nhiêu (quantity)
// Giá tại thời điểm thêm vào giỏ (price)
// Thuộc về giỏ hàng nào (cart)

// Ví dụ minh họa
// Cart:
// User A có 1 giỏ hàng.

// CartDetail:
// Trong giỏ hàng đó có 2 sản phẩm:
// 1 laptop Dell, số lượng 1
// 2 chuột Logitech, số lượng 2

// Tóm lại
// Cart là "cái giỏ" tổng thể của user.
// CartDetail là "từng món hàng" nằm trong cái giỏ đó.



@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 0) 
    // Để hiển thị cái số trên giỏ  hàng
    private int sum;

    //user_id
    // 1 giỏ hàng thuộc 1 user
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; 

    //card_details_id
    // 1 giỏ hàng có nhiều bản ghi 
    @OneToMany(mappedBy = "cart")
    List<CartDetail> cartDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    @Override
    public String toString() {
        return "Cart [id=" + id + ", sum=" + sum + ", user=" + user + ", cartDetails=" + cartDetails + "]";
    }

    


}
