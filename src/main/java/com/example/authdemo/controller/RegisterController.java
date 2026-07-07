package com.example.authdemo.controller;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {


    private final InMemoryUserDetailsManager userDetailsService;
    private final PasswordEncoder passwordEncoder;


    public RegisterController(
            InMemoryUserDetailsManager userDetailsService,
            PasswordEncoder passwordEncoder) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;

    }



    @GetMapping("/register")
    public String registerPage() {

        return "register";

    }



    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {


        if (userDetailsService.userExists(username)) {

            model.addAttribute(
                    "error",
                    "Username already exists"
            );

            return "register";
        }



        userDetailsService.createUser(
                User.withUsername(username)
                        .password(passwordEncoder.encode(password))
                        .roles("USER")
                        .build()
        );



        model.addAttribute(
                "success",
                "Account created successfully. Please login."
        );


        return "register";

    }

}