package com.TASS.controller;

import com.TASS.model.Attivita;
import com.TASS.model.Partecipazione;
import com.TASS.service.PartecipazioneService;
import com.TASS.service.AttivitaService;
import com.TASS.service.UtenteService;
import org.springframework.security.core.Authentication;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/partecipazioni")
public class PartecipazioneController {

    @Autowired
    private PartecipazioneService partecipazioneService;
    
    @Autowired
    private AttivitaService attivitaService;

    @Autowired
    private UtenteService utenteService;

    

    private static final Logger log = LoggerFactory.getLogger(AttivitaController.class);


    // GET: Ottieni tutte le partecipazioni
    @GetMapping
    public List<Partecipazione> getAllPartecipazioni() {
        return partecipazioneService.getAllPartecipazioni();
    }

    // GET: Ottieni partecipazione per ID
    @GetMapping("/{id}")
    public Partecipazione getPartecipazioneById(@PathVariable Long id) {
        return partecipazioneService.getPartecipazioneById(id);
    }

    // POST: Crea una nuova partecipazione
    @PostMapping
    public Partecipazione createPartecipazione(@RequestBody Partecipazione partecipazione) {
        return partecipazioneService.createPartecipazione(partecipazione);
    }

    // DELETE: Elimina una partecipazione
    @DeleteMapping("/{id}")
    public void deletePartecipazione(@PathVariable Long id) {
        partecipazioneService.deletePartecipazione(id);
    }

    @PostMapping("/{id}/prenota")
    public ResponseEntity<?> prenotaAttivita(@PathVariable Long id, Authentication authentication) {
        try {
            String email = extractEmailFromAuthentication(authentication);
            Long utenteId = utenteService.findIdByEmail(email);
            Partecipazione partecipazione = partecipazioneService.prenotaAttivita(id, utenteId);
            Attivita attivita = attivitaService.getAttivitaById(id); // Recupera l'attività aggiornata
            return ResponseEntity.ok(attivita); // Restituisci l'attività con i posti aggiornati
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Hai già prenotato questa attività")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Non puoi prenotare due volte la stessa attività"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Errore interno del server"));
        }
    }
    
    
    public class ErrorResponse {
        private String message;
    
        public ErrorResponse(String message) {
            this.message = message;
        }
    
        public String getMessage() {
            return message;
        }
    
        public void setMessage(String message) {
            this.message = message;
        }
    }
    

    private String extractEmailFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Utente non autenticato");
        }
    
        Object principal = authentication.getPrincipal();
    
        // Gestione di utenti locali (UserDetails)
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
    
        // Gestione di OAuth2 (attributi utente come mappa)
        if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauthUser = (DefaultOAuth2User) principal;
            return (String) oauthUser.getAttributes().get("email");
        }
    
        // Altri casi (opzionale)
        throw new IllegalArgumentException("Tipo di autenticazione non supportato: " + principal.getClass());
    }
    

    
    
    
    




}