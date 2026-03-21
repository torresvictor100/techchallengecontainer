package com.techchallenge.domain.restaurante.service;

import com.techchallenge.domain.restaurante.dto.RestauranteCreateDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteUpdateDTO;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.restaurante.repository.RestauranteRepository;
import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@Import(RestauranteService.class)
class RestauranteServiceIntegrationTest {

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    private Usuario dono;

    @BeforeEach
    void setup() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setNome("Dono de Restaurante");
        tipo = tipoUsuarioRepository.save(tipo);

        Usuario usuario = new Usuario();
        usuario.setNome("Dono");
        usuario.setEmail("dono@tech.com");
        usuario.setSenha("123456");
        usuario.setEndereco("Rua do Dono, 1");
        usuario.setUltimaAtualizacao(LocalDateTime.now());
        usuario.setRole(UsuarioRole.DONO);
        usuario.setTipoUsuario(tipo);
        dono = usuarioRepository.save(usuario);
    }

    @Test
    void criarRestauranteComSucesso() {
        RestauranteCreateDTO dto = new RestauranteCreateDTO(
                "Cantina Teste",
                "Rua do Teste, 10",
                "Italiana",
                "Seg-Dom 11:00-22:00",
                dono.getId());

        var response = restauranteService.criar(dto);

        assertThat(response.nome()).isEqualTo("Cantina Teste");
        assertThat(restauranteRepository.findById(response.id())).isPresent();
    }

    @Test
    void criarSemDonoLancaExcecao() {
        RestauranteCreateDTO dto = new RestauranteCreateDTO(
                "Sem Dono",
                "Rua Nao, 1",
                "Italiana",
                "Seg-Dom 11:00-22:00",
                999L);

        assertThrows(EntityNotFoundException.class, () -> restauranteService.criar(dto));
    }

    @Test
    void atualizarRestauranteExistente() {
        var criado = restauranteService.criar(new RestauranteCreateDTO(
                "Cantina Teste",
                "Rua do Teste, 10",
                "Italiana",
                "Seg-Dom 11:00-22:00",
                dono.getId()));

        RestauranteUpdateDTO updateDTO = new RestauranteUpdateDTO(
                "Cantina Atualizada",
                "Av. Nova, 20",
                "Brasileira",
                "Seg-Sex 10:00-22:00",
                dono.getId());

        var atualizado = restauranteService.atualizar(criado.id(), updateDTO);

        assertThat(atualizado.nome()).isEqualTo("Cantina Atualizada");
        assertThat(restauranteRepository.findById(criado.id()).get().getTipoCozinha()).isEqualTo("Brasileira");
    }

    @Test
    void atualizarRestauranteInexistenteLancaExcecao() {
        RestauranteUpdateDTO dto = new RestauranteUpdateDTO(
                "Nome",
                "Endereco",
                "Tipo",
                "Horario",
                dono.getId());

        assertThrows(EntityNotFoundException.class, () -> restauranteService.atualizar(999L, dto));
    }

    @Test
    void deletarRestauranteExistente() {
        var criado = restauranteService.criar(new RestauranteCreateDTO(
                "Restaurante",
                "Rua 1",
                "Tipo",
                "Seg-Dom 11:00-22:00",
                dono.getId()));

        restauranteService.deletar(criado.id());

        assertThat(restauranteRepository.existsById(criado.id())).isFalse();
    }

    @Test
    void deletarRestauranteInexistenteLancaExcecao() {
        assertThrows(EntityNotFoundException.class, () -> restauranteService.deletar(999L));
    }
}
