package com.techchallenge.domain.tipousuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação de tipo de usuário")
public record TipoUsuarioCreateDTO(

        @Schema(description = "Nome do tipo de usuário", example = "Cliente")
        @NotBlank(message = "Nome é obrigatório")
        @NotNull
        String nome

) {}
