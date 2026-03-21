package com.techchallenge.domain.cardapio.service;

import com.techchallenge.domain.cardapio.dto.ItemCardapioCreateDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioResponseDTO;
import com.techchallenge.domain.cardapio.dto.ItemCardapioUpdateDTO;
import com.techchallenge.domain.cardapio.entity.ItemCardapio;
import com.techchallenge.domain.cardapio.factory.ItemCardapioFactory;
import com.techchallenge.domain.cardapio.repository.ItemCardapioRepository;
import com.techchallenge.domain.restaurante.entity.Restaurante;
import com.techchallenge.domain.restaurante.repository.RestauranteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCardapioService {

    private static final Logger log = LoggerFactory.getLogger(ItemCardapioService.class);

    private final ItemCardapioRepository repository;
    private final RestauranteRepository restauranteRepository;

    @Autowired
    public ItemCardapioService(ItemCardapioRepository repository, RestauranteRepository restauranteRepository) {
        this.repository = repository;
        this.restauranteRepository = restauranteRepository;
    }

    public List<ItemCardapioResponseDTO> listarTodos() {
        log.info("📌 Listando itens do cardápio...");

        return repository.findAll()
                .stream()
                .map(ItemCardapioFactory::toResponseDTO)
                .toList();
    }

    public ItemCardapioResponseDTO buscarPorId(Long id) {
        log.info("🔍 Buscando item do cardápio ID {}", id);

        ItemCardapio item = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado"));

        return ItemCardapioFactory.toResponseDTO(item);
    }

    public ItemCardapioResponseDTO criar(ItemCardapioCreateDTO dto) {
        log.info("📝 Criando item do cardápio: {}", dto.nome());

        Restaurante restaurante = restauranteRepository.findById(dto.restauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

        ItemCardapio novo = ItemCardapioFactory.fromCreateDTO(dto, restaurante);
        ItemCardapio salvo = repository.save(novo);

        return ItemCardapioFactory.toResponseDTO(salvo);
    }

    public ItemCardapioResponseDTO atualizar(Long id, ItemCardapioUpdateDTO dto) {
        log.info("✏ Atualizando item do cardápio ID {}", id);

        ItemCardapio item = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado"));

        Restaurante restaurante = restauranteRepository.findById(dto.restauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

        ItemCardapioFactory.applyUpdate(item, dto, restaurante);
        ItemCardapio atualizado = repository.save(item);

        return ItemCardapioFactory.toResponseDTO(atualizado);
    }

    public void deletar(Long id) {
        log.info("🗑 Deletando item do cardápio ID {}", id);

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Item do cardápio não encontrado");
        }

        repository.deleteById(id);
    }
}
