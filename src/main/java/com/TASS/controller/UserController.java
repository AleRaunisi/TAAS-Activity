package com.TASS.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.TASS.service.CustomUserDetails;

@Controller
public class UserController {

    @GetMapping("/user")
    public String userProfile(@AuthenticationPrincipal Object user, Model model) {
        if (user == null) {
            return "redirect:/login"; // Reindirizza al login se non autenticato
        }

        String nome = null;
        String email = null;

        if (user instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) user;
            nome = oauthUser.getAttribute("name");
            email = oauthUser.getAttribute("email");
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);
            model.addAttribute("immagine", oauthUser.getAttribute("picture"));
        } else if (user instanceof CustomUserDetails) {
            CustomUserDetails customUser = (CustomUserDetails) user;
            nome = customUser.getNome();
            email = customUser.getUsername();
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);
            model.addAttribute("immagine", "/images/padel.jpg");
        }

        if ("raunisi.alex@gmail.compare".equals(email)) {
            model.addAttribute("ruolo", "ADMIN");
            return "user-profile2"; // Mostra la dashboard admin
        }

        return "user-profile"; // Ritorna la pagina del profilo
    }
}