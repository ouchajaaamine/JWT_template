package com.example.springsecurityjwttemplate;

import com.example.springsecurityjwttemplate.model.Role;
import com.example.springsecurityjwttemplate.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SpringSecurityJwtTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtTemplateApplication.class, args);
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

            for (String roleName : roles) {
                Optional<Role> roleOptional = roleRepository.findByName(roleName);
                if (roleOptional.isEmpty()) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                    System.out.println(role + " created");
                }
            }
        };
    }
}
