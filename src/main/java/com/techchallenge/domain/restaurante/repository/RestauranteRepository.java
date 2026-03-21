package com.techchallenge.domain.restaurante.repository;

import com.techchallenge.domain.restaurante.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}
