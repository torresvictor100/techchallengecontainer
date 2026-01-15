package com.techchallenge.modules.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para alteração de senha do usuário")
public record UsuarioUpdateSenhaDTO(

        @Schema(description = "Senha atual do usuário", example = "123456")
        @NotBlank(message = "Senha atual é obrigatória")
        @NotNull
        String senhaAtual,

        @Schema(description = "Nova senha do usuário", example = "novaSenha123")
        @NotBlank(message = "Nova senha é obrigatória")
        @NotNull
        String novaSenha
) {}
