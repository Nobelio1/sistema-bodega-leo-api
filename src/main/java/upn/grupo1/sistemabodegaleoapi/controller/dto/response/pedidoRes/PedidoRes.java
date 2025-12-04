package upn.grupo1.sistemabodegaleoapi.controller.dto.response.pedidoRes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRes {
    private Long idPedido;
    private LocalDate fechaPedido;
    private LocalTime horaPedido;
    private BigDecimal montoTotal;
    private String codigoRecojo;
    private String nombreCliente;
    private String estadoPedido;
    private BigDecimal totalPagado;
}
