package br.com.fiap.mottu_api.model;

public enum Role {
    ADMIN("Administrador"),
    SUPERVISOR("Supervisor"),
    USER("Usuário");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
