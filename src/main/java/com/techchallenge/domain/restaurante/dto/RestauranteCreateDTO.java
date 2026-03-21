package com.techchallenge.domain.restaurante.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação de restaurante")
public record RestauranteCreateDTO(

        @Schema(description = "Nome do restaurante", example = "Cantina da Praça")
        @NotBlank(message = "Nome é obrigatório")
        @NotNull
        String nome,

        @Schema(description = "Endereço do restaurante", example = "Rua Central, 100 - Recife")
        @NotBlank(message = "Endereço é obrigatório")
        @NotNull
        String endereco,

        @Schema(description = "Tipo de cozinha", example = "Italiana")
        @NotBlank(message = "Tipo de cozinha é obrigatório")
        @NotNull
        String tipoCozinha,

        @Schema(description = "Horário de funcionamento", example = "Seg-Dom 11:00-23:00")
        @NotBlank(message = "Horário de funcionamento é obrigatório")
        @NotNull
        String horarioFuncionamento,

        @Schema(description = "ID do dono do restaurante", example = "2")
        @NotNull(message = "Dono do restaurante é obrigatório")
        Long donoId

) {}
