package com.TASS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupportoController2 {

    @GetMapping("/supporto2")
    public String esploraPage() {
        return "supporto2"; 
    }
    
}
