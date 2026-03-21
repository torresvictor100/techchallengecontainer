package com.techchallenge.domain.restaurante.controller;

import com.techchallenge.domain.auth.dto.ErrorResponseDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteCreateDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteResponseDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteUpdateDTO;
import com.techchallenge.domain.restaurante.service.RestauranteService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/restaurantes")
@Tag(name = "Restaurantes", description = "Endpoints de cadastro de restaurantes")
@SecurityRequirement(name = "bearerAuth")
public class RestauranteController {

    private static final Logger log = LoggerFactory.getLogger(RestauranteController.class);

    private final RestauranteService service;

    @Autowired
    public RestauranteController(RestauranteService service) {
        this.service = service;
    }

    @Operation(summary = "Listar restaurantes", description = "Retorna todos os restaurantes (requer autenticação)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Lista de restaurantes",
                                    value = "[\n  {\n    \"id\": 1,\n    \"nome\": \"Cantina da Praca\",\n    \"endereco\": \"Rua Central, 100 - Recife\",\n    \"tipoCozinha\": \"Italiana\",\n    \"horarioFuncionamento\": \"Seg-Dom 11:00-23:00\",\n    \"donoId\": 1,\n    \"donoNome\": \"Administrador\",\n    \"donoEmail\": \"admin2@tech.com\"\n  }\n]"
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
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listarTodos() {
        log.info("📌 [GET] Listando restaurantes");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar restaurante por ID", description = "Retorna um restaurante específico (requer autenticação)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurante encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestauranteResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Restaurante encontrado",
                                    value = "{\n  \"id\": 1,\n  \"nome\": \"Cantina da Praca\",\n  \"endereco\": \"Rua Central, 100 - Recife\",\n  \"tipoCozinha\": \"Italiana\",\n  \"horarioFuncionamento\": \"Seg-Dom 11:00-23:00\",\n  \"donoId\": 1,\n  \"donoNome\": \"Administrador\",\n  \"donoEmail\": \"admin2@tech.com\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(
            @Parameter(description = "ID do restaurante", example = "1")
            @PathVariable Long id) {

        log.info("🔍 [GET] Buscando restaurante ID {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Criar restaurante", description = "Cria um novo restaurante (requer autenticação)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurante criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestauranteResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Restaurante criado",
                                    value = "{\n  \"id\": 10,\n  \"nome\": \"Cantina da Praca\",\n  \"endereco\": \"Rua Central, 100 - Recife\",\n  \"tipoCozinha\": \"Italiana\",\n  \"horarioFuncionamento\": \"Seg-Dom 11:00-23:00\",\n  \"donoId\": 2,\n  \"donoNome\": \"Joao\",\n  \"donoEmail\": \"joao@tech.com\"\n}"
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
                    description = "Dono do restaurante não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> criar(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestauranteCreateDTO.class),
                            examples = @ExampleObject(
                                    name = "Request",
                                    value = "{\n  \"nome\": \"Cantina da Praca\",\n  \"endereco\": \"Rua Central, 100 - Recife\",\n  \"tipoCozinha\": \"Italiana\",\n  \"horarioFuncionamento\": \"Seg-Dom 11:00-23:00\",\n  \"donoId\": 2\n}"
                            )
                    )
            )
            @RequestBody RestauranteCreateDTO dto) {

        log.info("📝 [POST] Criando restaurante {}", dto.nome());
        return ResponseEntity.ok(service.criar(dto));
    }

    @Operation(summary = "Atualizar restaurante", description = "Atualiza um restaurante existente (requer autenticação)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurante atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestauranteResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Restaurante atualizado",
                                    value = "{\n  \"id\": 1,\n  \"nome\": \"Cantina Atualizada\",\n  \"endereco\": \"Av. Nova, 200 - Recife\",\n  \"tipoCozinha\": \"Brasileira\",\n  \"horarioFuncionamento\": \"Seg-Sex 10:00-22:00\",\n  \"donoId\": 2,\n  \"donoNome\": \"Joao\",\n  \"donoEmail\": \"joao@tech.com\"\n}"
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
                    description = "Restaurante ou dono não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizar(
            @Parameter(description = "ID do restaurante", example = "1")
            @PathVariable Long id,
            @Valid
            @RequestBody RestauranteUpdateDTO dto) {

        log.info("✏ [PUT] Atualizando restaurante ID {}", id);
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Deletar restaurante", description = "Remove um restaurante (requer autenticação)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante removido com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do restaurante", example = "1")
            @PathVariable Long id) {

        log.info("🗑 [DELETE] Deletando restaurante ID {}", id);
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
