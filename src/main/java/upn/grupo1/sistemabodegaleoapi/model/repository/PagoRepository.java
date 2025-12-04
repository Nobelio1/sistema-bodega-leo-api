package upn.grupo1.sistemabodegaleoapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upn.grupo1.sistemabodegaleoapi.model.Pago;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByIdPago(Long id);

    List<Pago> findByPedidoIdPedido(Long idPedido);
}
