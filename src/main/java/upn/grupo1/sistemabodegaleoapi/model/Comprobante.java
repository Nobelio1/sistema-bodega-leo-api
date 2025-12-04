package upn.grupo1.sistemabodegaleoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobante")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComprobante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal igv;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @OneToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @OneToOne(mappedBy = "comprobante", cascade = CascadeType.ALL)
    private DatosFactura datosFactura;

    public enum Tipo {
        BOLETA,  // B001-00000001
        FACTURA  // F001-00000001
    }
}
