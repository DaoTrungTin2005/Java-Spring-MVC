package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(
            UploadService uploadService,
            ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> prs = this.productService.fetchProducts();
        // lấy danh sách tất cả sản phẩm
        // xong rồi truyền qua view thông qua biến products
        model.addAttribute("products", prs);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {

        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String handleCreateProduct(
            @ModelAttribute("newProduct") @Valid Product pr,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        // upload img
        String image = this.uploadService.handleSaveUploadFile(file, "product");
        pr.setImage(image);

        this.productService.createProduct(pr);

        return "redirect:/admin/product";

    }

    @GetMapping("admin/product/delete/{id}")
    public String getDeleteProductPage(Model model,
            @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product sp) {
        this.productService.deleteProduct(sp.getId());
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product pr = this.productService.fetchProductById(id).get();
        model.addAttribute("product", pr);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Optional<Product> currentProduct = this.productService.fetchProductById(id);
        model.addAttribute("newProduct", currentProduct.get());
        return "admin/product/update";
    }

    @PostMapping("admin/product/update")
    public String handleUpdateProduct(@ModelAttribute("newProduct") @Valid Product pr,
        BindingResult newProducBindingResult,
        @RequestParam("hoidanitFile") MultipartFile file ){

            if (newProducBindingResult.hasErrors()){
                return "admin/product/update" ; 
            }
// lấy sản phẩm hiện tại
            Product currenProduct = this.productService.fetchProductById(pr.getId()).get();
            if(currenProduct != null){
                // update new image
                if (!file.isEmpty()){
                    String img = this.uploadService.handleSaveUploadFile(file,"product");
                    currenProduct.setImage(img);
                }

                currenProduct.setName(pr.getName());
                currenProduct.setPrice(pr.getPrice());
                currenProduct.setQuantity(pr.getQuantity());
                currenProduct.setDetailDesc(pr.getDetailDesc());
                currenProduct.setShortDesc(pr.getShortDesc());
                currenProduct.setFactory(pr.getFactory());
                currenProduct.setTarget(pr.getTarget());

                this.productService.createProduct(currenProduct);
            }
            return "redirect:/admin/product" ; 

    }
    

}
