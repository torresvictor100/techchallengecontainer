package com.techchallenge.modules.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.modules.auth.dto.ErrorResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Set;

@Component
public class AuthFilter extends HttpFilter {

    @Value("${app.auth.jwtSecret}")
    private String jwtSecret;

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/v1/api/auth/login",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars/",
            "/v1/api/usuarios/registrar"
    );

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        log.debug("🔎 Incoming request: [{}] {}", method, path);

        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }

        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response, "Token ausente ou mal formatado. Use: Authorization: Bearer <token>");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            request.setAttribute("email", email);

            String role = claims.get("role", String.class);
            request.setAttribute("role", role);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            token,
                            Collections.singleton(() -> "ROLE_" + role)
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.warn("⛔ Token expirado! Path: {}", path);
            sendUnauthorized(response, "Token expirado. Faça login novamente.");

        } catch (Exception e) {
            log.error("❌ Erro ao validar token no path {} | Motivo: {}", path, e.getMessage());
            sendUnauthorized(response, "Token inválido.");
        }
    }

    private boolean isPublic(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        log.warn("🔒 Acesso negado: {}", message);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorResponseDTO error = new ErrorResponseDTO(401, message);
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}
