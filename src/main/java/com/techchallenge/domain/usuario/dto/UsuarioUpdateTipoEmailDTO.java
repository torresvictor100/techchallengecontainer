package com.techchallenge.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização do tipo de usuário por email")
public record UsuarioUpdateTipoEmailDTO(

        @Schema(description = "Email do usuário", example = "joao@tech.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Schema(description = "ID do tipo de usuário", example = "1")
        @NotNull(message = "tipoUsuarioId é obrigatório")
        Long tipoUsuarioId
) {}
