package upn.grupo1.sistemabodegaleoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
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

    @Column(nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    private MetodoEnum metodo;

    @Column(unique = true, length = 100)
    private String codigoTransaccion;

    @CreationTimestamp
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    private EstadoPagoEnum estadoPago;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

}
