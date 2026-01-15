package com.techchallenge.modules.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização de usuário")
public record UsuarioUpdateDTO(

        @Schema(description = "Nome completo do usuário", example = "João Victor Torres")
        @NotBlank(message = "Nome é obrigatório")
        @NotNull
        String nome,

        @Schema(description = "Email válido do usuário", example = "joao.victor@tech.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @NotNull
        String email,

        @Schema(description = "Endereço do usuário", example = "Rua Centro, 123 - Recife, PE")
        @NotBlank(message = "Endereço é obrigatório")
        @NotNull
        String endereco
) {}
