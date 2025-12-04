package upn.grupo1.sistemabodegaleoapi.controller.dto.response.pagoRes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoRes {
    private Long idPago;
    private BigDecimal monto;
    private String metodo;
    private String estadoPago;
    private LocalDateTime fechaPago;
}
