package br.com.fiap.mottu_api.dto;

import br.com.fiap.mottu_api.model.StatusMoto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MotoDTO {
    private Long id;
    private String modelo;
    private String placa;
    private StatusMoto status;
    private String coordenadaGps;
    private LocalDate dataUltimaManutencao;
    private String descricaoProblema;
    private Long patioId;
}