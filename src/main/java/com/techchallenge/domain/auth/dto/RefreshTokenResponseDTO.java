package com.techchallenge.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta contendo novos tokens JWT")
public record RefreshTokenResponseDTO(

        @Schema(description = "Novo token de acesso (JWT)")
        String accessToken,

        @Schema(description = "Novo refresh token caso gere novamente")
        String refreshToken
) {}
