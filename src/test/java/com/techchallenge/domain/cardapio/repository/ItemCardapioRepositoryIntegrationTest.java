package com.techchallenge.domain.cardapio.repository;

import com.techchallenge.domain.cardapio.entity.ItemCardapio;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.restaurante.repository.RestauranteRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ItemCardapioRepositoryIntegrationTest {

    @Autowired
    private ItemCardapioRepository itemRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    private Restaurante restaurante;

    @BeforeEach
    void setup() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setNome("Dono de Restaurante");
        tipo = tipoUsuarioRepository.save(tipo);

        Usuario dono = new Usuario();
        dono.setNome("Dono");
        dono.setEmail("dono.cardapio@tech.com");
        dono.setSenha("123456");
        dono.setEndereco("Rua A, 1");
        dono.setUltimaAtualizacao(LocalDateTime.now());
        dono.setRole(UsuarioRole.DONO);
        dono.setTipoUsuario(tipo);
        usuarioRepository.save(dono);

        Restaurante r = new Restaurante();
        r.setNome("Cantina Cardapio");
        r.setEndereco("Rua B, 2");
        r.setTipoCozinha("Italiana");
        r.setHorarioFuncionamento("Seg-Dom 10:00-23:00");
        r.setDono(dono);
        restaurante = restauranteRepository.save(r);
    }

    @Test
    void salvarEBuscarPorNome() {
        ItemCardapio item = new ItemCardapio();
        item.setNome("Lasanha Teste");
        item.setDescricao("Descricao");
        item.setPreco(new BigDecimal("25.90"));
        item.setSomenteNoRestaurante(true);
        item.setFotoPath("/imagens/teste.jpg");
        item.setRestaurante(restaurante);

        itemRepository.save(item);

        assertThat(itemRepository.findAll()).hasSize(1);
        assertThat(itemRepository.findById(item.getId())).isPresent();
    }

    @Test
    void atualizarRegistroMantemRestaurante() {
        ItemCardapio item = new ItemCardapio();
        item.setNome("Prato");
        item.setDescricao("Descricao");
        item.setPreco(new BigDecimal("20.00"));
        item.setSomenteNoRestaurante(true);
        item.setFotoPath("/imagens/prato.jpg");
        item.setRestaurante(restaurante);

        item = itemRepository.save(item);
        item.setPreco(new BigDecimal("22.00"));

        itemRepository.save(item);

        assertThat(itemRepository.findById(item.getId()).get().getPreco()).isEqualByComparingTo("22.00");
    }
}
