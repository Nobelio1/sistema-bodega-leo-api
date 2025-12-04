package upn.grupo1.sistemabodegaleoapi.controller.dto.response.comprobanteRes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComprobanteRes {
    private Long idComprobante;
    private String tipo;
    private String numero;
    private LocalDateTime fechaEmision;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal montoTotal;

    private String nombreCliente;
    private String documentoCliente;
    private String direccionCliente;

    private String ruc;
    private String razonSocial;
    private String direccionFiscal;

    private List<DetalleComprobanteRes> detalles;

    private String codigoPedido;
}
