package vn.hoidanit.laptopshop.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        model.addAttribute("eric", "test");
        model.addAttribute("tindao", "from controller with model");
        return "hello";
    }

    @RequestMapping("/admin/user") // get
    public String getUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createUserPage(Model model, @ModelAttribute("newUser") User Daotrungtin) {
        System.out.println("run here" + Daotrungtin);
        // lưu vào database lun
        this.userService.handleSaveUser(Daotrungtin);
        return "hello";
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