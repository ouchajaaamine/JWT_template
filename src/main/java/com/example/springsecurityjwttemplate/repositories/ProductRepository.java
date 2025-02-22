package com.example.springsecurityjwttemplate.repositories;

import com.example.springsecurityjwttemplate.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}