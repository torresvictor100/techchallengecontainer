package com.techchallenge.domain.tipousuario.controller;

import com.techchallenge.domain.tipousuario.dto.TipoUsuarioCreateDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioResponseDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioUpdateDTO;
import com.techchallenge.domain.tipousuario.service.TipoUsuarioService;
import com.techchallenge.domain.usuario.dto.UsuarioResponseDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateTipoEmailDTO;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioControllerUnitTest {

    @Mock
    private TipoUsuarioService tipoUsuarioService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private TipoUsuarioController controller;

    @Test
    void listarTodosRetornaLista() {
        when(tipoUsuarioService.listarTodos())
                .thenReturn(List.of(new TipoUsuarioResponseDTO(1L, "Cliente")));

        ResponseEntity<List<TipoUsuarioResponseDTO>> response = controller.listarTodos();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(tipoUsuarioService).listarTodos();
    }

    @Test
    void buscarPorIdRetornaTipo() {
        when(tipoUsuarioService.buscarPorId(1L))
                .thenReturn(new TipoUsuarioResponseDTO(1L, "Cliente"));

        ResponseEntity<TipoUsuarioResponseDTO> response = controller.buscarPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Cliente", response.getBody().nome());
    }

    @Test
    void buscarUsuariosPorTipoIdRetornaLista() {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO(
                2L,
                "Joao",
                "joao@tech.com",
                "Rua B, 456",
                UsuarioRole.CLIENT,
                new TipoUsuarioResponseDTO(1L, "Cliente"),
                LocalDateTime.now()
        );
        when(usuarioService.buscarPorTipo(1L)).thenReturn(List.of(usuario));

        ResponseEntity<List<UsuarioResponseDTO>> response = controller.buscarUsuariosPorTipoId(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void buscarUsuariosPorTipoNomeRetornaLista() {
        when(usuarioService.buscarPorTipoNome("Cliente"))
                .thenReturn(List.of(new UsuarioResponseDTO(
                        3L,
                        "Maria",
                "maria@tech.com",
                "Rua C, 789",
                UsuarioRole.CLIENT,
                        new TipoUsuarioResponseDTO(1L, "Cliente"),
                LocalDateTime.now()
                )));

        ResponseEntity<List<UsuarioResponseDTO>> response = controller.buscarUsuariosPorTipoNome("Cliente");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void atualizarTipoUsuarioPorEmailRetornaUsuario() {
        UsuarioResponseDTO atualizado = new UsuarioResponseDTO(
                2L,
                "Joao",
                "joao@tech.com",
                "Rua B, 456",
                UsuarioRole.CLIENT,
                new TipoUsuarioResponseDTO(2L, "Dono de Restaurante"),
                LocalDateTime.now()
        );
        when(usuarioService.atualizarTipoUsuarioPorEmail("joao@tech.com", 2L))
                .thenReturn(atualizado);

        var dto = new UsuarioUpdateTipoEmailDTO("joao@tech.com", 2L);
        ResponseEntity<UsuarioResponseDTO> response = controller.atualizarTipoUsuarioPorEmail(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("joao@tech.com", response.getBody().email());
        assertEquals("Dono de Restaurante", response.getBody().tipoUsuario().nome());
    }

    @Test
    void criarRetornaTipoCriado() {
        when(tipoUsuarioService.criar(new TipoUsuarioCreateDTO("NovoTipo")))
                .thenReturn(new TipoUsuarioResponseDTO(10L, "NovoTipo"));

        ResponseEntity<TipoUsuarioResponseDTO> response = controller.criar(new TipoUsuarioCreateDTO("NovoTipo"));

        assertEquals(200, response.getStatusCode().value());
        assertEquals("NovoTipo", response.getBody().nome());
    }

    @Test
    void atualizarRetornaTipoAtualizado() {
        when(tipoUsuarioService.atualizar(1L, new TipoUsuarioUpdateDTO("ClienteAtualizado")))
                .thenReturn(new TipoUsuarioResponseDTO(1L, "ClienteAtualizado"));

        ResponseEntity<TipoUsuarioResponseDTO> response =
                controller.atualizar(1L, new TipoUsuarioUpdateDTO("ClienteAtualizado"));

        assertEquals(200, response.getStatusCode().value());
        assertEquals("ClienteAtualizado", response.getBody().nome());
    }

    @Test
    void deletarRetornaOk() {
        doNothing().when(tipoUsuarioService).deletar(1L);

        ResponseEntity<Void> response = controller.deletar(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(tipoUsuarioService).deletar(1L);
    }
}
