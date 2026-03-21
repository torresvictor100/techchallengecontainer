package com.techchallenge.domain.usuario.repository;

import com.techchallenge.domain.usuario.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryUnitTest {

    @Test
    void findByNomeEtcSimulados() {
        UsuarioRepository repository = mock(UsuarioRepository.class);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Joao");

        when(repository.findByNomeContainingIgnoreCase("joao")).thenReturn(List.of(usuario));
        when(repository.existsByEmail("joao@tech.com")).thenReturn(true);
        when(repository.findByEmail("joao@tech.com")).thenReturn(java.util.Optional.of(usuario));

        assertEquals(1, repository.findByNomeContainingIgnoreCase("joao").size());
        assertTrue(repository.existsByEmail("joao@tech.com"));
        assertTrue(repository.findByEmail("joao@tech.com").isPresent());

        verify(repository).findByNomeContainingIgnoreCase("joao");
        verify(repository).existsByEmail("joao@tech.com");
        verify(repository).findByEmail("joao@tech.com");
    }
}
