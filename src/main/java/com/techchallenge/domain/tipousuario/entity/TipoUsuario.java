package com.techchallenge.domain.tipousuario.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_usuario")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;
}
