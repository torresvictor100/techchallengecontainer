package com.techchallenge.domain.tipousuario.controller;

import com.techchallenge.domain.auth.dto.ErrorResponseDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioCreateDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioResponseDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioUpdateDTO;
import com.techchallenge.domain.tipousuario.service.TipoUsuarioService;
import com.techchallenge.domain.usuario.dto.UsuarioUpdateTipoEmailDTO;
import com.techchallenge.domain.usuario.dto.UsuarioResponseDTO;
import com.techchallenge.domain.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/tipos-usuario")
@Tag(name = "Tipos de Usuário", description = "Endpoints para gerenciamento de tipos de usuário")
@SecurityRequirement(name = "bearerAuth")
public class TipoUsuarioController {

    private static final Logger log = LoggerFactory.getLogger(TipoUsuarioController.class);

    private final TipoUsuarioService service;
    private final UsuarioService usuarioService;

    @Autowired
    public TipoUsuarioController(TipoUsuarioService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar tipos de usuario", description = "Retorna todos os tipos cadastrados (requer autenticacao)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Lista de tipos",
                                    value = "[\n  {\n    \"id\": 1,\n    \"nome\": \"Cliente\"\n  },\n  {\n    \"id\": 2,\n    \"nome\": \"Dono de Restaurante\"\n  }\n]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<TipoUsuarioResponseDTO>> listarTodos() {
        log.info("📌 [GET] Listando tipos de usuário");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar tipo de usuario por ID", description = "Retorna um tipo especifico (requer autenticacao)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoUsuarioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Tipo encontrado",
                                    value = "{\n  \"id\": 1,\n  \"nome\": \"Cliente\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> buscarPorId(
            @Parameter(description = "ID do tipo de usuário", example = "1")
            @PathVariable Long id) {

        log.info("🔍 [GET] Buscando tipo de usuário ID {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar usuários por tipo (ID)", description = "Lista usuários de um tipo específico (somente ADMIN)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Usuários do tipo (ID)",
                                    value = "[\n  {\n    \"id\": 3,\n    \"nome\": \"Maria\",\n    \"email\": \"maria@tech.com\",\n    \"endereco\": \"Rua C, 789\",\n    \"role\": \"CLIENT\"\n  }\n]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuariosPorTipoId(
            @Parameter(description = "ID do tipo de usuário", example = "1")
            @PathVariable Long id) {

        log.info("🔍 [GET] Buscando usuários por tipo ID: {}", id);
        List<UsuarioResponseDTO> lista = usuarioService.buscarPorTipo(id);
        log.info("📄 {} usuários retornados para o tipo ID {}", lista.size(), id);

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar usuários por nome do tipo", description = "Lista usuários pelo nome do tipo (somente ADMIN)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Usuários do tipo (nome)",
                                    value = "[\n  {\n    \"id\": 4,\n    \"nome\": \"Pedro\",\n    \"email\": \"pedro@tech.com\",\n    \"endereco\": \"Rua D, 321\",\n    \"role\": \"CLIENT\"\n  }\n]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetro inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuariosPorTipoNome(
            @Parameter(description = "Nome do tipo de usuário", example = "Cliente")
            @RequestParam String tipoNome) {

        log.info("🔍 [GET] Buscando usuários por tipo nome: {}", tipoNome);
        List<UsuarioResponseDTO> lista = usuarioService.buscarPorTipoNome(tipoNome);
        log.info("📄 {} usuários retornados para o tipo nome {}", lista.size(), tipoNome);

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Atualizar tipo do usuário por email", description = "Atualiza o tipo de um usuário informando o email (somente ADMIN)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuario atualizado",
                                    value = "{\n  \"id\": 2,\n  \"nome\": \"Joao\",\n  \"email\": \"joao@tech.com\",\n  \"endereco\": \"Rua B, 456\",\n  \"role\": \"CLIENT\",\n  \"tipoUsuario\": {\n    \"id\": 1,\n    \"nome\": \"Cliente\"\n  }\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário ou tipo não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/usuarios/tipo")
    public ResponseEntity<UsuarioResponseDTO> atualizarTipoUsuarioPorEmail(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualizar tipo do usuário por email",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioUpdateTipoEmailDTO.class),
                            examples = @ExampleObject(
                                    name = "Request tipo por email",
                                    value = "{\n  \"email\": \"joao@tech.com\",\n  \"tipoUsuarioId\": 1\n}"
                            )
                    )
            )
            @RequestBody UsuarioUpdateTipoEmailDTO dto) {

        log.info("🧩 [PATCH] ADMIN solicitou atualização de tipo do usuário por email {}", dto.email());

        UsuarioResponseDTO atualizado = usuarioService.atualizarTipoUsuarioPorEmail(dto.email(), dto.tipoUsuarioId());

        log.info("✅ Tipo atualizado com sucesso para o usuário {}", dto.email());

        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Criar tipo de usuário", description = "Cria um novo tipo (somente ADMIN)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoUsuarioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Tipo criado",
                                    value = "{\n  \"id\": 10,\n  \"nome\": \"NovoTipo\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos ou tipo duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TipoUsuarioResponseDTO> criar(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoUsuarioCreateDTO.class),
                            examples = @ExampleObject(
                                    name = "Request",
                                    value = "{\n  \"nome\": \"NovoTipo\"\n}"
                            )
                    )
            )
            @RequestBody TipoUsuarioCreateDTO dto) {

        log.info("📝 [POST] Criando tipo de usuário {}", dto.nome());
        return ResponseEntity.ok(service.criar(dto));
    }

    @Operation(summary = "Atualizar tipo de usuário", description = "Atualiza um tipo existente (somente ADMIN)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoUsuarioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Tipo atualizado",
                                    value = "{\n  \"id\": 1,\n  \"nome\": \"ClienteAtualizado\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos ou tipo duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> atualizar(
            @Parameter(description = "ID do tipo de usuário", example = "1")
            @PathVariable Long id,
            @Valid
            @RequestBody TipoUsuarioUpdateDTO dto) {

        log.info("✏ [PUT] Atualizando tipo de usuário ID {}", id);
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Deletar tipo de usuário", description = "Remove um tipo (somente ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo removido com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do tipo de usuário", example = "1")
            @PathVariable Long id) {

        log.info("🗑 [DELETE] Deletando tipo de usuário ID {}", id);
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
