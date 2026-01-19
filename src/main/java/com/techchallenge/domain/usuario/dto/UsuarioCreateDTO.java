package com.techchallenge.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

@Schema(description = "DTO para criação de usuário contendo os dados necessários")
public record UsuarioCreateDTO(

        @Schema(description = "Nome completo do usuário", example = "João Victor Torres")
        @NotBlank(message = "Nome é obrigatório")
        @NotNull
        String nome,

        @Schema(description = "Email válido e único do usuário", example = "joao.victor@tech.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @NotNull
        String email,

        @Schema(description = "Senha do usuário", example = "123456")
        @NotBlank(message = "Senha é obrigatória")
        @NotNull
        String senha,

        @Schema(description = "Endereço residencial do usuário", example = "Rua Centro, 123 - Recife, PE")
        @NotBlank(message = "Endereço é obrigatório")
        @NotNull
        String endereco
) {}
