package com.techchallenge.modules.usuario.service;

import com.techchallenge.modules.usuario.exception.InvalidRoleException;
import com.techchallenge.modules.usuario.dto.*;
import com.techchallenge.modules.usuario.entity.Usuario;
import com.techchallenge.modules.usuario.entity.UsuarioRole;
import com.techchallenge.modules.usuario.factory.UsuarioFactory;
import com.techchallenge.modules.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponseDTO> listarTodos() {

        log.info("📌 Iniciando listagem de todos os usuários...");

        List<UsuarioResponseDTO> lista = repository.findAll()
                .stream()
                .map(UsuarioFactory::toResponseDTO)
                .toList();

        log.info("📄 {} usuários encontrados.", lista.size());

        return lista;
    }

    public Usuario buscarPorEmail(String email) {

        log.info("🔍 Buscando usuário pelo Email: {}", email);

        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("❌ Usuário com ID {} não encontrado!", email);
                    return new EntityNotFoundException("Usuário não encontrado");
                });

        log.info("✔ Usuário encontrado: {}", usuario.getEmail());

        return usuario;
    }

    public UsuarioResponseDTO buscarPorId(Long id) {

        log.info("🔍 Buscando usuário pelo ID: {}", id);

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("❌ Usuário com ID {} não encontrado!", id);
                    return new EntityNotFoundException("Usuário não encontrado");
                });

        log.info("✔ Usuário encontrado: {}", usuario.getEmail());

        return UsuarioFactory.toResponseDTO(usuario);
    }

    public List<UsuarioResponseDTO> buscarPorNome(String nome) {

        log.info("🔎 Buscando usuários pelo nome contendo: {}", nome);

        if (nome == null || nome.trim().isEmpty()) {
            log.warn("⚠ Nome vazio enviado na busca!");
            throw new IllegalArgumentException("O parâmetro 'nome' é obrigatório.");
        }

        List<UsuarioResponseDTO> usuarios = repository.findByNomeContainingIgnoreCase(nome.trim())
                .stream()
                .map(UsuarioFactory::toResponseDTO)
                .toList();

        log.info("✅ {} usuários encontrados para o nome: {}", usuarios.size(), nome);

        return usuarios;
    }


    public UsuarioResponseDTO criar(UsuarioCreateDTO dto) {

        log.info("📝 Criando novo usuário com email: {}", dto.email());

        if (repository.existsByEmail(dto.email())) {
            log.warn("⚠ Tentativa de criar usuário com email já existente: {}", dto.email());
            throw new IllegalArgumentException("Email já está em uso.");
        }

        Usuario novo = UsuarioFactory.fromCreateDTO(dto);
        novo.setSenha(passwordEncoder.encode(dto.senha()));

        Usuario salvo = repository.save(novo);

        log.info("✅ Usuário criado com sucesso! ID: {}, Email: {}", salvo.getId(), salvo.getEmail());

        return UsuarioFactory.toResponseDTO(salvo);
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO dto) {

        log.info("✏ Atualizando usuário ID: {}", id);

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("❌ Usuário com ID {} não encontrado para atualização!", id);
                    return new EntityNotFoundException("Usuário não encontrado");
                });

        UsuarioFactory.applyUpdate(usuario, dto);

        Usuario atualizado = repository.save(usuario);

        log.info("✔ Usuário atualizado: ID {}, Email {}", atualizado.getId(), atualizado.getEmail());

        return UsuarioFactory.toResponseDTO(atualizado);
    }

    public UsuarioResponseDTO atualizarRole(UsuarioUpdateRoleDTO dto) {

        Long id = Long.valueOf(dto.idUser());

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        UsuarioRole role;
        try {
            role = UsuarioRole.valueOf(dto.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Role inválida.");
        }

        UsuarioFactory.applyUpdateUserRole(usuario, role);

        Usuario atualizado = repository.save(usuario);

        return UsuarioFactory.toResponseDTO(atualizado);
    }

    public void atualizarSenha(Long id, UsuarioUpdateSenhaDTO dto) {

        log.info("🔐 Atualizando senha do usuário ID {}", id);

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("❌ Usuário com ID {} não encontrado para atualização de senha!", id);
                    return new EntityNotFoundException("Usuário não encontrado");
                });

        boolean senhaOk = passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha());

        if (!senhaOk) {
            log.warn("❌ Senha atual inválida para usuário {}", usuario.getEmail());
            throw new IllegalArgumentException("Senha atual incorreta");
        }

        UsuarioFactory.applySenhaUpdate(usuario, passwordEncoder.encode(dto.novaSenha()));

        repository.save(usuario);

        log.info("✔ Senha atualizada com sucesso para o usuário {}", usuario.getEmail());
    }


    public void deletar(Long id) {

        log.info("🗑 Tentativa de deletar usuário ID {}", id);

        if (!repository.existsById(id)) {
            log.warn("❌ Tentativa de deletar usuário inexistente ID {}", id);
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        repository.deleteById(id);

        log.info("🗑✔ Usuário ID {} deletado com sucesso!", id);
    }
}
