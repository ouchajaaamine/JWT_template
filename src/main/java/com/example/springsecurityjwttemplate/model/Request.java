package com.example.springsecurityjwttemplate.model;

import lombok.Data;

import java.util.List;

@Data
public class Request {

    private String username;
    private String password;
    private List<String> roles; // Ajout des rôles

}