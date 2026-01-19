package com.techchallenge.domain.auth.service;

import com.techchallenge.domain.auth.dto.LoginRequestDTO;
import com.techchallenge.domain.auth.dto.LoginResponseDTO;
import com.techchallenge.domain.auth.dto.RefreshTokenResponseDTO;
import com.techchallenge.domain.auth.dto.UserInfoDTO;
import com.techchallenge.domain.auth.exception.InvalidPasswordException;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Value("${app.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${app.auth.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Autowired
    public AuthService(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {

        log.info("🔐 Tentativa de login para email: {}", dto.email());

        Usuario usuario = usuarioService.buscarPorEmail(dto.email());

        if (!passwordEncoder.matches(dto.password(), usuario.getSenha())) {
            log.warn("❌ Senha inválida para {}", dto.email());
            throw new InvalidPasswordException("Usuário ou senha inválidos");
        }

        String token = generateJwtToken(usuario);

        log.info("✅ Login bem sucedido para {}", usuario.getEmail());

        return new LoginResponseDTO("ok", "logged", token);
    }

    public RefreshTokenResponseDTO refreshToken(String refreshToken) {

        log.info("♻️ Tentando renovar refresh token...");

        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            log.info("♻️ Token renovado para usuário {} com role {}", email, role);

            String newAccess = generateJwtToken(email, role);
            String newRefresh = generateJwtToken(email, role);

            return new RefreshTokenResponseDTO(newAccess, newRefresh);

        } catch (Exception e) {
            log.error("⛔ Refresh token inválido: {}", e.getMessage());
            throw new InvalidPasswordException("Refresh token inválido ou expirado");
        }
    }

    public UserInfoDTO getUserInfo(String authHeader) {

        log.debug("📌 Solicitando dados do usuário autenticado...");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("⚠️ Token ausente / malformado");
            throw new InvalidPasswordException("Token inválido");
        }

        String token = authHeader.substring(7);
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            String issuedAt = claims.getIssuedAt().toString();
            String expiresAt = claims.getExpiration().toString();

            log.info("👤 Info do usuário retornada: {} ({})", email, role);

            Usuario usuario = usuarioService.buscarPorEmail(email);

            return new UserInfoDTO(email, issuedAt, expiresAt, role, usuario.getId()
                    , usuario.getNome(), usuario.getEndereco());

        } catch (Exception e) {
            log.error("❌ Erro ao processar token: {}", e.getMessage());
            throw new InvalidPasswordException("Token inválido");
        }
    }


    private String generateJwtToken(Usuario usuario) {
        return generateJwtToken(usuario.getEmail(), usuario.getRole().name());
    }

    private String generateJwtToken(String email, String role) {
        log.debug("🔑 Gerando token JWT para {} com role {}", email, role);

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
