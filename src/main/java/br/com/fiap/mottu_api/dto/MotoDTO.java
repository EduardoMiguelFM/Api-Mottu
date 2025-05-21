package br.com.fiap.mottu_api.dto;

import br.com.fiap.mottu_api.model.StatusMoto;



public class MotoDTO {
    private Long id;
    private String modelo;
    private String placa;
    private StatusMoto status;
    private String setor;
    private String corSetor;
    private String nomePatio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public StatusMoto getStatus() {
        return status;
    }

    public void setStatus(StatusMoto status) {
        this.status = status;
    }


    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getCorSetor() {
        return corSetor;
    }

    public void setCorSetor(String corSetor) {
        this.corSetor = corSetor;
    }

    public String getNomePatio() {
        return nomePatio;
    }

    public void setNomePatio(String nomePatio) {
        this.nomePatio = nomePatio;
    }

    @Override
    public String toString() {
        return "MotoDTO{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ", placa='" + placa + '\'' +
                ", status=" + status +
                ", setor='" + setor + '\'' +
                ", corSetor='" + corSetor + '\'' +
                ", patioId=" + nomePatio +
                '}';
    }
}