package upn.grupo1.sistemabodegaleoapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upn.grupo1.sistemabodegaleoapi.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}