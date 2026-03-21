package com.techchallenge.domain.tipousuario.controller;

import com.techchallenge.domain.auth.dto.ErrorResponseDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioCreateDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioResponseDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioUpdateDTO;
import com.techchallenge.domain.tipousuario.service.TipoUsuarioService;
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

    @Autowired
    public TipoUsuarioController(TipoUsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar tipos de usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<TipoUsuarioResponseDTO>> listarTodos() {
        log.info("📌 [GET] Listando tipos de usuário");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar tipo de usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo não encontrado",
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

    @Operation(summary = "Criar tipo de usuário", description = "Cria um novo tipo (somente ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo criado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
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
                                    value = "{\n  \"nome\": \"Cliente\"\n}"
                            )
                    )
            )
            @RequestBody TipoUsuarioCreateDTO dto) {

        log.info("📝 [POST] Criando tipo de usuário {}", dto.nome());
        return ResponseEntity.ok(service.criar(dto));
    }

    @Operation(summary = "Atualizar tipo de usuário", description = "Atualiza um tipo existente (somente ADMIN)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo atualizado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo não encontrado",
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
