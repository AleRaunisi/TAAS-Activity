package com.TASS.repository;

import com.TASS.model.Attivita;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttivitaRepository extends JpaRepository<Attivita, Long> {
    // JpaRepository già fornisce il metodo findById(Long id)
    // Non è necessario aggiungere la dichiarazione di findById a meno che tu non voglia una personalizzazione
    // Metodo personalizzato per trovare attività per categoria
    List<Attivita> findByCategoria(String categoria);
    
    List<Attivita> findByTitoloContaining(String keyword);

    @Query("SELECT a FROM Attivita a WHERE LOWER(a.titolo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Attivita> findByKeyword(@Param("keyword") String keyword);

    // Metodo personalizzato per trovare attività per organizzatore
    List<Attivita> findByOrganizzatoreId(Long organizzatoreId);

    List<Attivita> findByTitoloContainingIgnoreCase(String titolo);

    @Query("SELECT a FROM Attivita a WHERE LOWER(a.titolo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.categoria) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Attivita> findByTitoloOrCategoria(@Param("keyword") String keyword);

    Optional<Attivita> findByTitoloAndLuogoAndDataInizio(String titolo, String luogo, Date dataInizio);

    @Query("SELECT a FROM Attivita a JOIN a.partecipanti p WHERE p.id = :userId")
    List<Attivita> findByPartecipantiId(@Param("userId") Long userId);

    // Metodo personalizzato per attività in un intervallo di date
    List<Attivita> findByDataInizioBetweenAndDataFineBetween(
            java.util.Date dataInizioStart, java.util.Date dataInizioEnd,
            java.util.Date dataFineStart, java.util.Date dataFineEnd);
}