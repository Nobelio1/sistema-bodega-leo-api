package upn.grupo1.sistemabodegaleoapi.controller.dto.request.productoDto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ListarProductoDto {
    private Long categoria;
    private BigDecimal precioMin;
    private BigDecimal precioMax;

    private String orden = "asc";

    @Min(value = 0, message = "La página no puede ser un número negativo")
    private int pagina = 0;

    @Min(value = 1, message = "El límite debe ser de al menos 1")
    private int limite = 10;
}
