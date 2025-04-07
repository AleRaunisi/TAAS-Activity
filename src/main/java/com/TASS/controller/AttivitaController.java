package com.TASS.controller;

import com.TASS.model.Attivita;
import com.TASS.repository.AttivitaRepository;
import com.TASS.service.AttivitaService;
import com.TASS.service.UtenteService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/attivita")
public class AttivitaController {

    private static final Logger log = LoggerFactory.getLogger(AttivitaController.class);

    private final AttivitaService attivitaService;
    private final AttivitaRepository attivitaRepository;
    private final UtenteService utenteService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AttivitaController(AttivitaService attivitaService,
                             AttivitaRepository attivitaRepository,
                             UtenteService utenteService,
                             RabbitTemplate rabbitTemplate) {
        this.attivitaService = attivitaService;
        this.attivitaRepository = attivitaRepository;
        this.utenteService = utenteService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public List<Attivita> getAllAttivita(@RequestParam(required = false) String keyword, 
                                        Authentication authentication) {
        List<Attivita> attivitaList;

        if (keyword != null && !keyword.isEmpty()) {
            attivitaList = attivitaRepository.findByTitoloOrCategoria(keyword);
        } else {
            attivitaList = attivitaRepository.findAll();
        }

        // Controlla se l'utente è autenticato
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUserEmail = extractEmailFromAuthentication(authentication);
            Long currentUserId = utenteService.findIdByEmail(currentUserEmail);
            
            // Controlla se l'utente ha il ruolo di Admin
            boolean isAdmin = authentication.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
            log.error("Errore prenotazioni: è 123admin", isAdmin);
            for (Attivita attivita : attivitaList) {
                // L'utente può eliminare se è il creatore o se è un Admin
                attivita.setCanDelete(isAdmin || attivita.getOrganizzatoreId().equals(currentUserId));
            }
        }

        return attivitaList;
    }

    @GetMapping("/attivita")
    public List<Attivita> getFilteredAttivita(@RequestParam(required = false) String keyword) {
        return attivitaService.getFilteredAttivita(keyword);
    }

    @GetMapping("/prenotazioni")
    public ResponseEntity<?> getUserBookings(Authentication authentication) {
        log.info("Richiesta prenotazioni per utente autenticato");
        
        try {
            String email = extractEmailFromAuthentication(authentication);
            log.info("Utente autenticato: {}", email);
            
            List<Attivita> prenotazioni = attivitaService.getPrenotazioniByEmail(email);
            return prenotazioni.isEmpty() ? 
                ResponseEntity.ok("Nessuna prenotazione trovata") : 
                ResponseEntity.ok(prenotazioni);
                
        } catch (Exception e) {
            log.error("Errore prenotazioni: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore recupero prenotazioni");
        }
    }

    @GetMapping("/{id}")
    public Attivita getAttivitaById(@PathVariable Long id) {
        return attivitaService.getAttivitaById(id);
    }

    @GetMapping("/images/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageName) {
        return attivitaService.getImage(imageName); // Ottieni l'immagine dal servizio
    }
    

    @PostMapping(consumes = {"multipart/form-data"})
public ResponseEntity<?> createAttivita(
        @RequestParam("titolo") String titolo,
        @RequestParam("descrizione") String descrizione,
        @RequestParam("luogo") String luogo,
        @RequestParam("maxPartecipanti") Integer maxPartecipanti,
        @RequestParam("dataInizio") String dataInizio,
        @RequestParam("dataFine") String dataFine,
        @RequestParam("categoria") String categoria,
        @RequestParam(value = "image", required = false) MultipartFile image,
        Authentication authentication) {
    try {
        // Estrai l'email dell'organizzatore autenticato
        String email = extractEmailFromAuthentication(authentication);
        Long organizzatoreId = utenteService.findIdByEmail(email);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime dataInizioParsed = LocalDateTime.parse(dataInizio, formatter);
        LocalDateTime dataFineParsed = LocalDateTime.parse(dataFine, formatter);

        // Creazione dell'oggetto Attivita
        Attivita attivita = new Attivita();
        attivita.setTitolo(titolo);
        attivita.setDescrizione(descrizione);
        attivita.setLuogo(luogo);
        attivita.setMaxPartecipanti(maxPartecipanti);
        attivita.setCategoria(categoria);
        attivita.setDataInizio(Date.valueOf(dataInizioParsed.toLocalDate()));
        attivita.setDataFine(Date.valueOf(dataFineParsed.toLocalDate()));
        attivita.setOrganizzatoreId(organizzatoreId);
        attivita.setAvailableSpots(maxPartecipanti);
        attivita.setTotalSpots(maxPartecipanti);

        if (descrizione.length() < 10 || descrizione.length() > 500) {
            return ResponseEntity.badRequest().body("La descrizione deve avere tra 10 e 500 caratteri.");
        }

        // Gestione immagine
        if (image != null && !image.isEmpty()) {
            String imageName = attivitaService.saveImage(image);
            attivita.setImage(imageName);
        }

        // Salva l'attività
        Attivita nuovaAttivita = attivitaService.createAttivita(attivita);

        // **INSERISCI QUI IL CODICE PER INVIARE IL MESSAGGIO A RABBITMQ**
        String messaggio = String.format(
            "Nuova attività creata: %s\nOrganizzatore: %s\nLuogo: %s\nData: %s",
            titolo, email, luogo, dataInizioParsed
        );

        rabbitTemplate.convertAndSend(
            "tasks-exchange",  // Nome dell'exchange
            "task.created",    // Routing key
            messaggio          // Messaggio da inviare
        );

        log.info("Messaggio inviato a RabbitMQ: {}", messaggio);

        // Restituisci la risposta al client
        return ResponseEntity.ok(nuovaAttivita);
    } catch (Exception e) {
        log.error("Errore durante la creazione dell'attività: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore durante la creazione dell'attività: " + e.getMessage());
    }
}

@DeleteMapping("/{id}/cancel-booking")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication authentication) {
        log.info("Richiesta DELETE ricevuta per annullare prenotazione - Attività ID: {}", id);

        try {
            // Estrai l'email dell'utente autenticato
            String email = extractEmailFromAuthentication(authentication);
            log.info("Utente autenticato: {}", email);

            // Chiama il service per annullare la prenotazione
            attivitaService.cancelBooking(id, email);

            return ResponseEntity.ok("Prenotazione annullata con successo!");
        } catch (IllegalArgumentException e) {
            log.error("Errore: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Errore inaspettato: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'annullamento della prenotazione.");
        }
    }

    @PutMapping("/{id}")
    public Attivita updateAttivita(@PathVariable Long id, @RequestBody Attivita attivita) {
        return attivitaService.updateAttivita(id, attivita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttivita(
        @PathVariable Long id, 
        Authentication authentication) { // Aggiungi il parametro Authentication
        
        try {
            // Estrai l'email dell'utente autenticato
            String email = extractEmailFromAuthentication(authentication);
            
            // Trova l'ID dell'utente dal suo email
            Long userId = utenteService.findIdByEmail(email);
            
            // Recupera l'attività dal database
            Attivita attivita = attivitaService.getAttivitaById(id);
            
            
            
            // Se è l'organizzatore, procedi con l'eliminazione
            attivitaService.deleteAttivita(id);
            return ResponseEntity.ok("Attività eliminata");
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore eliminazione attività");
        }
    }

    private String extractEmailFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Utente non autenticato");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof DefaultOAuth2User) {
            return ((DefaultOAuth2User) principal).getAttribute("email");
        } else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        throw new IllegalArgumentException("Tipo autenticazione non supportato");
    }
}