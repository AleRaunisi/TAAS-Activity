package com.TASS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MobileController {

    @GetMapping("/mobile")
    public String mobilePage() {
        return "mobile-page"; 
    }
}