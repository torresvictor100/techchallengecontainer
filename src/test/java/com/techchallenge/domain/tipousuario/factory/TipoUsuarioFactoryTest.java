package com.techchallenge.domain.tipousuario.factory;

import com.techchallenge.domain.tipousuario.dto.TipoUsuarioCreateDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioUpdateDTO;
import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoUsuarioFactoryTest {

    @Test
    void fromCreateDTOCriaEntidadeComNome() {
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("Cliente");

        TipoUsuario tipo = TipoUsuarioFactory.fromCreateDTO(dto);

        assertNull(tipo.getId());
        assertEquals("Cliente", tipo.getNome());
    }

    @Test
    void applyUpdateAtualizaNome() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(1L);
        tipo.setNome("Antigo");

        TipoUsuarioFactory.applyUpdate(tipo, new TipoUsuarioUpdateDTO("Novo"));

        assertEquals(1L, tipo.getId());
        assertEquals("Novo", tipo.getNome());
    }

    @Test
    void toResponseDTOMapeiaCampos() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(10L);
        tipo.setNome("Dono de Restaurante");

        var dto = TipoUsuarioFactory.toResponseDTO(tipo);

        assertEquals(10L, dto.id());
        assertEquals("Dono de Restaurante", dto.nome());
    }
}
