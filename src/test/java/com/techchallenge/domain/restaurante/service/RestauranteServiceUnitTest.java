package com.techchallenge.domain.restaurante.service;

import com.techchallenge.domain.restaurante.dto.RestauranteCreateDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteUpdateDTO;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.restaurante.repository.RestauranteRepository;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceUnitTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RestauranteService restauranteService;

    private Usuario novoDono() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Dono");
        usuario.setEmail("dono@tech.com");
        usuario.setSenha("123456");
        usuario.setRole(UsuarioRole.DONO);
        usuario.setEndereco("Rua A");
        usuario.setUltimaAtualizacao(LocalDateTime.now());
        return usuario;
    }

    @Test
    void criarRestauranteComDonoExistente() {
        Usuario aluno = novoDono();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestauranteCreateDTO dto = new RestauranteCreateDTO("Novo", "Endereco", "Tipo", "Horario", 1L);
        var response = restauranteService.criar(dto);

        assertEquals("Novo", response.nome());
        verify(restauranteRepository).save(any());
    }

    @Test
    void criarRestauranteSemDonoLancaExcecao() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> restauranteService.criar(new RestauranteCreateDTO("Nome", "End", "Tipo", "Hor", 1L)));
    }

    @Test
    void atualizarRestauranteComSucesso() {
        Usuario dono = novoDono();
        Restaurante existente = new Restaurante();
        existente.setId(1L);
        existente.setDono(dono);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(dono));
        when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestauranteUpdateDTO dto = new RestauranteUpdateDTO("Atualizado", "Av. N", "Tipo", "Horario", 1L);
        var response = restauranteService.atualizar(1L, dto);

        assertEquals("Atualizado", response.nome());
    }

    @Test
    void atualizarRestauranteInexistenteLancaExcecao() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> restauranteService.atualizar(1L, new RestauranteUpdateDTO("Nome", "End", "Tipo", "Hor", 1L)));
    }

    @Test
    void atualizarComDonoInexistenteLancaExcecao() {
        Restaurante existente = new Restaurante();
        existente.setId(1L);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> restauranteService.atualizar(1L, new RestauranteUpdateDTO("Nome", "End", "Tipo", "Hor", 1L)));
    }

    @Test
    void deletarRestauranteExistente() {
        when(restauranteRepository.existsById(1L)).thenReturn(true);

        restauranteService.deletar(1L);

        verify(restauranteRepository).deleteById(1L);
    }

    @Test
    void deletarRestauranteInexistenteLancaErro() {
        when(restauranteRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> restauranteService.deletar(1L));
    }
}
