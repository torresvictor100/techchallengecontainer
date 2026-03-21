package com.techchallenge.domain.cardapio.repository;

import com.techchallenge.domain.cardapio.entity.ItemCardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {
}
