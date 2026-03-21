package com.techchallenge.domain.restaurante.service;

import com.techchallenge.domain.restaurante.dto.RestauranteCreateDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteResponseDTO;
import com.techchallenge.domain.restaurante.dto.RestauranteUpdateDTO;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.restaurante.factory.RestauranteFactory;
import com.techchallenge.domain.restaurante.repository.RestauranteRepository;
import com.techchallenge.domain.usuario.entity.Usuario;
import com.techchallenge.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteService {

    private static final Logger log = LoggerFactory.getLogger(RestauranteService.class);

    private final RestauranteRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public RestauranteService(RestauranteRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<RestauranteResponseDTO> listarTodos() {
        log.info("📌 Listando restaurantes...");

        return repository.findAll()
                .stream()
                .map(RestauranteFactory::toResponseDTO)
                .toList();
    }

    public RestauranteResponseDTO buscarPorId(Long id) {
        log.info("🔍 Buscando restaurante ID {}", id);

        Restaurante restaurante = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

        return RestauranteFactory.toResponseDTO(restaurante);
    }

    public RestauranteResponseDTO criar(RestauranteCreateDTO dto) {
        log.info("📝 Criando restaurante: {}", dto.nome());

        Usuario dono = usuarioRepository.findById(dto.donoId())
                .orElseThrow(() -> new EntityNotFoundException("Dono do restaurante não encontrado"));

        Restaurante novo = RestauranteFactory.fromCreateDTO(dto, dono);
        Restaurante salvo = repository.save(novo);

        return RestauranteFactory.toResponseDTO(salvo);
    }

    public RestauranteResponseDTO atualizar(Long id, RestauranteUpdateDTO dto) {
        log.info("✏ Atualizando restaurante ID {}", id);

        Restaurante restaurante = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

        Usuario dono = usuarioRepository.findById(dto.donoId())
                .orElseThrow(() -> new EntityNotFoundException("Dono do restaurante não encontrado"));

        RestauranteFactory.applyUpdate(restaurante, dto, dono);
        Restaurante atualizado = repository.save(restaurante);

        return RestauranteFactory.toResponseDTO(atualizado);
    }

    public void deletar(Long id) {
        log.info("🗑 Deletando restaurante ID {}", id);

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Restaurante não encontrado");
        }

        repository.deleteById(id);
    }
}
