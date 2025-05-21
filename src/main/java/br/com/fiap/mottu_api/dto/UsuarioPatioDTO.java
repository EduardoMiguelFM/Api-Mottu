package br.com.fiap.mottu_api.dto;

public class UsuarioPatioDTO {

    private Long id;
    private String nome;
    private String email;
    private String funcao;

    public UsuarioPatioDTO() {}

    public UsuarioPatioDTO(Long id, String nome, String email, String funcao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.funcao = funcao;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @Override
    public String toString() {
        return "UsuarioPatioDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", funcao='" + funcao + '\'' +
                '}';
    }
}