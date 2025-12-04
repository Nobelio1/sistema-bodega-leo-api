package upn.grupo1.sistemabodegaleoapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upn.grupo1.sistemabodegaleoapi.model.DatosFactura;

import java.util.Optional;

@Repository
public interface DatosFacturaRepository extends JpaRepository<DatosFactura, Long> {

    Optional<DatosFactura> findByComprobanteIdComprobante(Long idComprobante);

    Optional<DatosFactura> findByRuc(String ruc);
}
