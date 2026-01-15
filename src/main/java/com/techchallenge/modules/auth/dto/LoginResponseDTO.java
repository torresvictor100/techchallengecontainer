package com.techchallenge.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta contendo o token JWT e informações do login")
public record LoginResponseDTO(

        @Schema(description = "Status da operação", example = "ok")
        String status,

        @Schema(description = "Mensagem de retorno", example = "logged")
        String message,

        @Schema(description = "Token JWT gerado")
        String token
) {}
