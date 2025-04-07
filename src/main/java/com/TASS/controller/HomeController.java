package com.TASS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class HomeController {

   @GetMapping("/") // Gestisce la root
   public String homepage(@RequestHeader("User-Agent") String userAgent) {
        if (userAgent.contains("Mobi")) {
            return "redirect:/mobile"; 
        } else {
            return "home2"; 
        }
    }

    @GetMapping("/home")
    public String homepages() {
        return "home";  
    }
}