package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    // DI : Dependency injection
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    // @RequestMapping("/")
    // public String getHomePage(Model model) {
    // String test = this.userService.handleHello(); // controller gọi từ model
    // (service) để tính toán hàm, dữ liệu
    // model.addAttribute("eric", test); // model sau khi có data rồi nó sẽ gửi lại
    // thằng controller
    // model.addAttribute("tindao", "from controller with model");
    // return "hello"; // controller xử lí data và gửi lại cho view
    // } // view render ra html

    @RequestMapping("/")
    public String getHomePage(Model model) {
        List<User> arrUsers = this.userService.getAllUserByEmails("eric@gmail.com");
        System.out.println(arrUsers);
        model.addAttribute("eric", "test");
        model.addAttribute("tindao", "from controller with model");
        return "hello";
    }

    @RequestMapping("/admin/user") // get
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUser();
        System.out.println("check users : " + users);
        model.addAttribute("users1", users);
        return "admin/user/table-user";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        System.out.println("=======================");
        System.out.println("check path id = " + id);
        System.out.println("=======================");
        model.addAttribute("user", user);
        model.addAttribute("id", id); // truyền từ controller qua view
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/create") // get
    public String getCreateUserPage(Model model) {
        // lúc tạo mỡi chưa có người dùng thì truyền dô rỗng
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    // lấy data từ view
    public String createUserPage(Model model, @ModelAttribute("newUser") User Daotrungtin) {
        System.out.println("run here" + Daotrungtin);
        // lưu vào database lun
        this.userService.handleSaveUser(Daotrungtin);
        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        // Khi update truyền giá trị người dùng lấy từ database -> data sẽ dc filled vào
        // sẵn
        // khi create do chưa có ng dùng nên truyền vào rỗng
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User Daotrungtin) {
        User currentUser = this.userService.getUserById(Daotrungtin.getId());
        if (currentUser != null) {
            currentUser.setAddress(Daotrungtin.getAddress());
            currentUser.setFullname(Daotrungtin.getFullname());
            currentUser.setPhone(Daotrungtin.getPhone());
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        // User user = new User();
        // user.setId(id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User Eric) {
        this.userService.deleteById(Eric.getId());
        return "redirect:/admin/user";
    }

}

// @RestController
// public class UserController {

// // DI : Dependency injection
// private UserService userService ;

// public UserController(UserService userService) {
// this.userService = userService;
// }

// @GetMapping("")
// public String getHomePage(){
// return this.userService.handleHello() ;
// }
// }