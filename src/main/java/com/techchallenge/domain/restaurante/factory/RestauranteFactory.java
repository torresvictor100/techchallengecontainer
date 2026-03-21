package com.techchallenge.domain.restaurante.factory;

import com.techchallenge.domain.restaurante.dto.RestauranteCreateDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteResponseDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteUpdateDTO;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.usuario.entity.Usuario;

public class RestauranteFactory {

    public static Restaurante fromCreateDTO(RestauranteCreateDTO dto, Usuario dono) {
        return Restaurante.builder()
                .nome(dto.nome())
                .endereco(dto.endereco())
                .tipoCozinha(dto.tipoCozinha())
                .horarioFuncionamento(dto.horarioFuncionamento())
                .dono(dono)
                .build();
    }

    public static void applyUpdate(Restaurante restaurante, RestauranteUpdateDTO dto, Usuario dono) {
        restaurante.setNome(dto.nome());
        restaurante.setEndereco(dto.endereco());
        restaurante.setTipoCozinha(dto.tipoCozinha());
        restaurante.setHorarioFuncionamento(dto.horarioFuncionamento());
        restaurante.setDono(dono);
    }

    public static RestauranteResponseDTO toResponseDTO(Restaurante restaurante) {
        Usuario dono = restaurante.getDono();
        return new RestauranteResponseDTO(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getEndereco(),
                restaurante.getTipoCozinha(),
                restaurante.getHorarioFuncionamento(),
                dono != null ? dono.getId() : null,
                dono != null ? dono.getNome() : null,
                dono != null ? dono.getEmail() : null
        );
    }
}
