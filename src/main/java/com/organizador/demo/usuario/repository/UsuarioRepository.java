package com.organizador.demo.usuario.repository;

import java.util.Optional;

import com.organizador.demo.usuario.entidade.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNome(String nome);
}
