package upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CrearPedidoDto {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "El ID del estado es obligatorio")
    private Long idEstado;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal montoTotal;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<ListaProductoPedidoDto> detallePedido;
}
