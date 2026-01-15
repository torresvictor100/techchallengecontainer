package com.techchallenge.modules.auth.controller;

import com.techchallenge.modules.auth.dto.*;
import com.techchallenge.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de login e geração de token JWT")
public class AuthController {

    @Value("${app.auth.email}")
    private String configuredEmail;

    @Value("${app.auth.password}")
    private String configuredPassword;

    private AuthService authService;

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
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {

        log.info("🔐 Tentativa de login para o email: {}", dto.email());

        LoginResponseDTO response = authService.login(dto);

        log.info("✅ Login bem-sucedido para: {}", dto.email());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Renovar token JWT",
            description = "Recebe o refresh token e retorna novos tokens gerados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido")
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO dto) {

        log.info("♻️  Solicitada renovação do token");

        RefreshTokenResponseDTO response = authService.refreshToken(dto.refreshToken());

        log.info("🔁 Novo token JWT gerado com sucesso");

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obter informações do usuário autenticado",
            description = "Lê o token JWT e retorna dados do usuário logado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
    })
    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getUserInfo(@RequestHeader("Authorization") String token) {

        log.info("👤 Solicitando informações do usuário autenticado");

        UserInfoDTO info = authService.getUserInfo(token);

        log.info("📌 Dados do usuário retornados com sucesso: {}", info.email());

        return ResponseEntity.ok(info);
    }

}
