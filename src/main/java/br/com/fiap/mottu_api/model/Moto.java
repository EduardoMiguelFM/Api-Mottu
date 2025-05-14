package br.com.fiap.mottu_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Moto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String modelo;

    @NotBlank
    private String placa;

    @Enumerated(EnumType.STRING)
    private StatusMoto status;

    @ManyToOne
    private Patio patio;
}