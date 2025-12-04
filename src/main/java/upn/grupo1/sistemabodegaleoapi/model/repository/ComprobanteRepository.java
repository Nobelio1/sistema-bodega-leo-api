package upn.grupo1.sistemabodegaleoapi.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upn.grupo1.sistemabodegaleoapi.model.Comprobante;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {

    Optional<Comprobante> findByPedidoIdPedido(Long idPedido);

    Optional<Comprobante> findByNumero(String numero);

    List<Comprobante> findByTipo(Comprobante.Tipo tipo);

    Page<Comprobante> findByTipo(Comprobante.Tipo tipo, Pageable pageable);

    @Query("SELECT c FROM Comprobante c WHERE c.fechaEmision BETWEEN :fechaInicio AND :fechaFin")
    List<Comprobante> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT c FROM Comprobante c WHERE c.tipo = :tipo ORDER BY c.numero DESC")
    List<Comprobante> findFirstByTipoOrderByNumeroDesc(Comprobante.Tipo tipo, Pageable pageable);

    boolean existsByPedidoIdPedido(Long idPedido);
}
