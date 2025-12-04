package upn.grupo1.sistemabodegaleoapi.controller.dto.request.comprobanteDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DatosFacturaDto {

    @NotBlank(message = "El RUC es obligatorio para facturas")
    @Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe tener 11 dígitos")
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria para facturas")
    private String razonSocial;

    @NotBlank(message = "La dirección fiscal es obligatoria para facturas")
    private String direccionFiscal;
}
