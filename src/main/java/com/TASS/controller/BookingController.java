package com.TASS.controller;

import org.springframework.web.bind.annotation.RequestParam;
import com.TASS.model.Attivita;
import com.TASS.service.AttivitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingController {

    @Autowired
    private AttivitaService attivitaService;

    @GetMapping("/activity")
    public String activityPage(@RequestParam("id") Long id, Model model) {
        try {
            // Recupera i dettagli dell'attività dal database
            Attivita attivita = attivitaService.getAttivitaById(id);

            // Passa i dettagli dell'attività al modello
            model.addAttribute("attivita", attivita);

            return "activity"; // Restituisce la pagina di dettaglio dell'attività
        } catch (RuntimeException e) {
            // Se l'attività non viene trovata, mostra una pagina di errore
            model.addAttribute("error", "Attività non trovata!");
            return "error"; // Restituisce la pagina di errore
        }
    }
}