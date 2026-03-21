package com.techchallenge.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização do tipo de usuário")
public record UsuarioUpdateTipoDTO(

        @Schema(description = "ID do tipo de usuário", example = "1")
        @NotNull(message = "tipoUsuarioId é obrigatório")
        Long tipoUsuarioId

) {}
