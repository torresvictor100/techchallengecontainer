package com.techchallenge.domain.auth.controller;

import com.techchallenge.domain.auth.dto.ErrorResponseDTO;
import com.techchallenge.domain.auth.dto.LoginRequestDTO;
import com.techchallenge.domain.auth.dto.LoginResponseDTO;
import com.techchallenge.domain.auth.dto.UserInfoDTO;
import com.techchallenge.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de login e geração de token JWT")
public class AuthController {

    private final AuthService authService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza o login e retorna um token JWT caso as credenciais estejam corretas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Login OK",
                                    value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiJ9...\",\n  \"refreshToken\": \"\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Login inválido",
                                    value = "{\n  \"status\": 401,\n  \"message\": \"Senha inválida\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Email não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Email não encontrado",
                                    value = "{\n  \"status\": 404,\n  \"message\": \"Email não encontrado\"\n}"
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de login",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Login Request",
                                    value = "{\n  \"email\": \"admin@tech.com\",\n  \"password\": \"123456\"\n}"
                            )
                    )
            )
            @RequestBody LoginRequestDTO dto
    ) {

        log.info("🔐 Tentativa de login para o email: {}", dto.email());

        LoginResponseDTO response = authService.login(dto);

        log.info("✅ Login bem-sucedido para: {}", dto.email());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obter informações do usuário autenticado",
            description = "Lê o token JWT e retorna dados do usuário logado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoDTO.class),
                            examples = @ExampleObject(
                                    name = "Me OK",
                                    value = "{\n  \"idUser\": 1,\n  \"email\": \"admin@tech.com\",\n  \"nome\": \"Admin\",\n  \"role\": \"ADMIN\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token inválido ou expirado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Token inválido",
                                            value = "{\n  \"status\": 401,\n  \"message\": \"Token inválido\"\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Token expirado",
                                            value = "{\n  \"status\": 401,\n  \"message\": \"Token expirado\"\n}"
                                    )
                            }
                    )
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getUserInfo(
            @RequestHeader("Authorization")
            @Parameter(
                    description = "Bearer token JWT",
                    example = "Bearer eyJhbGciOiJIUzI1NiJ9..."
            )
            String token
    ) {

        log.info("👤 Solicitando informações do usuário autenticado");

        UserInfoDTO info = authService.getUserInfo(token);

        log.info("📌 Dados do usuário retornados com sucesso: {}", info.email());

        return ResponseEntity.ok(info);
    }
}