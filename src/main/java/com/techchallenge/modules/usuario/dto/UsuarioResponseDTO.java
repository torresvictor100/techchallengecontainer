package com.techchallenge.modules.usuario.dto;

import com.techchallenge.modules.usuario.entity.UsuarioRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Retorno de informações de usuário")
public record UsuarioResponseDTO(

        @Schema(description = "ID do usuário", example = "1")
        Long id,

        @Schema(description = "Nome completo do usuário", example = "João Victor Torres")
        String nome,

        @Schema(description = "Email do usuário", example = "joao.victor@tech.com")
        String email,

        @Schema(description = "Endereço do usuário", example = "Rua Centro, 123 - Recife, PE")
        String endereco,

        @Schema(description = "Perfil do usuário", example = "CLIENT")
        UsuarioRole role,

        @Schema(description = "Data da última atualização")
        LocalDateTime ultimaAtualizacao
) {}