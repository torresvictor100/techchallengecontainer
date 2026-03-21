package com.techchallenge.domain.cardapio.controller;

import com.techchallenge.domain.auth.dto.ErrorResponseDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioCreateDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioResponseDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioUpdateDTO;
import com.techchallenge.domain.cardapio.service.ItemCardapioService;
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
@RequestMapping("/v1/api/itens-cardapio")
@Tag(name = "Itens do Cardápio", description = "Endpoints de cadastro de itens do cardápio")
@SecurityRequirement(name = "bearerAuth")
public class ItemCardapioController {

    private static final Logger log = LoggerFactory.getLogger(ItemCardapioController.class);

    private final ItemCardapioService service;

    @Autowired
    public ItemCardapioController(ItemCardapioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar itens do cardápio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ItemCardapioResponseDTO>> listarTodos() {
        log.info("📌 [GET] Listando itens do cardápio");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar item do cardápio por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemCardapioResponseDTO> buscarPorId(
            @Parameter(description = "ID do item", example = "1")
            @PathVariable Long id) {

        log.info("🔍 [GET] Buscando item do cardápio ID {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Criar item do cardápio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item criado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping
    public ResponseEntity<ItemCardapioResponseDTO> criar(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemCardapioCreateDTO.class),
                            examples = @ExampleObject(
                                    name = "Request",
                                    value = "{\n  \"nome\": \"Lasanha\",\n  \"descricao\": \"Lasanha à bolonhesa\",\n  \"preco\": 29.90,\n  \"somenteNoRestaurante\": true,\n  \"fotoPath\": \"/imagens/lasanha.jpg\",\n  \"restauranteId\": 1\n}"
                            )
                    )
            )
            @RequestBody ItemCardapioCreateDTO dto) {

        log.info("📝 [POST] Criando item do cardápio {}", dto.nome());
        return ResponseEntity.ok(service.criar(dto));
    }

    @Operation(summary = "Atualizar item do cardápio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ItemCardapioResponseDTO> atualizar(
            @Parameter(description = "ID do item", example = "1")
            @PathVariable Long id,
            @Valid
            @RequestBody ItemCardapioUpdateDTO dto) {

        log.info("✏ [PUT] Atualizando item do cardápio ID {}", id);
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Deletar item do cardápio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removido com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do item", example = "1")
            @PathVariable Long id) {

        log.info("🗑 [DELETE] Deletando item do cardápio ID {}", id);
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
