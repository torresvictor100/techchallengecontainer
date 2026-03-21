package com.techchallenge.domain.usuario.service;

import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.dto.UsuarioCreateDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateSenhaDTO;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceUnitTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void criarComEmailExistenteLancaErro() {
        when(usuarioRepository.existsByEmail("dup@tech.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                usuarioService.criar(new UsuarioCreateDTO("Dup", "dup@tech.com", "123456", "Rua A, 1", null)));
    }

    @Test
    void criarSemTipoAssumeCliente() {
        when(usuarioRepository.existsByEmail("novo@tech.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hash");
        when(tipoUsuarioRepository.findByNomeIgnoreCase("Cliente"))
                .thenReturn(Optional.of(new TipoUsuario(1L, "Cliente")));

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = usuarioService.criar(new UsuarioCreateDTO("Novo", "novo@tech.com", "123456", "Rua B", null));

        assertEquals("Novo", response.nome());
        verify(usuarioRepository).save(any());
    }

    @Test
    void buscarPorEmailNaoEncontradoLancaErro() {
        when(usuarioRepository.findByEmail("missing@tech.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.buscarPorEmail("missing@tech.com"));
    }

    @Test
    void buscarPorNomeComParametroVazioLancaErro() {
        assertThrows(IllegalArgumentException.class, () -> usuarioService.buscarPorNome(" "));
    }

    @Test
    void atualizarSenhaComSenhaAtualIncorretaLancaErro() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setSenha("hash");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "hash")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.atualizarSenha(1L, new UsuarioUpdateSenhaDTO("errada", "nova")));
    }

    @Test
    void deletarInexistenteLancaErro() {
        when(usuarioRepository.existsById(10L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> usuarioService.deletar(10L));
    }

    @Test
    void atualizarTipoUsuarioAlteraTipo() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("temp@tech.com");

        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(2L);
        tipo.setNome("Dono de Restaurante");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(tipoUsuarioRepository.findById(2L)).thenReturn(Optional.of(tipo));
        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = usuarioService.atualizarTipoUsuario(1L, 2L);

        assertEquals(2L, response.tipoUsuario().id());
    }
}
