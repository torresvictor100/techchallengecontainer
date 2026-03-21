package com.techchallenge.domain.cardapio.service;

import com.techchallenge.domain.cardapio.dto.ItemCardapioCreateDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioUpdateDTO;
import com.techchallenge.domain.cardapio.repository.ItemCardapioRepository;
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
@Import(ItemCardapioService.class)
class ItemCardapioServiceIntegrationTest {

    @Autowired
    private ItemCardapioService itemService;

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
        tipo.setNome("Dono");
        tipo = tipoUsuarioRepository.save(tipo);

        Usuario dono = new Usuario();
        dono.setNome("Dono");
        dono.setEmail("dono.cardapio@tech.com");
        dono.setSenha("123456");
        dono.setEndereco("Rua X, 1");
        dono.setUltimaAtualizacao(LocalDateTime.now());
        dono.setRole(UsuarioRole.DONO);
        dono.setTipoUsuario(tipo);
        usuarioRepository.save(dono);

        Restaurante r = new Restaurante();
        r.setNome("Cantina Prato");
        r.setEndereco("Rua Y, 2");
        r.setTipoCozinha("Italiana");
        r.setHorarioFuncionamento("Seg-Dom 11:00-22:00");
        r.setDono(dono);
        restaurante = restauranteRepository.save(r);
    }

    @Test
    void criarItemComSucesso() {
        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Lasanha",
                "Descricao",
                new java.math.BigDecimal("30.00"),
                false,
                "/imagens/lasanha.jpg",
                restaurante.getId());

        var result = itemService.criar(dto);

        assertThat(result.nome()).isEqualTo("Lasanha");
        assertThat(itemRepository.findById(result.id())).isPresent();
    }

    @Test
    void criarComRestauranteInexistenteLancaExcecao() {
        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Prato",
                "Descricao",
                new java.math.BigDecimal("20.00"),
                true,
                "/imagens/prato.jpg",
                999L);

        assertThrows(EntityNotFoundException.class, () -> itemService.criar(dto));
    }

    @Test
    void atualizarItemComSucesso() {
        var criado = itemService.criar(new ItemCardapioCreateDTO(
                "Lasanha",
                "Descricao",
                new java.math.BigDecimal("30.00"),
                true,
                "/imagens/lasanha.jpg",
                restaurante.getId()));

        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Lasanha Especial",
                "Outra desc",
                new java.math.BigDecimal("35.00"),
                false,
                "/imagens/lasanha-especial.jpg",
                restaurante.getId());

        var atualizado = itemService.atualizar(criado.id(), dto);

        assertThat(atualizado.nome()).isEqualTo("Lasanha Especial");
    }

    @Test
    void atualizarComRestauranteInexistenteLancaExcecao() {
        var criado = itemService.criar(new ItemCardapioCreateDTO(
                "Prato",
                "Desc",
                new java.math.BigDecimal("15.00"),
                true,
                "/imagens/p.jpg",
                restaurante.getId()));

        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Prato",
                "Desc",
                new java.math.BigDecimal("15.00"),
                true,
                "/imagens/p.jpg",
                999L);

        assertThrows(EntityNotFoundException.class, () -> itemService.atualizar(criado.id(), dto));
    }

    @Test
    void deletarItemExistente() {
        var criado = itemService.criar(new ItemCardapioCreateDTO(
                "Prato",
                "Desc",
                new java.math.BigDecimal("15.00"),
                true,
                "/imagens/p.jpg",
                restaurante.getId()));

        itemService.deletar(criado.id());

        assertThat(itemRepository.existsById(criado.id())).isFalse();
    }

    @Test
    void deletarItemInexistenteLancaExcecao() {
        assertThrows(EntityNotFoundException.class, () -> itemService.deletar(999L));
    }
}
