package com.example.springsecurityjwttemplate.controller;

import com.example.springsecurityjwttemplate.Services.UserService;
import com.example.springsecurityjwttemplate.model.Request;
import com.example.springsecurityjwttemplate.model.Role;
import com.example.springsecurityjwttemplate.model.User;
import com.example.springsecurityjwttemplate.repositories.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final RoleRepository roleRepository;

    private final UserService userService;

    public AuthController(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }
    @GetMapping("/home")
    public String home() {
        return "hello" ;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Request request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            List<Role> roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found - " + roleName)))
                    .collect(Collectors.toList());

            user.setRoles(roles);
            User registeredUser = userService.register(user);
            return ResponseEntity.ok("User registered successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
