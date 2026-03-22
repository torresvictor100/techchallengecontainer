package com.techchallenge.domain.restaurante.factory;

import com.techchallenge.domain.restaurante.dto.RestauranteCreateDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteUpdateDTO;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.usuario.entity.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteFactoryTest {

    @Test
    void fromCreateDTOCriaRestauranteComDono() {
        RestauranteCreateDTO dto = new RestauranteCreateDTO(
                "Cantina da Praca",
                "Rua Central, 100",
                "Italiana",
                "Seg-Dom 11:00-23:00",
                2L
        );
        Usuario dono = criarUsuario(2L, "Joao", "joao@tech.com");

        Restaurante restaurante = RestauranteFactory.fromCreateDTO(dto, dono);

        assertNull(restaurante.getId());
        assertEquals(dto.nome(), restaurante.getNome());
        assertEquals(dto.endereco(), restaurante.getEndereco());
        assertEquals(dto.tipoCozinha(), restaurante.getTipoCozinha());
        assertEquals(dto.horarioFuncionamento(), restaurante.getHorarioFuncionamento());
        assertSame(dono, restaurante.getDono());
    }

    @Test
    void applyUpdateAlteraCamposEAssociaNovoDono() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(5L);
        restaurante.setNome("Antiga");
        restaurante.setEndereco("Rua Velha");
        restaurante.setTipoCozinha("Antiga");
        restaurante.setHorarioFuncionamento("Seg-Sab");

        RestauranteUpdateDTO dto = new RestauranteUpdateDTO(
                "Cantina Atualizada",
                "Av. Nova, 200",
                "Brasileira",
                "Seg-Sex 10:00-22:00",
                3L
        );
        Usuario novoDono = criarUsuario(3L, "Lucia", "lucia@tech.com");

        RestauranteFactory.applyUpdate(restaurante, dto, novoDono);

        assertEquals(5L, restaurante.getId());
        assertEquals(dto.nome(), restaurante.getNome());
        assertEquals(dto.endereco(), restaurante.getEndereco());
        assertEquals(dto.tipoCozinha(), restaurante.getTipoCozinha());
        assertEquals(dto.horarioFuncionamento(), restaurante.getHorarioFuncionamento());
        assertSame(novoDono, restaurante.getDono());
    }

    @Test
    void toResponseDTOIncluiCamposDoDonoQuandoPresente() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(10L);
        restaurante.setNome("Cantina");
        restaurante.setEndereco("Rua A");
        restaurante.setTipoCozinha("Nordestina");
        restaurante.setHorarioFuncionamento("Seg-Dom 11:00-23:00");
        Usuario dono = criarUsuario(7L, "Carlos", "carlos@tech.com");
        restaurante.setDono(dono);

        var dto = RestauranteFactory.toResponseDTO(restaurante);

        assertEquals(10L, dto.id());
        assertEquals("Cantina", dto.nome());
        assertEquals("Rua A", dto.endereco());
        assertEquals("Nordestina", dto.tipoCozinha());
        assertEquals("Seg-Dom 11:00-23:00", dto.horarioFuncionamento());
        assertEquals(7L, dto.donoId());
        assertEquals("Carlos", dto.donoNome());
        assertEquals("carlos@tech.com", dto.donoEmail());
    }

    @Test
    void toResponseDTOSemDonoMantemCamposNulos() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(11L);
        restaurante.setNome("Cantina Solitaria");
        restaurante.setEndereco("Rua B");
        restaurante.setTipoCozinha("Vegetariana");
        restaurante.setHorarioFuncionamento("Seg-Dom 10:00-20:00");

        var dto = RestauranteFactory.toResponseDTO(restaurante);

        assertEquals(11L, dto.id());
        assertNull(dto.donoId());
        assertNull(dto.donoNome());
        assertNull(dto.donoEmail());
    }

    private Usuario criarUsuario(Long id, String nome, String email) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        return usuario;
    }
}
