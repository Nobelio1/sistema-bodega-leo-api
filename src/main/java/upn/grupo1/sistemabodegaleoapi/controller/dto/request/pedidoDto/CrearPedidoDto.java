package upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CrearPedidoDto {
    private LocalDate fecha;
    private String hora;
    private BigDecimal montoTotal;
    private Long idCliente;
    private Long idEstado;
    private ListaProductoPedidoDto detallePedido;
}
