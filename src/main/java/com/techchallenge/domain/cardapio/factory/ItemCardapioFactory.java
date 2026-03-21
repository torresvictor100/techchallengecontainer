package com.techchallenge.domain.cardapio.factory;

import com.techchallenge.domain.cardapio.dto.ItemCardapioCreateDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioResponseDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioUpdateDTO;
import com.techchallenge.domain.cardapio.entity.ItemCardapio;
import com.techchallenge.domain.restaurante.entity.Restaurante;

public class ItemCardapioFactory {

    public static ItemCardapio fromCreateDTO(ItemCardapioCreateDTO dto, Restaurante restaurante) {
        return ItemCardapio.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .preco(dto.preco())
                .somenteNoRestaurante(dto.somenteNoRestaurante())
                .fotoPath(dto.fotoPath())
                .restaurante(restaurante)
                .build();
    }

    public static void applyUpdate(ItemCardapio item, ItemCardapioUpdateDTO dto, Restaurante restaurante) {
        item.setNome(dto.nome());
        item.setDescricao(dto.descricao());
        item.setPreco(dto.preco());
        item.setSomenteNoRestaurante(dto.somenteNoRestaurante());
        item.setFotoPath(dto.fotoPath());
        item.setRestaurante(restaurante);
    }

    public static ItemCardapioResponseDTO toResponseDTO(ItemCardapio item) {
        Restaurante restaurante = item.getRestaurante();
        return new ItemCardapioResponseDTO(
                item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getPreco(),
                item.getSomenteNoRestaurante(),
                item.getFotoPath(),
                restaurante != null ? restaurante.getId() : null,
                restaurante != null ? restaurante.getNome() : null
        );
    }
}
