package upn.grupo1.sistemabodegaleoapi.controller.dto.request.productoDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarStockDto {

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El tipo de actualización es obligatorio")
    private Boolean tipoActualizacion;
}
