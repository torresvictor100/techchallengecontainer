package com.techchallenge.domain.tipousuario.repository;

import com.techchallenge.domain.tipousuario.entity.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
    Optional<TipoUsuario> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
