package br.com.fiap.mottu_api.dto;

import br.com.fiap.mottu_api.model.StatusMoto;

public class MotoResponseDTO {
    private Long id;
    private String modelo;
    private String placa;
    private StatusMoto status;
    private String setor;
    private String corSetor;
    private String nomePatio;

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public StatusMoto getStatus() { return status; }
    public void setStatus(StatusMoto status) { this.status = status; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getCorSetor() { return corSetor; }
    public void setCorSetor(String corSetor) { this.corSetor = corSetor; }

    public String getNomePatio() { return nomePatio; }
    public void setNomePatio(String nomePatio) { this.nomePatio = nomePatio; }
}