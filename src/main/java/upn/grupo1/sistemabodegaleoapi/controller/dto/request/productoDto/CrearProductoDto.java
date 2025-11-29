package upn.grupo1.sistemabodegaleoapi.controller.dto.request.productoDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CrearProductoDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioUnitario;

    @NotNull(message = "El stock actual es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockActual;

    @NotNull(message = "Debe indicar si el producto es refrigerado")
    private Boolean refrigerado;

    @NotNull(message = "El ID de categoría es obligatorio")
    private Long idCategoria;


}
