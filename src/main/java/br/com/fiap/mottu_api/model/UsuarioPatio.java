package br.com.fiap.mottu_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


@Entity
public class UsuarioPatio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String funcao;

    @NotBlank
    private String email;
    @NotBlank
    private String senha;


    public UsuarioPatio() {
    }

    public UsuarioPatio(Long id, String nome, String cpf, String funcao, String email, String senha, List<Moto> motos) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.funcao = funcao;
        this.email = email;
        this.senha = senha;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}

