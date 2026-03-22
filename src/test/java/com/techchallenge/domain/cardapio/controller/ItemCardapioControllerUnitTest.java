package com.techchallenge.domain.cardapio.controller;

import com.techchallenge.domain.cardapio.dto.ItemCardapioCreateDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioResponseDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioUpdateDTO;
import com.techchallenge.domain.cardapio.service.ItemCardapioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemCardapioControllerUnitTest {

    @Mock
    private ItemCardapioService service;

    @InjectMocks
    private ItemCardapioController controller;

    @Test
    void listarTodosRetornaLista() {
        ItemCardapioResponseDTO resposta = new ItemCardapioResponseDTO(
                1L,
                "Lasanha",
                "Lasanha à bolonhesa",
                new BigDecimal("29.90"),
                true,
                "/imagens/lasanha.jpg",
                1L,
                "Cantina"
        );
        when(service.listarTodos()).thenReturn(List.of(resposta));

        ResponseEntity<List<ItemCardapioResponseDTO>> response = controller.listarTodos();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals(resposta, response.getBody().get(0));
        verify(service).listarTodos();
    }

    @Test
    void buscarPorIdRetornaItem() {
        ItemCardapioResponseDTO resposta = new ItemCardapioResponseDTO(
                2L,
                "Baião",
                "Baião de dois",
                new BigDecimal("24.90"),
                false,
                "/imagens/baiao.jpg",
                2L,
                "Sabor"
        );
        when(service.buscarPorId(2L)).thenReturn(resposta);

        ResponseEntity<ItemCardapioResponseDTO> response = controller.buscarPorId(2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Baião de dois", response.getBody().descricao());
        assertEquals(resposta, response.getBody());
        verify(service).buscarPorId(2L);
    }

    @Test
    void criarChamaServico() {
        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Escondidinho",
                "Escondidinho de carne seca",
                new BigDecimal("32.50"),
                true,
                "/imagens/escondidinho.jpg",
                1L
        );
        ItemCardapioResponseDTO resposta = new ItemCardapioResponseDTO(
                3L,
                "Escondidinho",
                "Escondidinho de carne seca",
                new BigDecimal("32.50"),
                true,
                "/imagens/escondidinho.jpg",
                1L,
                "Nordeste"
        );
        when(service.criar(dto)).thenReturn(resposta);

        ResponseEntity<ItemCardapioResponseDTO> response = controller.criar(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(3L, response.getBody().id());
        verify(service).criar(dto);
    }

    @Test
    void atualizarChamaServicoComId() {
        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Lasanha Especial",
                "Lasanha com molho artesanal",
                new BigDecimal("34.90"),
                false,
                "/imagens/lasanha-especial.jpg",
                1L
        );
        ItemCardapioResponseDTO resposta = new ItemCardapioResponseDTO(
                4L,
                "Lasanha Especial",
                "Lasanha com molho artesanal",
                new BigDecimal("34.90"),
                false,
                "/imagens/lasanha-especial.jpg",
                1L,
                "Cantina"
        );
        when(service.atualizar(4L, dto)).thenReturn(resposta);

        ResponseEntity<ItemCardapioResponseDTO> response = controller.atualizar(4L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(resposta, response.getBody());
        verify(service).atualizar(4L, dto);
    }

    @Test
    void deletarChamaServico() {
        doNothing().when(service).deletar(5L);

        ResponseEntity<Void> response = controller.deletar(5L);

        assertEquals(200, response.getStatusCode().value());
        verify(service).deletar(5L);
    }
}
