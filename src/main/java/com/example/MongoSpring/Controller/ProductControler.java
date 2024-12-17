package com.example.MongoSpring.Controller;

import java.util.List;

import com.example.MongoSpring.Enity.QuantityUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MongoSpring.Enity.Product;
import com.example.MongoSpring.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductControler {
    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{category}")
    public List<Product> getProduct(@PathVariable String category) {
        return productService.getProduct(category);
    }

    @PostMapping("/insert")
    public Product insert(@RequestBody Product product) {
        return productService.addProoducts(product);

    }

    @PutMapping("/update")
    public Product update(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @PutMapping("/update-quantity")
    public Product decreaseQuantity(@RequestBody QuantityUpdateRequest request) {
        return productService.updateQuantity(request.getName(), request.getQuantity());
    }

    @DeleteMapping("/delete/{id}")
    public Product delete(@PathVariable int id, @RequestBody Product product) {
        return productService.deleProduct(id);
    }
}
