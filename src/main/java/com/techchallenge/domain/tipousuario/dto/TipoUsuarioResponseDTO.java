package com.techchallenge.domain.tipousuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Retorno de tipo de usuário")
public record TipoUsuarioResponseDTO(

        @Schema(description = "ID do tipo", example = "1")
        Long id,

        @Schema(description = "Nome do tipo", example = "Cliente")
        String nome

) {}
