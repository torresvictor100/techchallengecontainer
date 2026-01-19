package com.techchallenge.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estrutura padrão de erro retornada pela API")
public record ErrorResponseDTO(

        @Schema(description = "Status da resposta", example = "error")
        int status,

        @Schema(description = "Mensagem detalhada do erro", example = "Email não encontrado")
        String message
) {}
