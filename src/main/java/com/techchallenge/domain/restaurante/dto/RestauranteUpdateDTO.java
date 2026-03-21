package com.techchallenge.domain.restaurante.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização de restaurante")
public record RestauranteUpdateDTO(

        @Schema(description = "Nome do restaurante", example = "Cantina Atualizada")
        @NotBlank(message = "Nome é obrigatório")
        @NotNull
        String nome,

        @Schema(description = "Endereço do restaurante", example = "Av. Nova, 200 - Recife")
        @NotBlank(message = "Endereço é obrigatório")
        @NotNull
        String endereco,

        @Schema(description = "Tipo de cozinha", example = "Brasileira")
        @NotBlank(message = "Tipo de cozinha é obrigatório")
        @NotNull
        String tipoCozinha,

        @Schema(description = "Horário de funcionamento", example = "Seg-Sex 10:00-22:00")
        @NotBlank(message = "Horário de funcionamento é obrigatório")
        @NotNull
        String horarioFuncionamento,

        @Schema(description = "ID do dono do restaurante", example = "2")
        @NotNull(message = "Dono do restaurante é obrigatório")
        Long donoId

) {}
