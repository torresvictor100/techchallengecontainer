package com.techchallenge.domain.tipousuario.service;

import com.techchallenge.domain.tipousuario.dto.TipoUsuarioCreateDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioUpdateDTO;
import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TipoUsuarioService.class)
class TipoUsuarioServiceIntegrationTest {

    @Autowired
    private TipoUsuarioService service;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void criarEBuscarPorId() {
        var criado = service.criar(new TipoUsuarioCreateDTO("Cliente"));
        var encontrado = service.buscarPorId(criado.id());

        assertEquals("Cliente", encontrado.nome());
    }

    @Test
    void atualizarTipoComSucesso() {
        var criado = service.criar(new TipoUsuarioCreateDTO("Tipo Antigo"));

        var atualizado = service.atualizar(criado.id(), new TipoUsuarioUpdateDTO("Tipo Novo"));

        assertEquals("Tipo Novo", atualizado.nome());
    }

    @Test
    void criarTipoDuplicadoLancaExcecao() {
        service.criar(new TipoUsuarioCreateDTO("Dono de Restaurante"));

        assertThrows(IllegalArgumentException.class,
                () -> service.criar(new TipoUsuarioCreateDTO("Dono de Restaurante")));
    }

    @Test
    void atualizarParaNomeDuplicadoLancaExcecao() {
        var tipoA = service.criar(new TipoUsuarioCreateDTO("Tipo A"));
        var tipoB = service.criar(new TipoUsuarioCreateDTO("Tipo B"));

        assertThrows(IllegalArgumentException.class,
                () -> service.atualizar(tipoA.id(), new TipoUsuarioUpdateDTO(tipoB.nome())));
    }

    @Test
    void deletarTipoAssociadoLancaExcecao() {
        var tipo = service.criar(new TipoUsuarioCreateDTO("Associado"));

        Usuario usuario = new Usuario();
        usuario.setNome("Joao");
        usuario.setEmail("joao.associado@tech.com");
        usuario.setSenha("123456");
        usuario.setEndereco("Rua A, 1");
        usuario.setUltimaAtualizacao(LocalDateTime.now());
        usuario.setRole(UsuarioRole.CLIENT);
        usuario.setTipoUsuario(tipoUsuarioRepository.findById(tipo.id()).orElseThrow());

        usuarioRepository.save(usuario);

        assertThrows(IllegalArgumentException.class, () -> service.deletar(tipo.id()));
    }

    @Test
    void deletarTipoComSucesso() {
        var tipo = service.criar(new TipoUsuarioCreateDTO("Removivel"));

        service.deletar(tipo.id());

        assertTrue(tipoUsuarioRepository.findById(tipo.id()).isEmpty());
    }

    @Test
    void listarTodosRetornaItens() {
        service.criar(new TipoUsuarioCreateDTO("Tipo 1"));
        service.criar(new TipoUsuarioCreateDTO("Tipo 2"));

        var lista = service.listarTodos();

        assertTrue(lista.size() >= 2);
    }

    @Test
    void deletarTipoInexistenteLancaExcecao() {
        assertThrows(EntityNotFoundException.class, () -> service.deletar(9999L));
    }
}
