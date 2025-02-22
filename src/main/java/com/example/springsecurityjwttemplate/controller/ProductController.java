package com.example.springsecurityjwttemplate.controller;

import com.example.springsecurityjwttemplate.Services.ProductService;
import com.example.springsecurityjwttemplate.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping

    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.AddProduct(product);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.GetAllProducts();
    return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.GetProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.UpdateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.DeleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
