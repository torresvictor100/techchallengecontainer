package com.techchallenge.domain.cardapio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Retorno de item do cardápio")
public record ItemCardapioResponseDTO(

        @Schema(description = "ID do item", example = "100")
        Long id,

        @Schema(description = "Nome do item", example = "Lasanha")
        String nome,

        @Schema(description = "Descrição do item")
        String descricao,

        @Schema(description = "Preço do item")
        BigDecimal preco,

        @Schema(description = "Disponível apenas para consumo no restaurante")
        Boolean somenteNoRestaurante,

        @Schema(description = "Caminho da foto do prato")
        String fotoPath,

        @Schema(description = "ID do restaurante")
        Long restauranteId,

        @Schema(description = "Nome do restaurante")
        String restauranteNome

) {}
