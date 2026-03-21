package com.techchallenge.configuration.initializer;

import com.techchallenge.domain.cardapio.entity.ItemCardapio;
import com.techchallenge.domain.cardapio.repository.ItemCardapioRepository;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.restaurante.repository.RestauranteRepository;
import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.entity.UsuarioRole;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner createDefaultUser(UsuarioRepository repository,
                                               PasswordEncoder encoder,
                                               TipoUsuarioRepository tipoUsuarioRepository,
                                               RestauranteRepository restauranteRepository,
                                               ItemCardapioRepository itemCardapioRepository) {
        return args -> {

            TipoUsuario donoRestaurante = criarTipoUsuarioSeNaoExiste(tipoUsuarioRepository, "Dono de Restaurante");
            criarTipoUsuarioSeNaoExiste(tipoUsuarioRepository, "Cliente");
            
            criarAdminComSha256(repository, encoder,
                    "admin2@tech.com", "123456", donoRestaurante);

            criarAdminSemSha256(repository, encoder,
                    "admin@tech.com", "123456", donoRestaurante);

            Usuario adminSha = repository.findByEmail("admin2@tech.com").orElse(null);
            Usuario adminLegacy = repository.findByEmail("admin@tech.com").orElse(null);

            criarRestauranteSeNaoExiste(restauranteRepository, adminSha,
                    "Cantina da Praca",
                    "Rua Central, 100 - Recife",
                    "Italiana",
                    "Seg-Dom 11:00-23:00");

            criarRestauranteSeNaoExiste(restauranteRepository, adminLegacy,
                    "Sabor Nordestino",
                    "Av. Recife, 500 - Recife",
                    "Nordestina",
                    "Seg-Sab 11:00-22:00");

            Restaurante praca = restauranteRepository.findByNomeIgnoreCase("Cantina da Praca").orElse(null);
            Restaurante nordestino = restauranteRepository.findByNomeIgnoreCase("Sabor Nordestino").orElse(null);

            criarItemCardapioSeNaoExiste(itemCardapioRepository, praca,
                    "Lasanha da Praca",
                    "Lasanha com molho da casa",
                    new BigDecimal("29.90"),
                    true,
                    "/imagens/lasanha-praca.jpg");

            criarItemCardapioSeNaoExiste(itemCardapioRepository, nordestino,
                    "Baiao de Dois",
                    "Baiao de dois tradicional",
                    new BigDecimal("24.90"),
                    true,
                    "/imagens/baiao-de-dois.jpg");
        };
    }

    private TipoUsuario criarTipoUsuarioSeNaoExiste(TipoUsuarioRepository repository, String nome) {
        return repository.findByNomeIgnoreCase(nome)
                .orElseGet(() -> {
                    TipoUsuario tipo = new TipoUsuario();
                    tipo.setNome(nome);
                    return repository.save(tipo);
                });
    }

    private void criarAdminComSha256(UsuarioRepository repository, PasswordEncoder encoder,
                                     String email, String senhaPura, TipoUsuario tipoUsuario) {

        if (repository.existsByEmail(email)) {
            atualizarTipoSeNecessario(repository, email, tipoUsuario);
            System.out.println("⚠ Admin (SHA256) já existe: " + email);
            return;
        }

        String senhaSHA256 = sha256(senhaPura);
        String senhaFinal = encoder.encode(senhaSHA256);

        Usuario admin = novoUsuario(
                "Administrador",
                email,
                senhaFinal,
                tipoUsuario
        );

        repository.save(admin);

        System.out.println("✅ Admin criado com SHA-256 + BCrypt: " + email);
    }

    private void criarAdminSemSha256(UsuarioRepository repository, PasswordEncoder encoder,
                                     String email, String senhaPura, TipoUsuario tipoUsuario) {

        if (repository.existsByEmail(email)) {
            atualizarTipoSeNecessario(repository, email, tipoUsuario);
            System.out.println("⚠ Admin (sem SHA256) já existe: " + email);
            return;
        }

        String senhaFinal = encoder.encode(senhaPura);

        Usuario admin = novoUsuario(
                "Administrador (Legacy)",
                email,
                senhaFinal,
                tipoUsuario
        );

        repository.save(admin);

        System.out.println("⚠ Admin criado sem SHA-256 (somente BCrypt): " + email);
    }

    private Usuario novoUsuario(String nome, String email, String senha, TipoUsuario tipoUsuario) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(senha);
        u.setEndereco("Sistema interno");
        u.setUltimaAtualizacao(LocalDateTime.now());
        u.setRole(UsuarioRole.ADMIN);
        u.setTipoUsuario(tipoUsuario);
        return u;
    }

    private void atualizarTipoSeNecessario(UsuarioRepository repository, String email, TipoUsuario tipoUsuario) {
        repository.findByEmail(email).ifPresent(usuario -> {
            if (tipoUsuario != null && (usuario.getTipoUsuario() == null
                    || !usuario.getTipoUsuario().getId().equals(tipoUsuario.getId()))) {
                usuario.setTipoUsuario(tipoUsuario);
                usuario.setUltimaAtualizacao(LocalDateTime.now());
                repository.save(usuario);
                System.out.println("✅ Tipo atualizado para o usuário: " + email);
            }
        });
    }

    private void criarRestauranteSeNaoExiste(RestauranteRepository repository, Usuario dono,
                                             String nome, String endereco, String tipoCozinha,
                                             String horarioFuncionamento) {
        if (dono == null) {
            System.out.println("⚠ Dono não encontrado para criar restaurante: " + nome);
            return;
        }

        repository.findByNomeIgnoreCase(nome)
                .ifPresentOrElse(restaurante -> {
                    if (restaurante.getDono() == null
                            || !restaurante.getDono().getId().equals(dono.getId())) {
                        restaurante.setDono(dono);
                        repository.save(restaurante);
                        System.out.println("✅ Dono atualizado para restaurante: " + nome);
                    }
                }, () -> {
                    Restaurante novo = new Restaurante();
                    novo.setNome(nome);
                    novo.setEndereco(endereco);
                    novo.setTipoCozinha(tipoCozinha);
                    novo.setHorarioFuncionamento(horarioFuncionamento);
                    novo.setDono(dono);

                    repository.save(novo);
                    System.out.println("✅ Restaurante criado: " + nome);
                });
    }

    private void criarItemCardapioSeNaoExiste(ItemCardapioRepository repository, Restaurante restaurante,
                                              String nome, String descricao, BigDecimal preco,
                                              boolean somenteNoRestaurante, String fotoPath) {
        if (restaurante == null) {
            System.out.println("⚠ Restaurante não encontrado para criar item: " + nome);
            return;
        }

        repository.findByNomeIgnoreCase(nome)
                .ifPresentOrElse(item -> {
                    boolean changed = false;

                    if (item.getRestaurante() == null
                            || !item.getRestaurante().getId().equals(restaurante.getId())) {
                        item.setRestaurante(restaurante);
                        changed = true;
                    }
                    if (!nome.equals(item.getNome())) {
                        item.setNome(nome);
                        changed = true;
                    }
                    if (!descricao.equals(item.getDescricao())) {
                        item.setDescricao(descricao);
                        changed = true;
                    }
                    if (item.getPreco() == null || item.getPreco().compareTo(preco) != 0) {
                        item.setPreco(preco);
                        changed = true;
                    }
                    if (item.getSomenteNoRestaurante() == null
                            || item.getSomenteNoRestaurante() != somenteNoRestaurante) {
                        item.setSomenteNoRestaurante(somenteNoRestaurante);
                        changed = true;
                    }
                    if (!fotoPath.equals(item.getFotoPath())) {
                        item.setFotoPath(fotoPath);
                        changed = true;
                    }

                    if (changed) {
                        repository.save(item);
                        System.out.println("✅ Item atualizado: " + nome);
                    }
                }, () -> {
                    ItemCardapio novo = new ItemCardapio();
                    novo.setNome(nome);
                    novo.setDescricao(descricao);
                    novo.setPreco(preco);
                    novo.setSomenteNoRestaurante(somenteNoRestaurante);
                    novo.setFotoPath(fotoPath);
                    novo.setRestaurante(restaurante);

                    repository.save(novo);
                    System.out.println("✅ Item criado: " + nome);
                });
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
