package com.techchallenge.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para renovar o token JWT")
public record RefreshTokenRequestDTO(

        @Schema(description = "Token atual a ser renovado")
        String refreshToken
) {}
