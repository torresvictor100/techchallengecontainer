package com.techchallenge.domain.usuario.service;

import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.dto.UsuarioCreateDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateSenhaDTO;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import com.techchallenge.domain.usuario.security.SecurityBeansConfig;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({UsuarioService.class, SecurityBeansConfig.class})
class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private TipoUsuario cliente;

    @BeforeEach
    void setup() {
        cliente = new TipoUsuario();
        cliente.setNome("Cliente");
        cliente = tipoUsuarioRepository.save(cliente);
    }

    @Test
    void criarUsuarioComTipoCustomizado() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Joao",
                "joao.tipo@tech.com",
                "123456",
                "Rua A, 1",
                cliente.getId());

        var response = usuarioService.criar(dto);

        assertEquals("Joao", response.nome());
        assertEquals("joao.tipo@tech.com", response.email());
        assertNotNull(usuarioRepository.findByEmail("joao.tipo@tech.com"));
    }

    @Test
    void criarUsuarioDuplicadoLancaExcecao() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Cliente",
                "cliente@tech.com",
                "123456",
                "Rua B, 2",
                cliente.getId());

        usuarioService.criar(dto);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.criar(dto));
    }

    @Test
    void buscarPorNomeInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> usuarioService.buscarPorNome(" "));
    }

    @Test
    void buscarPorEmailInexistenteLancaExcecao() {
        assertThrows(EntityNotFoundException.class, () -> usuarioService.buscarPorEmail("naoexiste@tech.com"));
    }

    @Test
    void atualizarSenhaComSucesso() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Senhador",
                "senha@tech.com",
                "123456",
                "Rua C, 3",
                cliente.getId());
        var criado = usuarioService.criar(dto);

        usuarioService.atualizarSenha(criado.id(),
                new UsuarioUpdateSenhaDTO("123456", "NovaSenha@123"));

        var atualizado = usuarioRepository.findById(criado.id()).orElseThrow();
        assertNotEquals("123456", atualizado.getSenha());
        assertTrue(passwordEncoder.matches("NovaSenha@123", atualizado.getSenha()));
    }

    @Test
    void deletarUsuarioInexistenteLancaExcecao() {
        assertThrows(EntityNotFoundException.class, () -> usuarioService.deletar(999L));
    }
}
