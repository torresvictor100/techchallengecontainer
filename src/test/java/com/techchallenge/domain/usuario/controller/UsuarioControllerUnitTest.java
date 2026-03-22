package com.techchallenge.domain.usuario.controller;

import com.techchallenge.domain.usuario.dto.UsuarioCreateDTO;
import com.techchallenge.domain.usuario.dto.UsuarioResponseDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateRoleDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateSenhaDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateTipoDTO;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerUnitTest {

    @Mock
    private UsuarioService service;

    @Mock
    private HttpServletRequest request;

    private UsuarioController controller;

    @BeforeEach
    void setup() {
        controller = new UsuarioController(service);
        ReflectionTestUtils.setField(controller, "request", request);
    }

    @Test
    void listarTodosComoAdminRetornaLista() {
        when(request.getAttribute("role")).thenReturn("ADMIN");
        UsuarioResponseDTO usuario = usuarioResponse(1L, "admin@tech.com");
        when(service.listarTodos()).thenReturn(List.of(usuario));

        ResponseEntity<List<UsuarioResponseDTO>> response = controller.listarTodos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(usuario, response.getBody().get(0));
        verify(service).listarTodos();
    }

    @Test
    void listarTodosSemPermissaoLancaSecurityException() {
        when(request.getAttribute("role")).thenReturn("CLIENT");

        assertThrows(SecurityException.class, () -> controller.listarTodos());
        verifyNoInteractions(service);
    }

    @Test
    void buscarPorIdComoAdminIgnoraPermissao() {
        UsuarioResponseDTO usuario = usuarioResponse(2L, "admin@tech.com");
        when(service.buscarPorId(2L)).thenReturn(usuario);
        when(request.isUserInRole("ADMIN")).thenReturn(true);

        ResponseEntity<UsuarioResponseDTO> response = controller.buscarPorId(2L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuario, response.getBody());
        verify(service).buscarPorId(2L);
    }

    @Test
    void buscarPorIdComMesmoEmailCliente() {
        UsuarioResponseDTO usuario = usuarioResponse(3L, "cliente@tech.com");
        when(service.buscarPorId(3L)).thenReturn(usuario);
        when(request.isUserInRole("ADMIN")).thenReturn(false);
        when(request.getAttribute("role")).thenReturn("CLIENT");
        when(request.getAttribute("email")).thenReturn("cliente@tech.com");

        ResponseEntity<UsuarioResponseDTO> response = controller.buscarPorId(3L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuario.email(), response.getBody().email());
        verify(service).buscarPorId(3L);
    }

    @Test
    void buscarPorIdSemPermissaoLancaSecurityException() {
        UsuarioResponseDTO usuario = usuarioResponse(4L, "outro@tech.com");
        when(service.buscarPorId(4L)).thenReturn(usuario);
        when(request.isUserInRole("ADMIN")).thenReturn(false);
        when(request.getAttribute("role")).thenReturn("CLIENT");
        when(request.getAttribute("email")).thenReturn("cliente@tech.com");

        assertThrows(SecurityException.class, () -> controller.buscarPorId(4L));
    }

    @Test
    void criarChamaServico() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Nome", "novo@tech.com", "senha", "Rua X", null);
        UsuarioResponseDTO resposta = usuarioResponse(5L, "novo@tech.com");
        when(service.criar(dto)).thenReturn(resposta);

        ResponseEntity<UsuarioResponseDTO> response = controller.criar(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resposta, response.getBody());
        verify(service).criar(dto);
    }

    @Test
    void atualizarRoleChamaServico() {
        UsuarioUpdateRoleDTO dto = new UsuarioUpdateRoleDTO("6", "ADMIN");
        UsuarioResponseDTO resposta = usuarioResponse(6L, "role@tech.com");
        when(service.atualizarRole(dto)).thenReturn(resposta);

        ResponseEntity<UsuarioResponseDTO> response = controller.atualizarRole(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resposta, response.getBody());
        verify(service).atualizarRole(dto);
    }

    @Test
    void atualizarTipoUsuarioChamaServico() {
        UsuarioUpdateTipoDTO dto = new UsuarioUpdateTipoDTO(7L);
        UsuarioResponseDTO resposta = usuarioResponse(7L, "tipo@tech.com");
        when(service.atualizarTipoUsuario(7L, dto.tipoUsuarioId())).thenReturn(resposta);

        ResponseEntity<UsuarioResponseDTO> response = controller.atualizarTipoUsuario(7L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resposta, response.getBody());
        verify(service).atualizarTipoUsuario(7L, dto.tipoUsuarioId());
    }

    @Test
    void buscarPorNomeEncaminhaParaServico() {
        when(service.buscarPorNome("joao")).thenReturn(List.of(usuarioResponse(8L, "joao@tech.com")));

        ResponseEntity<List<UsuarioResponseDTO>> response = controller.buscarPorNome("joao");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(service).buscarPorNome("joao");
    }

    @Test
    void atualizarUsuarioChamaServicoComPermissao() {
        UsuarioResponseDTO buscado = usuarioResponse(9L, "user@tech.com");
        when(service.buscarPorId(9L)).thenReturn(buscado);
        when(request.getAttribute("role")).thenReturn("ADMIN");
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO("Nome Atualizado", "user@tech.com", "Rua Atualizada");
        UsuarioResponseDTO atualizado = usuarioResponse(9L, "user@tech.com");
        when(service.atualizar(9L, dto)).thenReturn(atualizado);

        ResponseEntity<UsuarioResponseDTO> response = controller.atualizar(9L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(atualizado, response.getBody());
        verify(service).buscarPorId(9L);
        verify(service).atualizar(9L, dto);
    }

    @Test
    void atualizarSenhaChamaServicoComPermissao() {
        UsuarioResponseDTO buscado = usuarioResponse(10L, "senha@tech.com");
        when(service.buscarPorId(10L)).thenReturn(buscado);
        when(request.getAttribute("role")).thenReturn("ADMIN");
        UsuarioUpdateSenhaDTO dto = new UsuarioUpdateSenhaDTO("123456", "Nova@123");

        ResponseEntity<Void> response = controller.atualizarSenha(10L, dto);

        assertEquals(200, response.getStatusCodeValue());
        verify(service).buscarPorId(10L);
        verify(service).atualizarSenha(10L, dto);
    }

    @Test
    void deletarChamaServicoComPermissao() {
        UsuarioResponseDTO buscado = usuarioResponse(11L, "delete@tech.com");
        when(service.buscarPorId(11L)).thenReturn(buscado);
        when(request.getAttribute("role")).thenReturn("ADMIN");

        ResponseEntity<Void> response = controller.deletar(11L);

        assertEquals(200, response.getStatusCodeValue());
        verify(service).buscarPorId(11L);
        verify(service).deletar(11L);
    }

    private UsuarioResponseDTO usuarioResponse(Long id, String email) {
        return new UsuarioResponseDTO(
                id,
                "Nome",
                email,
                "Rua Teste",
                UsuarioRole.CLIENT,
                null,
                LocalDateTime.now()
        );
    }
}
