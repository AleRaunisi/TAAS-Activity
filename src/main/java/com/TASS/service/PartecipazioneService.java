
package com.TASS.service;

import com.TASS.model.Attivita;
import com.TASS.model.Partecipazione;
import com.TASS.model.Utente;
import com.TASS.repository.AttivitaRepository;
import com.TASS.repository.PartecipazioneRepository;
import com.TASS.repository.UtenteRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartecipazioneService {

    @Autowired
    private PartecipazioneRepository partecipazioneRepository;

    @Autowired
    private AttivitaRepository attivitaRepository;

    @Autowired
    private UtenteRepository utenteRepository;


    // Recupera tutte le partecipazioni
    public List<Partecipazione> getAllPartecipazioni() {
        return partecipazioneRepository.findAll();
    }

    // Recupera una partecipazione per ID
    public Partecipazione getPartecipazioneById(Long id) {
        return partecipazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partecipazione non trovata!"));
    }

    // Crea una nuova partecipazione
    public Partecipazione createPartecipazione(Partecipazione partecipazione) {
        return partecipazioneRepository.save(partecipazione);
    }

    // Elimina una partecipazione per ID
    public void deletePartecipazione(Long id) {
        partecipazioneRepository.deleteById(id);
    }

    public Partecipazione prenotaAttivita(Long attivitaId, Long utenteId) {
        // Trova l'attività
        Attivita attivita = attivitaRepository.findById(attivitaId)
                .orElseThrow(() -> new RuntimeException("Attività non trovata"));

        // Trova l'utente
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Controlla se l'utente ha già prenotato questa attività
        if (partecipazioneRepository.findByUtenteAndAttivita(utente, attivita).isPresent()) {
            throw new RuntimeException("Hai già prenotato questa attività");
        }

        // Controlla i posti disponibili
        if (attivita.getAvailableSpots() <= 0) {
            throw new RuntimeException("Non ci sono posti disponibili per questa attività");
        }

        // Crea una nuova partecipazione
        Partecipazione nuovaPartecipazione = new Partecipazione();
        nuovaPartecipazione.setUtente(utente);
        nuovaPartecipazione.setAttivita(attivita);
        nuovaPartecipazione.setStato(Partecipazione.Stato.CONFERMATO);
        nuovaPartecipazione.setDataIscrizione(new Date());
        partecipazioneRepository.save(nuovaPartecipazione);

        // Aggiorna i posti disponibili
        attivita.setAvailableSpots(attivita.getAvailableSpots() - 1);
        attivitaRepository.save(attivita);

        return nuovaPartecipazione;
    }
}
