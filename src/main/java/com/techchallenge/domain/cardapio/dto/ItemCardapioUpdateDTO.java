package com.techchallenge.domain.cardapio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "DTO para atualização de item do cardápio")
public record ItemCardapioUpdateDTO(

        @Schema(description = "Nome do item", example = "Lasanha Especial")
        @NotBlank(message = "Nome é obrigatório")
        @NotNull
        String nome,

        @Schema(description = "Descrição do item", example = "Lasanha com molho artesanal")
        @NotBlank(message = "Descrição é obrigatória")
        @NotNull
        String descricao,

        @Schema(description = "Preço do item", example = "34.90")
        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser positivo")
        BigDecimal preco,

        @Schema(description = "Disponível apenas para consumo no restaurante", example = "false")
        @NotNull(message = "Disponibilidade é obrigatória")
        Boolean somenteNoRestaurante,

        @Schema(description = "Caminho da foto do prato", example = "/imagens/lasanha-especial.jpg")
        @NotBlank(message = "Foto é obrigatória")
        @NotNull
        String fotoPath,

        @Schema(description = "ID do restaurante", example = "1")
        @NotNull(message = "Restaurante é obrigatório")
        Long restauranteId

) {}
