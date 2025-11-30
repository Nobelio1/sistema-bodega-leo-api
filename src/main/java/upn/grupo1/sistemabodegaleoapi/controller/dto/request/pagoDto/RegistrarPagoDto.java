package upn.grupo1.sistemabodegaleoapi.controller.dto.request.pagoDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import upn.grupo1.sistemabodegaleoapi.model.enums.MetodoEnum;

import java.math.BigDecimal;

@Data
public class RegistrarPagoDto {

    @NotNull(message = "El ID del Pedido no puede ser nulo.")
    @Min(value = 1, message = "El ID del Pedido debe ser un número positivo.")
    private Long idPedido;

    @NotNull(message = "El método de pago es obligatorio.")
    private MetodoEnum metodoPago;

    @NotNull(message = "El monto del pago es obligatorio.")
    @DecimalMin(value = "0.01", message = "El monto debe ser positivo y mayor a cero.")
    private BigDecimal monto;

    @Valid
    private TransferenciaDetalleDto transferenciaDetalle;


}
