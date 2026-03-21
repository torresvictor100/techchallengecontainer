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

    @Operation(summary = "Listar restaurantes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listarTodos() {
        log.info("📌 [GET] Listando restaurantes");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar restaurante por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
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

    @Operation(summary = "Criar restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante criado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
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
                                    value = "{\n  \"nome\": \"Cantina da Praça\",\n  \"endereco\": \"Rua Central, 100\",\n  \"tipoCozinha\": \"Italiana\",\n  \"horarioFuncionamento\": \"Seg-Dom 11:00-23:00\",\n  \"donoId\": 2\n}"
                            )
                    )
            )
            @RequestBody RestauranteCreateDTO dto) {

        log.info("📝 [POST] Criando restaurante {}", dto.nome());
        return ResponseEntity.ok(service.criar(dto));
    }

    @Operation(summary = "Atualizar restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
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

    @Operation(summary = "Deletar restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante removido com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
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
