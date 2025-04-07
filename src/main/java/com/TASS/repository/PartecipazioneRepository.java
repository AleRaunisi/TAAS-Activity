package com.TASS.repository;

import com.TASS.model.Attivita;
import com.TASS.model.Partecipazione;
import com.TASS.model.Utente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartecipazioneRepository extends JpaRepository<Partecipazione, Long> {
    // Metodo personalizzato per trovare partecipazioni di un utente
    List<Partecipazione> findByUtenteId(Long utenteId);

    // Metodo personalizzato per trovare partecipazioni a un'attività
    List<Partecipazione> findByAttivitaId(Long attivitaId);

    Optional<Partecipazione> findByUtenteAndAttivita(Utente utente, Attivita attivita);
}