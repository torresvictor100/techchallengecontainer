package com.techchallenge.domain.restaurante.repository;

import com.techchallenge.domain.restaurante.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    Optional<Restaurante> findByNomeIgnoreCase(String nome);
}
