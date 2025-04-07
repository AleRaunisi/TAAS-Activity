package com.TASS.controller;

import com.TASS.dto.UserDto;
import com.TASS.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UtenteService utenteService;

    // Mostra il form di registrazione
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Viene restituito il nome della pagina HTML di registrazione
    }

    // Gestisce la registrazione dell'utente
    @PostMapping("/register")
    public String registerUser(UserDto userDto, Model model) {
        try {
            // Registra l'utente nel database
            utenteService.registerUser(userDto);
            model.addAttribute("message", "Registrazione riuscita! Puoi ora effettuare il login.");
            return "login"; // Reindirizza alla pagina di login
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Se c'è un errore, mostra di nuovo il form di registrazione con un messaggio di errore
        }
    }
}