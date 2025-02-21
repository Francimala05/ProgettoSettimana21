package com.epicode.Progetto21.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"event\"")
@Getter
@Setter
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private String descrizione;
    private LocalDateTime data;
    private String luogo;
    private int postiDisponibili;
    private String nomeOrganizzatore;
    private int postiPrenotati;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public String getNomeOrganizzatore() {
        return nomeOrganizzatore;
    }

    public void setNomeOrganizzatore(String nomeOrganizzatore) {
        this.nomeOrganizzatore = nomeOrganizzatore;
    }

    public int getPostiPrenotati() {
        return postiPrenotati;
    }

    public void setPostiPrenotati(int postiPrenotati) {
        this.postiPrenotati = postiPrenotati;
    }

    public void prenotaPosto() {
        if (postiDisponibili > postiPrenotati) {
            postiPrenotati++;
        } else {
            throw new IllegalArgumentException("Non ci sono posti disponibili per questo evento.");
        }
    }
}
