package com.techchallenge.domain.tipousuario.repository;

import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioRepositoryUnitTest {

    @Test
    void stubBasicoDoRepositorio() {
        TipoUsuarioRepository repository = mock(TipoUsuarioRepository.class);

        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(1L);
        tipo.setNome("Cliente");

        when(repository.findByNomeIgnoreCase("Cliente")).thenReturn(Optional.of(tipo));
        when(repository.existsByNomeIgnoreCase("Cliente")).thenReturn(true);

        assertTrue(repository.findByNomeIgnoreCase("Cliente").isPresent());
        assertTrue(repository.existsByNomeIgnoreCase("Cliente"));

        verify(repository).findByNomeIgnoreCase("Cliente");
        verify(repository).existsByNomeIgnoreCase("Cliente");
    }
}
