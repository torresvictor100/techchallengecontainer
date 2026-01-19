package com.techchallenge.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados enviados pelo usuário para realizar login")
public record LoginRequestDTO(

        @Schema(description = "E-mail do usuário", example = "admin@tech.com")
        String email,

        @Schema(description = "Senha do usuário", example = "123456")
        String password
) {}
