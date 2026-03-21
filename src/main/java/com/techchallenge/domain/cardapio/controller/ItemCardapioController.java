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

    @Operation(summary = "Listar itens do cardapio", description = "Retorna todos os itens do cardapio (requer autenticacao)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Lista de itens",
                                    value = "[\n  {\n    \"id\": 1,\n    \"nome\": \"Lasanha da Praca\",\n    \"descricao\": \"Lasanha com molho da casa\",\n    \"preco\": 29.90,\n    \"somenteNoRestaurante\": true,\n    \"fotoPath\": \"/imagens/lasanha-praca.jpg\",\n    \"restauranteId\": 1,\n    \"restauranteNome\": \"Cantina da Praca\"\n  }\n]"
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
    public ResponseEntity<List<ItemCardapioResponseDTO>> listarTodos() {
        log.info("📌 [GET] Listando itens do cardápio");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar item do cardapio por ID", description = "Retorna um item especifico (requer autenticacao)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemCardapioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Item encontrado",
                                    value = "{\n  \"id\": 1,\n  \"nome\": \"Lasanha da Praca\",\n  \"descricao\": \"Lasanha com molho da casa\",\n  \"preco\": 29.90,\n  \"somenteNoRestaurante\": true,\n  \"fotoPath\": \"/imagens/lasanha-praca.jpg\",\n  \"restauranteId\": 1,\n  \"restauranteNome\": \"Cantina da Praca\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
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

    @Operation(summary = "Criar item do cardapio", description = "Cria um novo item (requer autenticacao)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemCardapioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Item criado",
                                    value = "{\n  \"id\": 10,\n  \"nome\": \"Baiao de Dois\",\n  \"descricao\": \"Baiao de dois tradicional\",\n  \"preco\": 24.90,\n  \"somenteNoRestaurante\": true,\n  \"fotoPath\": \"/imagens/baiao-de-dois.jpg\",\n  \"restauranteId\": 2,\n  \"restauranteNome\": \"Sabor Nordestino\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
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
                                    value = "{\n  \"nome\": \"Lasanha da Praca\",\n  \"descricao\": \"Lasanha com molho da casa\",\n  \"preco\": 29.90,\n  \"somenteNoRestaurante\": true,\n  \"fotoPath\": \"/imagens/lasanha-praca.jpg\",\n  \"restauranteId\": 1\n}"
                            )
                    )
            )
            @RequestBody ItemCardapioCreateDTO dto) {

        log.info("📝 [POST] Criando item do cardápio {}", dto.nome());
        return ResponseEntity.ok(service.criar(dto));
    }

    @Operation(summary = "Atualizar item do cardapio", description = "Atualiza um item existente (requer autenticacao)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemCardapioResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Item atualizado",
                                    value = "{\n  \"id\": 1,\n  \"nome\": \"Lasanha Especial\",\n  \"descricao\": \"Lasanha com molho artesanal\",\n  \"preco\": 34.90,\n  \"somenteNoRestaurante\": false,\n  \"fotoPath\": \"/imagens/lasanha-especial.jpg\",\n  \"restauranteId\": 1,\n  \"restauranteNome\": \"Cantina da Praca\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item ou restaurante nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
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

    @Operation(summary = "Deletar item do cardapio", description = "Remove um item (requer autenticacao)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removido com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente ou invalido",
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
