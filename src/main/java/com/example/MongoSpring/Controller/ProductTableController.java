package com.example.MongoSpring.Controller;

import com.example.MongoSpring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductTableController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getProducts(Model model) {

        var products = productService.getProducts();

        model.addAttribute("products", products);

        return "products";
    }
}
