package com.techchallenge.modules.usuario.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;

    private LocalDateTime ultimaAtualizacao;

    private String endereco;

    @Enumerated(EnumType.STRING)
    private UsuarioRole role;

}
