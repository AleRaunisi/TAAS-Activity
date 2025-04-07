package com.TASS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
public class Attivita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_spots")
    private int totalSpots;

    
    @ManyToMany
    @JoinTable(
        name = "PARTECIPAZIONE", // Nome della tabella di join
        joinColumns = @JoinColumn(name = "attivita_id"),
        inverseJoinColumns = @JoinColumn(name = "utente_id")
    )
    private List<Utente> partecipanti;

    @Column(name = "available_spots")
    private int availableSpots;

    @NotNull(message = "Il titolo è obbligatorio.")
    @Size(min = 5, max = 100, message = "Il titolo deve avere tra 5 e 100 caratteri.")
    private String titolo;

    @NotNull(message = "La descrizione è obbligatoria.")
    @Size(min = 10, max = 500, message = "La descrizione deve avere tra 10 e 500 caratteri.")
    private String descrizione;

    @NotNull(message = "Il luogo è obbligatorio.")
    private String luogo;

    private String image;

    @NotNull(message = "La data di inizio è obbligatoria.")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInizio;

    @NotNull(message = "La data di fine è obbligatoria.")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFine;

    @NotNull(message = "La categoria è obbligatoria.")
    private String categoria;

    @NotNull(message = "Il numero massimo di partecipanti è obbligatorio.")
    private Integer maxPartecipanti;

    @Transient
    private boolean canDelete;

    
    @JoinColumn(name = "organizzatore_id", referencedColumnName = "id")
    private Long organizzatoreId;

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizzatoreId() {
        return organizzatoreId;
    }

    public void setOrganizzatoreId(Long organizzatoreId) {
        this.organizzatoreId = organizzatoreId;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public void setMaxPartecipanti(Integer maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

  

    public static Attivita getAttivitaById(Long id2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttivitaById'");
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

}