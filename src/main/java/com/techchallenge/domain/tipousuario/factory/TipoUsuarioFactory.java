package com.techchallenge.domain.tipousuario.factory;

import com.techchallenge.domain.tipousuario.dto.TipoUsuarioCreateDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioResponseDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioUpdateDTO;
import com.techchallenge.domain.tipousuario.entity.TipoUsuario;

public class TipoUsuarioFactory {

    public static TipoUsuario fromCreateDTO(TipoUsuarioCreateDTO dto) {
        return TipoUsuario.builder()
                .nome(dto.nome())
                .build();
    }

    public static void applyUpdate(TipoUsuario tipoUsuario, TipoUsuarioUpdateDTO dto) {
        tipoUsuario.setNome(dto.nome());
    }

    public static TipoUsuarioResponseDTO toResponseDTO(TipoUsuario tipoUsuario) {
        return new TipoUsuarioResponseDTO(tipoUsuario.getId(), tipoUsuario.getNome());
    }
}
