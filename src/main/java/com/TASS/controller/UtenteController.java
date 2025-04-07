package com.TASS.controller;

import com.TASS.model.Utente;
import com.TASS.dto.UserDto;
import com.TASS.service.CustomUserDetails;
import com.TASS.service.UtenteService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.util.List;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    // GET: Ottieni tutti gli utenti
    @GetMapping
    public List<Utente> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

  
    
    // GET: Ottieni utente per ID
    @GetMapping("/{id}")
    public Utente getUtenteById(@PathVariable Long id) {
        return utenteService.getUtenteById(id);
    }

  

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        utenteService.registerUser(userDto);
        return ResponseEntity.ok("Utente registrato con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = utenteService.authenticateUser(email, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login effettuato con successo");
        } else {
            return ResponseEntity.status(401).body("Credenziali non valide");
        }
    }

    // POST: Crea un nuovo utente
    @PostMapping
    public Utente createUtente(@RequestBody Utente utente) {
        return utenteService.createUtente(utente);
    }

    // DELETE: Elimina un utente
    @DeleteMapping("/{id}")
    public void deleteUtente(@PathVariable Long id) {
        utenteService.deleteUtente(id);
    }
}