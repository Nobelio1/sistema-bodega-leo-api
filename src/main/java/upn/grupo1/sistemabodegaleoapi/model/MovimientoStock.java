package upn.grupo1.sistemabodegaleoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upn.grupo1.sistemabodegaleoapi.model.enums.TipoMovimientoEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_stock")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @Enumerated(EnumType.STRING)
    private TipoMovimientoEnum tipoMovimiento;

    private Integer cantidad;
    private LocalDateTime fechaMovimiento;
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;
}
