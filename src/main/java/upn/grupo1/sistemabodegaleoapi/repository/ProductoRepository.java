package upn.grupo1.sistemabodegaleoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upn.grupo1.sistemabodegaleoapi.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
