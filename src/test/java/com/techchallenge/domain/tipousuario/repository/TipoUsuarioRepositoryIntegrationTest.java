package com.techchallenge.domain.tipousuario.repository;

import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TipoUsuarioRepositoryIntegrationTest {

    @Autowired
    private TipoUsuarioRepository repository;

    @Test
    void salvarEEncontrarPorNomeIgnoreCase() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setNome("Cliente");
        repository.save(tipo);

        assertTrue(repository.findByNomeIgnoreCase("cliente").isPresent());
        assertTrue(repository.existsByNomeIgnoreCase("CLIENTE"));
    }

    @Test
    void encontrarPorNomeNaoExistente() {
        assertTrue(repository.findByNomeIgnoreCase("Inexistente").isEmpty());
        assertFalse(repository.existsByNomeIgnoreCase("Inexistente"));
    }
}
