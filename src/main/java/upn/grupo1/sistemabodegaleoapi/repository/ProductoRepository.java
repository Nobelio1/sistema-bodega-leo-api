package upn.grupo1.sistemabodegaleoapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import upn.grupo1.sistemabodegaleoapi.model.Producto;

import java.math.BigDecimal;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE " +
            "(:categoriaId IS NULL OR p.categoria.idCategoria = :categoriaId) AND " +
            "(:precioMin IS NULL OR p.precioUnitario >= :precioMin) AND " +
            "(:precioMax IS NULL OR p.precioUnitario <= :precioMax) AND " +
            "p.activo = true")
    Page<Producto> findByFiltros(@Param("categoriaId") Long categoriaId,
                                 @Param("precioMin") BigDecimal precioMin,
                                 @Param("precioMax") BigDecimal precioMax,
                                 Pageable pageable);


}
