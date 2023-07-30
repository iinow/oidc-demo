package com.example.oidc_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/profiles")
@Controller
public class ProfileController {

    @GetMapping
    public String getProfile() {
        return "profile";
    }
}
