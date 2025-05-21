
package br.com.fiap.mottu_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

@Entity
public class Moto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(min = 2, max = 100, message = "O Modelo da moto deve ter entre 2 e 100 caracteres")
    private String modelo;

    @NotBlank(message = "Placa é obrigatória")
    @Size(min = 7, max = 7, message = "Placa deve ter exatamente 7 caracteres")
    private String placa;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status é obrigatório")
    private StatusMoto status;

    private String coordenadaGps;

    @NotBlank(message = "Setor é obrigatório")
    private String setor;


    @NotBlank(message = "Cor do setor é obrigatória")
    private String corSetor;


    @ManyToOne
    private Patio patio;


    public Moto(Long id, String modelo, String placa, StatusMoto status, String coordenadaGps, String setor, String corSetor, Patio patio) {
        this.id = id;
        this.modelo = modelo;
        this.placa = placa;
        this.status = status;
        this.coordenadaGps = coordenadaGps;
        this.setor = setor;
        this.corSetor = corSetor;
        this.patio = patio;
    }

    public Moto() {
    }

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

    public String getCoordenadaGps() {
        return coordenadaGps;
    }

    public void setCoordenadaGps(String coordenadaGps) {
        this.coordenadaGps = coordenadaGps;
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

    public Patio getPatio() {
        return patio;
    }

    public void setPatio(Patio patio) {
        this.patio = patio;
    }

    @Override
    public String toString() {
        return "Moto{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ", placa='" + placa + '\'' +
                ", status=" + status +
                ", coordenadaGps='" + coordenadaGps + '\'' +
                ", setor='" + setor + '\'' +
                ", corSetor='" + corSetor + '\'' +
                ", patio=" + patio +
                '}';
    }
}
