package com.techchallenge.domain.cardapio.service;

import com.techchallenge.domain.cardapio.dto.ItemCardapioCreateDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioUpdateDTO;
import com.techchallenge.domain.cardapio.entity.ItemCardapio;
import com.techchallenge.domain.cardapio.repository.ItemCardapioRepository;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.restaurante.repository.RestauranteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemCardapioServiceUnitTest {

    @Mock
    private ItemCardapioRepository itemRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private ItemCardapioService itemService;

    private Restaurante novoRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Cantina");
        return restaurante;
    }

    @Test
    void criarItemComRestauranteExistente() {
        Restaurante restaurante = novoRestaurante();
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        when(itemRepository.save(any(ItemCardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Lasanha",
                "Desc",
                new BigDecimal("25.90"),
                true,
                "/imagens/lasanha.jpg",
                1L);

        var response = itemService.criar(dto);

        assertEquals("Lasanha", response.nome());
        verify(itemRepository).save(any());
    }

    @Test
    void criarItemSemRestauranteLancaErro() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> itemService.criar(new ItemCardapioCreateDTO(
                        "Nome",
                        "Desc",
                        new BigDecimal("10.00"),
                        true,
                        "/img.jpg",
                        1L)));
    }

    @Test
    void atualizarItemComSucesso() {
        ItemCardapio item = new ItemCardapio();
        item.setId(1L);
        item.setRestaurante(novoRestaurante());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(novoRestaurante()));
        when(itemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Nova",
                "Desc",
                new BigDecimal("30.00"),
                false,
                "/img.jpg",
                1L);

        var response = itemService.atualizar(1L, dto);

        assertEquals("Nova", response.nome());
    }

    @Test
    void atualizarItemInexistenteLancaErro() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> itemService.atualizar(1L, new ItemCardapioUpdateDTO(
                        "Nome",
                        "Desc",
                        new BigDecimal("15.00"),
                        true,
                        "/img.jpg",
                        1L)));
    }

    @Test
    void atualizarComRestauranteInexistenteLancaErro() {
        ItemCardapio item = new ItemCardapio();
        item.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> itemService.atualizar(1L, new ItemCardapioUpdateDTO(
                        "Nome",
                        "Desc",
                        new BigDecimal("15.00"),
                        true,
                        "/img.jpg",
                        1L)));
    }

    @Test
    void deletarItemExistente() {
        when(itemRepository.existsById(1L)).thenReturn(true);

        itemService.deletar(1L);

        verify(itemRepository).deleteById(1L);
    }

    @Test
    void deletarItemInexistenteLancaErro() {
        when(itemRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> itemService.deletar(1L));
    }
}
