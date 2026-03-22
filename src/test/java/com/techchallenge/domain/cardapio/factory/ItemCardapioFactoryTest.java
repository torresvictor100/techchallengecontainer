package com.techchallenge.domain.cardapio.factory;

import com.techchallenge.domain.cardapio.dto.ItemCardapioCreateDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioResponseDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioUpdateDTO;
import com.techchallenge.domain.cardapio.entity.ItemCardapio;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemCardapioFactoryTest {

    @Test
    void fromCreateDTOPreencheCampos() {
        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Lasanha",
                "Lasanha com molho",
                new BigDecimal("28.50"),
                true,
                "/imagens/lasanha.jpg",
                1L
        );
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Cantina");

        ItemCardapio item = ItemCardapioFactory.fromCreateDTO(dto, restaurante);

        assertNull(item.getId());
        assertEquals(dto.nome(), item.getNome());
        assertEquals(dto.descricao(), item.getDescricao());
        assertEquals(dto.preco(), item.getPreco());
        assertTrue(item.getSomenteNoRestaurante());
        assertEquals(dto.fotoPath(), item.getFotoPath());
        assertSame(restaurante, item.getRestaurante());
    }

    @Test
    void applyUpdateAtualizaCamposQuandoRestauranteMudou() {
        ItemCardapio item = new ItemCardapio();
        item.setId(10L);
        item.setNome("Antiga");

        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Lasanha Premium",
                "Molho artesanal",
                new BigDecimal("35.00"),
                false,
                "/imagens/lasanha-premium.jpg",
                2L
        );
        Restaurante restaurante = new Restaurante();
        restaurante.setId(2L);
        restaurante.setNome("Cantina Premium");

        ItemCardapioFactory.applyUpdate(item, dto, restaurante);

        assertEquals(10L, item.getId());
        assertEquals(dto.nome(), item.getNome());
        assertEquals(dto.descricao(), item.getDescricao());
        assertEquals(dto.preco(), item.getPreco());
        assertEquals(dto.somenteNoRestaurante(), item.getSomenteNoRestaurante());
        assertEquals(dto.fotoPath(), item.getFotoPath());
        assertSame(restaurante, item.getRestaurante());
    }

    @Test
    void toResponseDTOMapeiaRestauranteQuandoPresente() {
        ItemCardapio item = new ItemCardapio();
        item.setId(20L);
        item.setNome("Escondidinho");
        item.setDescricao("Escondidinho com carne seca");
        item.setPreco(new BigDecimal("31.80"));
        item.setSomenteNoRestaurante(false);
        item.setFotoPath("/imagens/escondidinho.jpg");
        Restaurante restaurante = new Restaurante();
        restaurante.setId(3L);
        restaurante.setNome("Sabor Nordestino");
        item.setRestaurante(restaurante);

        ItemCardapioResponseDTO dto = ItemCardapioFactory.toResponseDTO(item);

        assertEquals(20L, dto.id());
        assertEquals("Escondidinho", dto.nome());
        assertEquals(new BigDecimal("31.80"), dto.preco());
        assertEquals(3L, dto.restauranteId());
        assertEquals("Sabor Nordestino", dto.restauranteNome());
    }

    @Test
    void toResponseDTOComRestauranteNullMantemNullNosCampos() {
        ItemCardapio item = new ItemCardapio();
        item.setId(30L);
        item.setNome("Sopa");
        item.setDescricao("Sopa da casa");
        item.setPreco(new BigDecimal("18.20"));
        item.setSomenteNoRestaurante(true);
        item.setFotoPath("/imagens/sopa.jpg");

        ItemCardapioResponseDTO dto = ItemCardapioFactory.toResponseDTO(item);

        assertEquals(30L, dto.id());
        assertNull(dto.restauranteId());
        assertNull(dto.restauranteNome());
    }

}
