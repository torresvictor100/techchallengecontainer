package com.techchallenge.domain.tipousuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização de tipo de usuário")
public record TipoUsuarioUpdateDTO(

        @Schema(description = "Nome do tipo de usuário", example = "Dono de Restaurante")
        @NotBlank(message = "Nome é obrigatório")
        @NotNull
        String nome

) {}
