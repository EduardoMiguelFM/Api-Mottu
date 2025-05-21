package br.com.fiap.mottu_api.dto;

public class UsuarioPatioDTO {
    private String nome;
    private String cpf;
    private String funcao;
    private String email;
    private String senha;

    public UsuarioPatioDTO() {
    }

    public UsuarioPatioDTO(String nome, String cpf, String funcao, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.funcao = funcao;
        this.email = email;
        this.senha = senha;
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
