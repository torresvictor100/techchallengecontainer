package com.techchallenge.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações do usuário autenticado extraídas do token JWT")
public record UserInfoDTO(

        @Schema(description = "Email do usuário autenticado", example = "usuario@tech.com")
        String email,

        @Schema(description = "Quando o token foi emitido", example = "2025-11-19T10:00:00")
        String issuedAt,

        @Schema(description = "Quando o token expira", example = "2025-11-19T12:00:00")
        String expiresAt,

        @Schema(description = "Papel (Role) do usuário no sistema", example = "ADMIN")
        String role,

        @Schema(description = "Id do usuario", example = "1")
        Long idUser,

        @Schema(description = "Nome do usuario", example = "nome")
        String nome,

        @Schema(description = "Endereco do usuario", example = "nome")
        String endereco
) {}
