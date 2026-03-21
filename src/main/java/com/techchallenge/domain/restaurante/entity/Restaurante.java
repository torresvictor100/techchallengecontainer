package com.techchallenge.domain.restaurante.entity;

import com.techchallenge.domain.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurante")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String tipoCozinha;

    @Column(nullable = false)
    private String horarioFuncionamento;

    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    private Usuario dono;
}
