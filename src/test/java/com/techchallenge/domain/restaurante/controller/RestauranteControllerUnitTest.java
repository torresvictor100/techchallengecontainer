package com.techchallenge.domain.restaurante.controller;

import com.techchallenge.domain.restaurante.dto.RestauranteCreateDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteResponseDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteUpdateDTO;
import com.techchallenge.domain.restaurante.service.RestauranteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteControllerUnitTest {

    @Mock
    private RestauranteService service;

    @InjectMocks
    private RestauranteController controller;

    @Test
    void listarTodosRetornaLista() {
        RestauranteResponseDTO resposta = new RestauranteResponseDTO(
                1L,
                "Cantina da Praca",
                "Rua Central, 100",
                "Italiana",
                "Seg-Dom 11:00-23:00",
                2L,
                "Joao",
                "joao@tech.com"
        );
        when(service.listarTodos()).thenReturn(List.of(resposta));

        ResponseEntity<List<RestauranteResponseDTO>> response = controller.listarTodos();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals(resposta, response.getBody().get(0));
        verify(service).listarTodos();
    }

    @Test
    void buscarPorIdRetornaRestaurante() {
        RestauranteResponseDTO resposta = new RestauranteResponseDTO(
                5L,
                "Cantina do Chef",
                "Av. Central, 500",
                "Brasileira",
                "Seg-Sex 10:00-22:00",
                4L,
                "Maria",
                "maria@tech.com"
        );
        when(service.buscarPorId(5L)).thenReturn(resposta);

        ResponseEntity<RestauranteResponseDTO> response = controller.buscarPorId(5L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(resposta, response.getBody());
        verify(service).buscarPorId(5L);
    }

    @Test
    void criarChamaServico() {
        RestauranteCreateDTO dto = new RestauranteCreateDTO(
                "Cantina Nova",
                "Rua Nova, 12",
                "Nordestina",
                "Seg-Dom 12:00-22:00",
                3L
        );
        RestauranteResponseDTO resposta = new RestauranteResponseDTO(
                6L,
                "Cantina Nova",
                "Rua Nova, 12",
                "Nordestina",
                "Seg-Dom 12:00-22:00",
                3L,
                "Carlos",
                "carlos@tech.com"
        );
        when(service.criar(dto)).thenReturn(resposta);

        ResponseEntity<RestauranteResponseDTO> response = controller.criar(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(6L, response.getBody().id());
        verify(service).criar(dto);
    }

    @Test
    void atualizarChamaServicoComId() {
        RestauranteUpdateDTO dto = new RestauranteUpdateDTO(
                "Cantina Atualizada",
                "Av. Nova, 200",
                "Brasileira",
                "Seg-Sex 10:00-22:00",
                4L
        );
        RestauranteResponseDTO resposta = new RestauranteResponseDTO(
                7L,
                "Cantina Atualizada",
                "Av. Nova, 200",
                "Brasileira",
                "Seg-Sex 10:00-22:00",
                4L,
                "Lucia",
                "lucia@tech.com"
        );
        when(service.atualizar(7L, dto)).thenReturn(resposta);

        ResponseEntity<RestauranteResponseDTO> response = controller.atualizar(7L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(resposta, response.getBody());
        verify(service).atualizar(7L, dto);
    }

    @Test
    void deletarChamaServico() {
        doNothing().when(service).deletar(8L);

        ResponseEntity<Void> response = controller.deletar(8L);

        assertEquals(200, response.getStatusCode().value());
        verify(service).deletar(8L);
    }
}
