package com.techchallenge.domain.restaurante.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Retorno de dados do restaurante")
public record RestauranteResponseDTO(

        @Schema(description = "ID do restaurante", example = "10")
        Long id,

        @Schema(description = "Nome do restaurante", example = "Cantina da Praça")
        String nome,

        @Schema(description = "Endereço do restaurante", example = "Rua Central, 100 - Recife")
        String endereco,

        @Schema(description = "Tipo de cozinha", example = "Italiana")
        String tipoCozinha,

        @Schema(description = "Horário de funcionamento", example = "Seg-Dom 11:00-23:00")
        String horarioFuncionamento,

        @Schema(description = "ID do dono do restaurante", example = "2")
        Long donoId,

        @Schema(description = "Nome do dono", example = "João Silva")
        String donoNome,

        @Schema(description = "Email do dono", example = "joao@tech.com")
        String donoEmail

) {}
