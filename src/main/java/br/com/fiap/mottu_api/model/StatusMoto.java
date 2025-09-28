package br.com.fiap.mottu_api.model;

public enum StatusMoto {
    DISPONIVEL("Disponível"),
    RESERVADA("Reservada"),
    INDISPONIVEL("Indisponível"),
    MANUTENCAO("Manutenção"),
    FALTA_PECA("Falta Peça"),
    DANOS_ESTRUTURAIS("Danos Estruturais"),
    SINISTRO("Sinistro");

    private final String descricao;

    StatusMoto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    private String definirCorPorStatus(StatusMoto status) {
        return switch (status) {
            case DISPONIVEL -> "Verde";
            case RESERVADA -> "Azul";
            case MANUTENCAO -> "Amarelo";
            case FALTA_PECA -> "Laranja";
            case INDISPONIVEL -> "Cinza";
            case DANOS_ESTRUTURAIS -> "Vermelho";
            case SINISTRO -> "Preto";
        };
    }

    private String definirSetorPorStatus(StatusMoto status) {
        return switch (status) {
            case DISPONIVEL -> "Setor A";
            case RESERVADA -> "Setor B";
            case MANUTENCAO -> "Setor C";
            case FALTA_PECA -> "Setor D";
            case INDISPONIVEL -> "Setor E";
            case DANOS_ESTRUTURAIS -> "Setor F";
            case SINISTRO -> "Setor G";
        };
    }
}