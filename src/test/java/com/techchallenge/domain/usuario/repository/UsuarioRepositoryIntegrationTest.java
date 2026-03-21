package com.techchallenge.domain.usuario.repository;

import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    private TipoUsuario tipoCliente;

    @BeforeEach
    void setup() {
        tipoCliente = new TipoUsuario();
        tipoCliente.setNome("Cliente");
        tipoCliente = tipoUsuarioRepository.save(tipoCliente);
    }

    @Test
    void salvarEBuscarPorEmail() {
        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setEmail("maria@tech.com");
        usuario.setSenha("123456");
        usuario.setEndereco("Rua A, 1");
        usuario.setRole(UsuarioRole.CLIENT);
        usuario.setUltimaAtualizacao(LocalDateTime.now());
        usuario.setTipoUsuario(tipoCliente);

        usuarioRepository.save(usuario);

        assertTrue(usuarioRepository.findByEmail("maria@tech.com").isPresent());
        assertTrue(usuarioRepository.existsByEmail("maria@tech.com"));
    }

    @Test
    void buscarUsuariosPorNomeContendo() {
        Usuario u1 = new Usuario();
        u1.setNome("Joao Victor");
        u1.setEmail("joao@tech.com");
        u1.setSenha("123456");
        u1.setEndereco("Rua B, 2");
        u1.setRole(UsuarioRole.CLIENT);
        u1.setUltimaAtualizacao(LocalDateTime.now());
        u1.setTipoUsuario(tipoCliente);
        usuarioRepository.save(u1);

        List<Usuario> lista = usuarioRepository.findByNomeContainingIgnoreCase("joao");
        assertFalse(lista.isEmpty());
    }

    @Test
    void existeUsuarioPorTipoId() {
        Usuario u = new Usuario();
        u.setNome("Dono");
        u.setEmail("dono@tech.com");
        u.setSenha("123456");
        u.setEndereco("Rua C, 3");
        u.setRole(UsuarioRole.CLIENT);
        u.setUltimaAtualizacao(LocalDateTime.now());
        u.setTipoUsuario(tipoCliente);

        usuarioRepository.save(u);

        assertTrue(usuarioRepository.existsByTipoUsuarioId(tipoCliente.getId()));
        assertEquals(1, usuarioRepository.findByTipoUsuarioId(tipoCliente.getId()).size());
    }
}
