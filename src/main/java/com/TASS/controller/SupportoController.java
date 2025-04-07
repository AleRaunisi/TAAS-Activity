package com.TASS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupportoController {

    @GetMapping("/supporto")
    public String esploraPage() {
        return "supporto";  
    }
    
}
