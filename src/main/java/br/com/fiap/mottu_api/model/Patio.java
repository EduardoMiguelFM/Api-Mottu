package br.com.fiap.mottu_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.util.List;

@Entity

public class Patio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @OneToMany(mappedBy = "patio")
    @JsonIgnore
    private List<Moto> motos;

    public Patio(Long id, String nome, List<Moto> motos) {
        this.id = id;
        this.nome = nome;
        this.motos = motos;
    }

    public Patio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Moto> getMotos() {
        return motos;
    }

    public void setMotos(List<Moto> motos) {
        this.motos = motos;
    }

    @Override
    public String toString() {
        return "Patio{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", motos=" + motos +
                '}';
    }
}