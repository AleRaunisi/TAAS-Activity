package com.TASS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError() {
        // Ritorna una vista personalizzata per gli errori generali
        return "error"; 
    }

    @GetMapping("/error/403")
    public String accessDenied() {
        // Ritorna una vista per accesso negato
        return "error403"; 
    }
}