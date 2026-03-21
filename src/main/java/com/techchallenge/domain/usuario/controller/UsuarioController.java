package com.techchallenge.domain.usuario.controller;

import com.techchallenge.domain.auth.dto.ErrorResponseDTO;
import com.techchallenge.domain.usuario.dto.UsuarioCreateDTO;
import com.techchallenge.domain.usuario.dto.UsuarioResponseDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateRoleDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateSenhaDTO;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateTipoDTO;
import com.techchallenge.domain.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
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

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Lista de usuários",
                                    value = "[\n" +
                                            "  {\n" +
                                            "    \"id\": 1,\n" +
                                            "    \"nome\": \"Admin\",\n" +
                                            "    \"email\": \"admin@tech.com\",\n" +
                                            "    \"endereco\": \"Rua A, 123\",\n" +
                                            "    \"role\": \"ADMIN\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"id\": 2,\n" +
                                            "    \"nome\": \"João\",\n" +
                                            "    \"email\": \"joao@tech.com\",\n" +
                                            "    \"endereco\": \"Rua B, 456\",\n" +
                                            "    \"role\": \"CLIENT\"\n" +
                                            "  }\n" +
                                            "]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado (não é ADMIN)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Forbidden",
                                    value = "{\n  \"status\": 403,\n  \"message\": \"Apenas administradores podem listar todos os usuários\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Unauthorized",
                                    value = "{\n  \"status\": 401,\n  \"message\": \"Token ausente ou mal formatado. Use: Authorization: Bearer <token>\"\n}"
                            )
                    )
            )
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

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico (ADMIN ou dono)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário encontrado",
                                    value = "{\n  \"id\": 2,\n  \"nome\": \"João\",\n  \"email\": \"joao@tech.com\",\n  \"endereco\": \"Rua B, 456\",\n  \"role\": \"CLIENT\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Not Found",
                                    value = "{\n  \"status\": 404,\n  \"message\": \"Usuário não encontrado\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para acessar o usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Forbidden",
                                    value = "{\n  \"status\": 403,\n  \"message\": \"Você não tem permissão para acessar ou alterar este usuário\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Unauthorized",
                                    value = "{\n  \"status\": 401,\n  \"message\": \"Token inválido\"\n}"
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @Parameter(description = "ID do usuário", example = "2")
            @PathVariable Long id
    ) {

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
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário criado",
                                    value = "{\n  \"id\": 10,\n  \"nome\": \"Novo Usuário\",\n  \"email\": \"novo@tech.com\",\n  \"endereco\": \"Rua Centro, 123 - Recife\",\n  \"role\": \"CLIENT\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Email duplicado",
                                            value = "{\n  \"status\": 400,\n  \"message\": \"Email já está em uso.\"\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Validação",
                                            value = "{\n  \"status\": 400,\n  \"message\": \"Dados inválidos\"\n}"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> criar(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioCreateDTO.class),
                            examples = @ExampleObject(
                                    name = "Request de cadastro",
                                    value = "{\n  \"nome\": \"Novo Usuário\",\n  \"email\": \"novo@tech.com\",\n  \"senha\": \"123456\",\n  \"endereco\": \"Rua Centro, 123 - Recife\"\n}"
                            )
                    )
            )
            @RequestBody UsuarioCreateDTO dto) {

        log.info("📝 [POST] Criando usuário com email {}", dto.email());

        UsuarioResponseDTO criado = service.criar(dto);

        log.info("✔ Usuário criado ID {}", criado.id());

        return ResponseEntity.ok(criado);
    }

    @Operation(summary = "Atualizar role do usuário", description = "Atualiza a role de um usuário (somente ADMIN pode alterar)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Role atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Role atualizada",
                                    value = "{\n  \"id\": 2,\n  \"nome\": \"João\",\n  \"email\": \"joao@tech.com\",\n  \"endereco\": \"Rua B, 456\",\n  \"role\": \"ADMIN\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Role inválida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Role inválida",
                                    value = "{\n  \"status\": 400,\n  \"message\": \"Role inválida. Valores permitidos: ADMIN, CLIENT, DONO\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Not Found",
                                    value = "{\n  \"status\": 404,\n  \"message\": \"Usuário não encontrado\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado (somente ADMIN)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Forbidden",
                                    value = "{\n  \"status\": 403,\n  \"message\": \"Acesso negado\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Unauthorized",
                                    value = "{\n  \"status\": 401,\n  \"message\": \"Token inválido\"\n}"
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/role")
    public ResponseEntity<UsuarioResponseDTO> atualizarRole(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualizar role",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioUpdateRoleDTO.class),
                            examples = @ExampleObject(
                                    name = "Request role",
                                    value = "{\n  \"idUser\": \"2\",\n  \"role\": \"ADMIN\"\n}"
                            )
                    )
            )
            @RequestBody UsuarioUpdateRoleDTO dto) {

        log.info("🛡️ [PATCH] ADMIN solicitou atualização de role do usuário ID {} para {}",
                dto.idUser(), dto.role());

        UsuarioResponseDTO atualizado = service.atualizarRole(dto);

        log.info("✅ Role atualizada com sucesso para usuário ID {}", dto.idUser());

        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Atualizar tipo do usuário", description = "Associa um tipo de usuário a um usuário existente (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário ou tipo não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado (somente ADMIN)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/tipo-usuario")
    public ResponseEntity<UsuarioResponseDTO> atualizarTipoUsuario(
            @Parameter(description = "ID do usuário", example = "2")
            @PathVariable Long id,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualizar tipo do usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioUpdateTipoDTO.class),
                            examples = @ExampleObject(
                                    name = "Request tipo",
                                    value = "{\n  \"tipoUsuarioId\": 1\n}"
                            )
                    )
            )
            @RequestBody UsuarioUpdateTipoDTO dto) {

        log.info("🧩 [PATCH] ADMIN solicitou atualização de tipo do usuário ID {}", id);

        UsuarioResponseDTO atualizado = service.atualizarTipoUsuario(id, dto.tipoUsuarioId());

        log.info("✅ Tipo atualizado com sucesso para usuário ID {}", id);

        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Buscar usuários por nome", description = "Busca usuários pelo nome (parcial) e retorna uma lista (somente ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Lista por nome",
                                    value = "[\n  {\n    \"id\": 2,\n    \"nome\": \"João Victor\",\n    \"email\": \"joao@tech.com\",\n    \"endereco\": \"Rua B, 456\",\n    \"role\": \"CLIENT\"\n  }\n]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetro inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Nome inválido",
                                    value = "{\n  \"status\": 400,\n  \"message\": \"O parâmetro 'nome' é obrigatório.\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Unauthorized",
                                    value = "{\n  \"status\": 401,\n  \"message\": \"Token inválido\"\n}"
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome parcial para buscar usuários", example = "joao")
            @RequestParam String nome) {

        log.info("🔍 [GET] Buscando usuários por nome: {}", nome);


        List<UsuarioResponseDTO> lista = service.buscarPorNome(nome);

        log.info("📄 {} usuários retornados na busca por nome '{}'", lista.size(), nome);

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente (ADMIN ou dono)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário atualizado",
                                    value = "{\n  \"id\": 2,\n  \"nome\": \"João Atualizado\",\n  \"email\": \"joao@tech.com\",\n  \"endereco\": \"Av. Atualizada, 999\",\n  \"role\": \"CLIENT\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro nos dados enviados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Bad Request",
                                    value = "{\n  \"status\": 400,\n  \"message\": \"Dados inválidos\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para atualizar este usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Forbidden",
                                    value = "{\n  \"status\": 403,\n  \"message\": \"Você não tem permissão para acessar ou alterar este usuário\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Not Found",
                                    value = "{\n  \"status\": 404,\n  \"message\": \"Usuário não encontrado\"\n}"
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @Parameter(description = "ID do usuário", example = "2")
            @PathVariable Long id,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioUpdateDTO.class),
                            examples = @ExampleObject(
                                    name = "Request update",
                                    value = "{\n  \"nome\": \"João Atualizado\",\n  \"email\": \"joao@tech.com\",\n  \"endereco\": \"Av. Atualizada, 999\"\n}"
                            )
                    )
            )
            @RequestBody UsuarioUpdateDTO dto) {

        log.info("✏ [PUT] Atualizando usuário ID {}", id);

        UsuarioResponseDTO user = service.buscarPorId(id);

        checkPermission(user.email());

        UsuarioResponseDTO atualizado = service.atualizar(id, dto);

        log.info("✔ Usuário ID {} atualizado com sucesso", id);

        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Atualizar senha do usuário", description = "Atualiza apenas a senha de um usuário (ADMIN ou dono)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Senha atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Senha atual incorreta / dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Senha atual incorreta",
                                            value = "{\n  \"status\": 400,\n  \"message\": \"Senha atual incorreta\"\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Validação",
                                            value = "{\n  \"status\": 400,\n  \"message\": \"Dados inválidos\"\n}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para alterar senha deste usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Forbidden",
                                    value = "{\n  \"status\": 403,\n  \"message\": \"Você não tem permissão para acessar ou alterar este usuário\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Not Found",
                                    value = "{\n  \"status\": 404,\n  \"message\": \"Usuário não encontrado\"\n}"
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> atualizarSenha(
            @Parameter(description = "ID do usuário", example = "2")
            @PathVariable Long id,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioUpdateSenhaDTO.class),
                            examples = @ExampleObject(
                                    name = "Request senha",
                                    value = "{\n  \"senhaAtual\": \"123456\",\n  \"novaSenha\": \"NovaSenha@123\"\n}"
                            )
                    )
            )
            @RequestBody UsuarioUpdateSenhaDTO dto) {

        log.info("🔐 [PATCH] Atualizando senha do usuário ID {}", id);

        UsuarioResponseDTO user = service.buscarPorId(id);

        checkPermission(user.email());

        service.atualizarSenha(id, dto);

        log.info("✔ Senha atualizada para usuário ID {}", id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema (ADMIN ou dono)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário removido com sucesso"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para remover este usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Forbidden",
                                    value = "{\n  \"status\": 403,\n  \"message\": \"Você não tem permissão para acessar ou alterar este usuário\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Not Found",
                                    value = "{\n  \"status\": 404,\n  \"message\": \"Usuário não encontrado\"\n}"
                            )
                    )
            )
    })
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','DONO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do usuário", example = "2")
            @PathVariable Long id) {

        log.warn("🗑 [DELETE] Tentativa de deletar usuário ID {}", id);

        UsuarioResponseDTO user = service.buscarPorId(id);

        checkPermission(user.email());

        service.deletar(id);

        log.info("🗑✔ Usuário ID {} deletado com sucesso!", id);

        return ResponseEntity.ok().build();
    }
}
