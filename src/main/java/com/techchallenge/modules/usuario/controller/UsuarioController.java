package com.techchallenge.modules.usuario.controller;

import com.techchallenge.modules.usuario.dto.*;
import com.techchallenge.modules.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/usuarios")
@Tag(name = "Usuários", description = "Endpoints de gerenciamento de usuários")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService service;

    @Autowired
    private HttpServletRequest request;

    private boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase((String) request.getAttribute("role"));
    }

    private String getLoggedEmail() {
        return (String) request.getAttribute("email");
    }

    private void checkPermission(String emailDono) {
        if (!isAdmin() && !emailDono.equalsIgnoreCase(getLoggedEmail())) {
            log.warn("⛔ CLIENT tentou acessar recurso de outro usuário (Email logado: {}, Dono: {})",
                    getLoggedEmail(), emailDono);

            throw new SecurityException("Você não tem permissão para acessar ou alterar este usuário");
        }
    }

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/todos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {

        log.info("📌 [GET] Solicitação para listar todos os usuários...");

        if (!isAdmin()) {
            log.warn("⛔ CLIENT tentou acessar lista de usuários!");
            throw new SecurityException("Apenas administradores podem listar todos os usuários");
        }

        List<UsuarioResponseDTO> lista = service.listarTodos();

        log.info("📄 {} usuários retornados.", lista.size());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {

        log.info("🔍 [GET] Buscando usuário ID {}", id);

        UsuarioResponseDTO user = service.buscarPorId(id);

        if (!request.isUserInRole("ADMIN")) {
            checkPermission(user.email());
        }

        log.info("✔ Usuário ID {} retornado com sucesso", id);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> criar(
            @Valid @RequestBody UsuarioCreateDTO dto) {

        log.info("📝 [POST] Criando usuário com email {}", dto.email());

        UsuarioResponseDTO criado = service.criar(dto);

        log.info("✔ Usuário criado ID {}", criado.id());

        return ResponseEntity.ok(criado);
    }

    @Operation(summary = "Atualizar role do usuário", description = "Atualiza a role de um usuário (apenas ADMIN pode alterar)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (somente ADMIN)"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/role")
    public ResponseEntity<UsuarioResponseDTO> atualizarRole(
            @Valid @RequestBody UsuarioUpdateRoleDTO dto) {

        log.info("🛡️ [PATCH] ADMIN solicitou atualização de role do usuário ID {} para {}",
                dto.idUser(), dto.role());

        UsuarioResponseDTO atualizado = service.atualizarRole(dto);

        log.info("✅ Role atualizada com sucesso para usuário ID {}", dto.idUser());

        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Buscar usuários por nome", description = "Busca usuários pelo nome (parcial) e retorna uma lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(@RequestParam String nome) {

        log.info("🔍 [GET] Buscando usuários por nome: {}", nome);

        if (!isAdmin()) {
            log.warn("⛔ CLIENT tentou buscar usuários por nome!");
            throw new SecurityException("Apenas administradores podem buscar usuários por nome");
        }

        List<UsuarioResponseDTO> lista = service.buscarPorNome(nome);

        log.info("📄 {} usuários retornados na busca por nome '{}'", lista.size(), nome);

        return ResponseEntity.ok(lista);
    }


    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados enviados")
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto) {

        log.info("✏ [PUT] Atualizando usuário ID {}", id);

        UsuarioResponseDTO user = service.buscarPorId(id);

        checkPermission(user.email());

        UsuarioResponseDTO atualizado = service.atualizar(id, dto);

        log.info("✔ Usuário ID {} atualizado com sucesso", id);

        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Atualizar senha do usuário", description = "Atualiza apenas a senha de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro nos dados enviados")
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> atualizarSenha(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateSenhaDTO dto) {

        log.info("🔐 [PATCH] Atualizando senha do usuário ID {}", id);

        UsuarioResponseDTO user = service.buscarPorId(id);

        checkPermission(user.email());

        service.atualizarSenha(id, dto);

        log.info("✔ Senha atualizada para usuário ID {}", id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário removido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        log.warn("🗑 [DELETE] Tentativa de deletar usuário ID {}", id);

        UsuarioResponseDTO user = service.buscarPorId(id);

        checkPermission(user.email());

        service.deletar(id);

        log.info("🗑✔ Usuário ID {} deletado com sucesso!", id);

        return ResponseEntity.ok().build();
    }
}
