package com.techchallenge.modules.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização de usuário")
public record UsuarioUpdateRoleDTO(

        @Schema(description = "O id do usuário", example = "123")
        @NotBlank(message = "id é obrigatório")
        @NotNull
        String idUser,

        @Schema(description = "role válido do usuário", example = "PROPRIETARIO")
        @NotBlank(message = "role é obrigatório")
        @NotNull
        String role

) {}
