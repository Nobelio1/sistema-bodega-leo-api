package upn.grupo1.sistemabodegaleoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upn.grupo1.sistemabodegaleoapi.model.enums.EstadoPagoEnum;
import upn.grupo1.sistemabodegaleoapi.model.enums.MetodoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @Enumerated(EnumType.STRING)
    private MetodoEnum metodo;

    private String codigoTransaccion;
    private BigDecimal monto;
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    private EstadoPagoEnum estadoPago;

}
