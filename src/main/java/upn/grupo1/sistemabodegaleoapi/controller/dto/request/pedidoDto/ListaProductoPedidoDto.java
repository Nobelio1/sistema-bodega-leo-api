package upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ListaProductoPedidoDto {
    private int idProducto;
    private int cantidad;
    private BigDecimal precio;
    private BigDecimal subtotal;
}
