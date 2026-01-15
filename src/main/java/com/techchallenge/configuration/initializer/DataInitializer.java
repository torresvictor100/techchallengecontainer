package com.techchallenge.configuration.initializer;

import com.techchallenge.modules.usuario.entity.Usuario;
import com.techchallenge.modules.usuario.entity.UsuarioRole;
import com.techchallenge.modules.usuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner createDefaultUser(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {

            criarAdminComSha256(repository, encoder,
                    "admin2@tech.com", "123456");

            criarAdminSemSha256(repository, encoder,
                    "admin@tech.com", "123456");
        };
    }

    private void criarAdminComSha256(UsuarioRepository repository, PasswordEncoder encoder,
                                     String email, String senhaPura) {

        if (repository.existsByEmail(email)) {
            System.out.println("⚠ Admin (SHA256) já existe: " + email);
            return;
        }

        String senhaSHA256 = sha256(senhaPura);
        String senhaFinal = encoder.encode(senhaSHA256);

        Usuario admin = novoUsuario(
                "Administrador",
                email,
                senhaFinal
        );

        repository.save(admin);

        System.out.println("✅ Admin criado com SHA-256 + BCrypt: " + email);
    }

    private void criarAdminSemSha256(UsuarioRepository repository, PasswordEncoder encoder,
                                     String email, String senhaPura) {

        if (repository.existsByEmail(email)) {
            System.out.println("⚠ Admin (sem SHA256) já existe: " + email);
            return;
        }

        String senhaFinal = encoder.encode(senhaPura);

        Usuario admin = novoUsuario(
                "Administrador (Legacy)",
                email,
                senhaFinal
        );

        repository.save(admin);

        System.out.println("⚠ Admin criado sem SHA-256 (somente BCrypt): " + email);
    }

    private Usuario novoUsuario(String nome, String email, String senha) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(senha);
        u.setEndereco("Sistema interno");
        u.setUltimaAtualizacao(LocalDateTime.now());
        u.setRole(UsuarioRole.ADMIN);
        return u;
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar SHA-256", e);
        }
    }
}
