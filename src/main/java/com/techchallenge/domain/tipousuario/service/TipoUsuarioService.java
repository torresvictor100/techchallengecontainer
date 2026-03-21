package com.techchallenge.domain.tipousuario.service;

import com.techchallenge.domain.tipousuario.dto.TipoUsuarioCreateDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioResponseDTO;
import com.techchallenge.domain.tipousuario.dto.TipoUsuarioUpdateDTO;
import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import com.techchallenge.domain.tipousuario.factory.TipoUsuarioFactory;
import com.techchallenge.domain.tipousuario.repository.TipoUsuarioRepository;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoUsuarioService {

    private static final Logger log = LoggerFactory.getLogger(TipoUsuarioService.class);

    private final TipoUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public TipoUsuarioService(TipoUsuarioRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<TipoUsuarioResponseDTO> listarTodos() {
        log.info("📌 Listando todos os tipos de usuário...");

        return repository.findAll()
                .stream()
                .map(TipoUsuarioFactory::toResponseDTO)
                .toList();
    }

    public TipoUsuarioResponseDTO buscarPorId(Long id) {
        log.info("🔍 Buscando tipo de usuário ID {}", id);

        TipoUsuario tipo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário não encontrado"));

        return TipoUsuarioFactory.toResponseDTO(tipo);
    }

    public TipoUsuarioResponseDTO criar(TipoUsuarioCreateDTO dto) {
        log.info("📝 Criando tipo de usuário: {}", dto.nome());

        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new IllegalArgumentException("Tipo de usuário já existe");
        }

        TipoUsuario novo = TipoUsuarioFactory.fromCreateDTO(dto);
        TipoUsuario salvo = repository.save(novo);

        return TipoUsuarioFactory.toResponseDTO(salvo);
    }

    public TipoUsuarioResponseDTO atualizar(Long id, TipoUsuarioUpdateDTO dto) {
        log.info("✏ Atualizando tipo de usuário ID {}", id);

        TipoUsuario tipo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário não encontrado"));

        if (!tipo.getNome().equalsIgnoreCase(dto.nome())
                && repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new IllegalArgumentException("Tipo de usuário já existe");
        }

        TipoUsuarioFactory.applyUpdate(tipo, dto);
        TipoUsuario atualizado = repository.save(tipo);

        return TipoUsuarioFactory.toResponseDTO(atualizado);
    }

    public void deletar(Long id) {
        log.info("🗑 Deletando tipo de usuário ID {}", id);

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de usuário não encontrado");
        }

        if (usuarioRepository.existsByTipoUsuarioId(id)) {
            throw new IllegalArgumentException("Não é possível deletar tipo de usuário associado a usuários");
        }

        repository.deleteById(id);
    }
}
