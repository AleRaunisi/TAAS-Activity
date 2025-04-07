package com.TASS.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PrenotazioneDto {

    @NotNull(message = "L'ID dell'attività è obbligatorio")
    private Long attivitaId;

    @NotNull(message = "La data è obbligatoria")
    @FutureOrPresent(message = "La data deve essere nel presente o nel futuro")
    private LocalDateTime dataPrenotazione;

    @NotNull(message = "Il nome dell'utente è obbligatorio")
    @Size(min = 2, message = "Il nome deve essere lungo almeno 2 caratteri")
    private String nomeUtente;

    // Getter e Setter
    public Long getAttivitaId() {
        return attivitaId;
    }

    public void setAttivitaId(Long attivitaId) {
        this.attivitaId = attivitaId;
    }

    public LocalDateTime getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(LocalDateTime dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }
}