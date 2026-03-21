package com.techchallenge.domain.restaurante.repository;

import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RestauranteRepositoryIntegrationTest {

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
    void salvarRestauranteEEncontrarPorId() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Cantina Test");
        restaurante.setEndereco("Rua Teste, 2");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioFuncionamento("Seg-Dom 10:00-22:00");
        restaurante.setDono(dono);

        Restaurante salvo = restauranteRepository.save(restaurante);

        assertThat(restauranteRepository.findById(salvo.getId())).isPresent();
        assertThat(restauranteRepository.findAll()).hasSize(1);
    }

    @Test
    void atualizarRestauranteDonoMantido() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Sabores");
        restaurante.setEndereco("Rua Nova, 5");
        restaurante.setTipoCozinha("Brasileira");
        restaurante.setHorarioFuncionamento("Seg-Sex 09:00-18:00");
        restaurante.setDono(dono);

        Restaurante salvo = restauranteRepository.save(restaurante);
        salvo.setEndereco("Av. Atualizada, 10");

        Restaurante atualizado = restauranteRepository.save(salvo);

        assertThat(atualizado.getEndereco()).isEqualTo("Av. Atualizada, 10");
        assertThat(atualizado.getDono()).isNotNull();
    }
}
