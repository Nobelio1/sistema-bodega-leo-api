package upn.grupo1.sistemabodegaleoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Tipo tipo;

    private String numero;
    private LocalDateTime fechaEmision;
    private BigDecimal montoTotal;

    @OneToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    public enum Tipo { BOLETA, FACTURA }

}
