package com.techchallenge.modules.usuario.repository;

import com.techchallenge.modules.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    Optional<Usuario> findByEmail(String email);
}
