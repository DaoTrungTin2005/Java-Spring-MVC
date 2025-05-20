package vn.hoidanit.laptopshop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "Hello World from Tin Dao";
    }

    @GetMapping("/user")
    public String userPage() {
        return "only user";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "only admin";
    }
}
