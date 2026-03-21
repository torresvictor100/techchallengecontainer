package com.techchallenge.domain.cardapio.entity;

import com.techchallenge.domain.restaurante.entity.Restaurante;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_cardapio")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean somenteNoRestaurante;

    @Column(nullable = false)
    private String fotoPath;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
}
