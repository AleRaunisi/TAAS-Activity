
package com.TASS.service;

import com.TASS.controller.AttivitaController;
import com.TASS.model.Attivita;
import com.TASS.model.Partecipazione;
import com.TASS.model.Utente;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.TASS.repository.AttivitaRepository;
import com.TASS.repository.UtenteRepository;
import com.TASS.service.UtenteService;
import com.TASS.repository.PartecipazioneRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttivitaService {

    @Autowired
    private final AttivitaRepository attivitaRepository;

    @Autowired
    private  PartecipazioneRepository partecipazioneRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UtenteService utenteService;

    

    

    private static final Logger log = LoggerFactory.getLogger(AttivitaController.class);


    @Autowired
    public AttivitaService(AttivitaRepository attivitaRepository) {
        this.attivitaRepository = attivitaRepository;
    }

    public List<Attivita> getAttivitaByKeyword(String keyword) {
        return attivitaRepository.findByTitoloContaining(keyword);
    }

    // Recupera tutte le attività
    public List<Attivita> getAllAttivita() {
        return attivitaRepository.findAll();
    }

    public Attivita getActivityById(Long id) {
        return attivitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attività non trovata!"));
    }

    public boolean bookSpot(Long activityId) {
        Optional<Attivita> activityOpt = attivitaRepository.findById(activityId);

        if (activityOpt.isPresent()) {
            Attivita activity = activityOpt.get();
            if (activity.getAvailableSpots() > 0) {
                activity.setAvailableSpots(activity.getAvailableSpots() - 1);
                attivitaRepository.save(activity);
                return true;
            }
        }

        return false; // Nessun posto disponibile o attività non trovata
    }

    // Recupera un'attività per ID
    public Attivita getAttivitaById(Long id) {
        return attivitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attività non trovata!"));
    }
    
    public synchronized Attivita createAttivita(Attivita attivita) {
        Optional<Attivita> existing = attivitaRepository.findByTitoloAndLuogoAndDataInizio(
            attivita.getTitolo(), attivita.getLuogo(), attivita.getDataInizio()
        );

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Attività già esistente!");
        }

        return attivitaRepository.save(attivita);
    }

    @Transactional
    public void cancelBooking(Long attivitaId, String email) {
        log.info("Inizio annullamento prenotazione - Attività ID: {}, Utente: {}", attivitaId, email);
    
        try {
            // Trova l'utente tramite email
            Utente utente = utenteRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.error("Utente non trovato con email: {}", email);
                        return new IllegalArgumentException("Utente non trovato con email: " + email);
                    });
            log.info("Utente trovato: ID = {}", utente.getId());
    
            // Trova l'attività tramite ID
            Attivita attivita = attivitaRepository.findById(attivitaId)
                    .orElseThrow(() -> {
                        log.error("Attività non trovata con ID: {}", attivitaId);
                        return new IllegalArgumentException("Attività non trovata con ID: " + attivitaId);
                    });
            log.info("Attività trovata: ID = {}, Titolo = {}", attivita.getId(), attivita.getTitolo());
    
            // Trova la prenotazione associata all'utente e all'attività
            Partecipazione partecipazione = partecipazioneRepository.findByUtenteAndAttivita(utente, attivita)
                    .orElseThrow(() -> {
                        log.error("Prenotazione non trovata per Utente ID: {} e Attività ID: {}", utente.getId(), attivitaId);
                        return new IllegalArgumentException("Prenotazione non trovata per questa attività e utente");
                    });
            log.info("Prenotazione trovata: ID = {}", partecipazione.getId());
    
            // Elimina la prenotazione
            partecipazioneRepository.delete(partecipazione);
            log.info("Prenotazione eliminata con successo");
    
            // Incrementa i posti disponibili
            attivita.setAvailableSpots(attivita.getAvailableSpots() + 1);
            attivitaRepository.save(attivita);
            log.info("Posti disponibili aggiornati. Nuovi posti: {}", attivita.getAvailableSpots());
    
            log.info("Fine annullamento prenotazione");
        } catch (Exception e) {
            log.error("Errore durante l'annullamento della prenotazione: {}", e.getMessage(), e);
            throw e; // Rilancia l'errore per farlo gestire dal Controller
        }
    }
    

    // Aggiorna un'attività esistente
    public Attivita updateAttivita(Long id, Attivita updatedAttivita) {
        Attivita attivita = getAttivitaById(id);
        attivita.setTitolo(updatedAttivita.getTitolo());
        attivita.setDescrizione(updatedAttivita.getDescrizione());
        attivita.setLuogo(updatedAttivita.getLuogo());
        attivita.setDataInizio(updatedAttivita.getDataInizio());
        attivita.setDataFine(updatedAttivita.getDataFine());
        attivita.setCategoria(updatedAttivita.getCategoria());
        attivita.setMaxPartecipanti(updatedAttivita.getMaxPartecipanti());
        return attivitaRepository.save(attivita);
    }

    public Attivita prenotaAttivita(Long id) {
        Optional<Attivita> optionalAttivita = attivitaRepository.findById(id);
        if (optionalAttivita.isEmpty()) {
            log.error("Attività non trovata con ID: {}", id);
            return null;
        }
        Attivita attivita = optionalAttivita.get();
    
        // Verifica che ci siano posti disponibili
        if (attivita.getTotalSpots() == 0) {
            log.error("Non ci sono posti disponibili per l'attività con ID: {}", id);
            return null;
        }
    
        // Aggiorna max_partecipanti se necessario
        attivita.setMaxPartecipanti(attivita.getTotalSpots());
    
        // Altri controlli e salvataggio
        attivita.setAvailableSpots(attivita.getAvailableSpots() - 1);
        return attivitaRepository.save(attivita);
    }

    public String saveImage(MultipartFile file) {
        try {
            // Directory dove salvare le immagini
            String uploadDir = "/Users/ale/Downloads/demo/target/classes/static/images";
            Path uploadPath = Paths.get(uploadDir);

            // Crea la directory se non esiste
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Genera un nome univoco per l'immagine
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            System.out.println("File salvato con percorso: " + filePath.toAbsolutePath());

            // Salva il file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il salvataggio dell'immagine", e);
        }
    }

    // Elimina un'attività per ID
    public void deleteAttivita(Long id) {
        if (!attivitaRepository.existsById(id)) {
            throw new IllegalArgumentException("Attività con ID " + id + " non trovata!");
        }
        attivitaRepository.deleteById(id);
    }
    // Recupera l'immagine dalla cartella 'static/images'
    public byte[] getImage(String imageName) {
        try {
            Resource resource = new ClassPathResource("static/images/" + imageName);
            return Files.readAllBytes(resource.getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException("Immagine non trovata: " + imageName);
        }
    }


    public List<Attivita> getPrenotazioniByEmail(String email) {
        Long userId = utenteService.findIdByEmail(email);
        return attivitaRepository.findByPartecipantiId(userId);
    }

    public List<Attivita> getFilteredAttivita(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            // Cerca nel titolo delle attività solo se la keyword è presente
            return attivitaRepository.findByTitoloContaining(keyword);
        } else {
            // Restituisci tutte le attività se la keyword è vuota
            return null;
        }
    }
    
}
