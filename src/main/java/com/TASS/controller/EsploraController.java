package com.TASS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EsploraController {
    @GetMapping("/esplora")
    public String esploraPage() {
        return "esplora";  
    }
}