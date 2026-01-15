package com.techchallenge.modules.usuario.factory;

import com.techchallenge.modules.usuario.dto.UsuarioCreateDTO;
import com.techchallenge.modules.usuario.dto.UsuarioResponseDTO;
import com.techchallenge.modules.usuario.dto.UsuarioUpdateDTO;
import com.techchallenge.modules.usuario.entity.Usuario;
import com.techchallenge.modules.usuario.entity.UsuarioRole;

import java.time.LocalDateTime;

public class UsuarioFactory {
    public static Usuario fromCreateDTO(UsuarioCreateDTO dto) {
        return Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(dto.senha())
                .endereco(dto.endereco())
                .role(UsuarioRole.CLIENT)
                .ultimaAtualizacao(LocalDateTime.now())
                .build();
    }

    public static void applyUpdate(Usuario usuario, UsuarioUpdateDTO dto) {
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setEndereco(dto.endereco());
        usuario.setUltimaAtualizacao(LocalDateTime.now());
    }


    public static void applyUpdateUserRole(Usuario usuario, UsuarioRole role) {
        usuario.setRole(role);
    }

    public static void applySenhaUpdate(Usuario usuario, String novaSenha) {
        usuario.setSenha(novaSenha);
        usuario.setUltimaAtualizacao(LocalDateTime.now());
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEndereco(),
                usuario.getRole(),
                usuario.getUltimaAtualizacao()
        );
    }
}
