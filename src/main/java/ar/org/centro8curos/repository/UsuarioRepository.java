package ar.org.centro8curos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.org.centro8curos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

}
