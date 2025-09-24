
package br.com.fiap.mottu_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "moto")
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

    @NotBlank(message = "Setor é obrigatório")
    private String setor;

    @NotBlank(message = "Cor do setor é obrigatória")
    @Column(name = "cor_setor")
    private String corSetor;

    @ManyToOne
    @JoinColumn(name = "patio_id")
    private Patio patio;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();

    public Moto(Long id, String modelo, String placa, StatusMoto status, String setor, String corSetor, Patio patio) {
        this.id = id;
        this.modelo = modelo;
        this.placa = placa;
        this.status = status;
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

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = java.time.LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Moto{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ", placa='" + placa + '\'' +
                ", status=" + status +
                ", setor='" + setor + '\'' +
                ", corSetor='" + corSetor + '\'' +
                ", patio=" + patio +
                '}';
    }
}
