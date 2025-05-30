package vn.hoidanit.laptopshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;

@Controller
public class ItemController {

    private final ProductService productService ;

    public ItemController(ProductService productService) {
        this.productService = productService ; 
    }

    @GetMapping("/product/{id}")
    public String getProductPage(Model   model, @PathVariable long id) {
        // lấy thông tin sp bằng id
        // có .get là do có optional
        // lấy thông tin đối tượng truyền qua view
        Product pr = this.productService.fetchProductById(id).get();
        model.addAttribute("product", pr);
        model.addAttribute("id", id);
        return "client/product/detail";
    }
}
