package com.techchallenge.domain.tipousuario.service;

import com.techchallenge.domain.tipousuario.dto.TipoUsuarioCreateDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioUpdateDTO;
import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioServiceUnitTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TipoUsuarioService service;

    @Test
    void criarTipoDuplicadoDisparaExcecao() {
        when(tipoUsuarioRepository.existsByNomeIgnoreCase("Cliente")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.criar(new TipoUsuarioCreateDTO("Cliente")));
    }

    @Test
    void criarTipoSalvaComSucesso() {
        when(tipoUsuarioRepository.existsByNomeIgnoreCase("NovoTipo")).thenReturn(false);

        TipoUsuario salvo = new TipoUsuario();
        salvo.setId(1L);
        salvo.setNome("NovoTipo");
        when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(salvo);

        var response = service.criar(new TipoUsuarioCreateDTO("NovoTipo"));

        assertEquals(1L, response.id());
        assertEquals("NovoTipo", response.nome());
    }

    @Test
    void buscarPorIdInexistenteDisparaExcecao() {
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(1L));
    }

    @Test
    void buscarPorIdComSucesso() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(2L);
        tipo.setNome("Dono de Restaurante");

        when(tipoUsuarioRepository.findById(2L)).thenReturn(Optional.of(tipo));

        var response = service.buscarPorId(2L);

        assertEquals(2L, response.id());
        assertEquals("Dono de Restaurante", response.nome());
    }

    @Test
    void atualizarComNomeDuplicadoDisparaExcecao() {
        TipoUsuario existente = new TipoUsuario();
        existente.setId(1L);
        existente.setNome("Tipo A");

        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoUsuarioRepository.existsByNomeIgnoreCase("Tipo B")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.atualizar(1L, new TipoUsuarioUpdateDTO("Tipo B")));
    }

    @Test
    void atualizarComSucesso() {
        TipoUsuario existente = new TipoUsuario();
        existente.setId(1L);
        existente.setNome("Tipo Antigo");

        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoUsuarioRepository.existsByNomeIgnoreCase("Tipo Novo")).thenReturn(false);

        TipoUsuario salvo = new TipoUsuario();
        salvo.setId(1L);
        salvo.setNome("Tipo Novo");
        when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(salvo);

        var response = service.atualizar(1L, new TipoUsuarioUpdateDTO("Tipo Novo"));

        assertEquals(1L, response.id());
        assertEquals("Tipo Novo", response.nome());
    }

    @Test
    void deletarTipoAssociadoDisparaExcecao() {
        when(tipoUsuarioRepository.existsById(1L)).thenReturn(true);
        when(usuarioRepository.existsByTipoUsuarioId(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.deletar(1L));
    }

    @Test
    void deletarTipoInexistenteDisparaExcecao() {
        when(tipoUsuarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.deletar(1L));
    }

    @Test
    void listarTodosRetornaLista() {
        TipoUsuario tipo1 = new TipoUsuario();
        tipo1.setId(1L);
        tipo1.setNome("Cliente");

        TipoUsuario tipo2 = new TipoUsuario();
        tipo2.setId(2L);
        tipo2.setNome("Dono de Restaurante");

        when(tipoUsuarioRepository.findAll()).thenReturn(List.of(tipo1, tipo2));

        var lista = service.listarTodos();

        assertEquals(2, lista.size());
        assertEquals("Cliente", lista.get(0).nome());
        assertEquals("Dono de Restaurante", lista.get(1).nome());
    }
}
