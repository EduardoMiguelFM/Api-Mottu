package br.com.fiap.mottu_api.model;

public enum StatusMoto {
    DISPONIVEL,
    RESERVADA,
    INDISPONIVEL,
    MANUTENCAO,
    FALTA_PECA,
    DANOS_ESTRUTURAIS,
    SINISTRO;

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
}