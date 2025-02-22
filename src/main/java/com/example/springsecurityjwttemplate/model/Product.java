package com.example.springsecurityjwttemplate.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double price;

}
