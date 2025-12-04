package upn.grupo1.sistemabodegaleoapi.controller.dto.request.comprobanteDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import upn.grupo1.sistemabodegaleoapi.model.Comprobante;

@Data
public class GenerarComprobanteDto {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long idPedido;

    @NotNull(message = "El tipo de comprobante es obligatorio")
    private Comprobante.Tipo tipoComprobante;

    private DatosFacturaDto datosFactura;
}
