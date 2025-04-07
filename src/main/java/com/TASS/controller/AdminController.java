package com.TASS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/homeadmin")
    public String secretPage() {
        return "homeadmin"; 
    }
}