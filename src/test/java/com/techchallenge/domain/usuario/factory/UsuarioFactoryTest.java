package com.techchallenge.domain.usuario.factory;

import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.usuario.dto.UsuarioCreateDTO;
import com.techchallenge.domain.usuario.dto.UsuarioResponseDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateDTO;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioFactoryTest {

    @Test
    void fromCreateDTODefineRoleClienteEData() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Usuario", "usuario@tech.com", "senha", "Rua A", null);

        Usuario usuario = UsuarioFactory.fromCreateDTO(dto);

        assertNull(usuario.getId());
        assertEquals("Usuario", usuario.getNome());
        assertEquals("usuario@tech.com", usuario.getEmail());
        assertEquals("senha", usuario.getSenha());
        assertEquals("Rua A", usuario.getEndereco());
        assertEquals(UsuarioRole.CLIENT, usuario.getRole());
        assertNotNull(usuario.getUltimaAtualizacao());
    }

    @Test
    void applyUpdateSubstituiCamposEAtualizaTimestamp() {
        Usuario usuario = new Usuario();
        usuario.setUltimaAtualizacao(LocalDateTime.of(2000, 1, 1, 0, 0));

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO("Novo Nome", "novo@tech.com", "Rua Nova");

        UsuarioFactory.applyUpdate(usuario, dto);

        assertEquals("Novo Nome", usuario.getNome());
        assertEquals("novo@tech.com", usuario.getEmail());
        assertEquals("Rua Nova", usuario.getEndereco());
        assertNotNull(usuario.getUltimaAtualizacao());
        assertTrue(usuario.getUltimaAtualizacao().isAfter(LocalDateTime.of(2000, 1, 1, 0, 0)));
    }

    @Test
    void applyUpdateUserRoleAlteraRole() {
        Usuario usuario = new Usuario();
        usuario.setRole(UsuarioRole.CLIENT);

        UsuarioFactory.applyUpdateUserRole(usuario, UsuarioRole.ADMIN);

        assertEquals(UsuarioRole.ADMIN, usuario.getRole());
    }

    @Test
    void applySenhaUpdateAlteraSenhaEData() {
        Usuario usuario = new Usuario();
        usuario.setSenha("antiga");
        LocalDateTime antes = LocalDateTime.of(2010, 1, 1, 0, 0);
        usuario.setUltimaAtualizacao(antes);

        UsuarioFactory.applySenhaUpdate(usuario, "novaSenha");

        assertEquals("novaSenha", usuario.getSenha());
        assertNotNull(usuario.getUltimaAtualizacao());
        assertTrue(usuario.getUltimaAtualizacao().isAfter(antes));
    }

    @Test
    void toResponseDTOSemTipoMantemTipoNulo() {
        Usuario usuario = Usuario.builder()
                .id(10L)
                .nome("Sem Tipo")
                .email("tipo@tech.com")
                .endereco("Rua Teste")
                .role(UsuarioRole.CLIENT)
                .ultimaAtualizacao(LocalDateTime.now())
                .build();

        UsuarioResponseDTO dto = UsuarioFactory.toResponseDTO(usuario);

        assertEquals(10L, dto.id());
        assertNull(dto.tipoUsuario());
    }

    @Test
    void toResponseDTOComTipoIncluiDadosDoTipo() {
        Usuario usuario = Usuario.builder()
                .id(11L)
                .nome("Com Tipo")
                .email("tipo@tech.com")
                .endereco("Rua Tipo")
                .role(UsuarioRole.CLIENT)
                .ultimaAtualizacao(LocalDateTime.now())
                .build();
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(3L);
        tipo.setNome("Cliente");
        usuario.setTipoUsuario(tipo);

        UsuarioResponseDTO dto = UsuarioFactory.toResponseDTO(usuario);

        assertEquals(11L, dto.id());
        assertNotNull(dto.tipoUsuario());
        assertEquals(3L, dto.tipoUsuario().id());
        assertEquals("Cliente", dto.tipoUsuario().nome());
    }
}
