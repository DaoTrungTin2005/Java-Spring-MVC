package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.repository.RoleRepository;
import vn.hoidanit.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public String handleHello() {
        return "hello from service";
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public List<User> getAllUserByEmails(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    public User handleSaveUser(User user) {
        User eric = this.userRepository.save(user);
        System.out.println(eric);
        return eric;
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id);
    }

    public void deleteById(long id) {
        this.userRepository.deleteById(id);
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);

    }

    // Mapper
    public User registerDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();

        user.setFullname(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        // sao ko làm cho confirm password nhỉ
        return user;
    }

    // coi email coi tồn tại chưa
    public boolean checkEmailExist(String email) {

        return this.userRepository.existsByEmail(email);
    }

    // spring security
    // lấy ra user phụ thuôc vào email truyền vào
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    // Hàm count để đếm số lượng user, product, order bên admin dashboard
    public long countUsers() {
        return this.userRepository.count();
    }

    public long countProducts() {
        return this.productRepository.count();
    }

    public long countOrders() {
        return this.orderRepository.count();
    }
}
