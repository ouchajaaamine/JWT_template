package com.example.springsecurityjwttemplate.Services;


import com.example.springsecurityjwttemplate.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    List<Product> GetAllProducts();
    Product AddProduct(Product product);
    Product GetProductById(Long id);
    Product UpdateProduct(Long id, Product product);

    void DeleteProduct(Long id);
}
