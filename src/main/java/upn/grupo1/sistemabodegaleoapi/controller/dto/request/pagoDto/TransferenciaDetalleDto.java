package upn.grupo1.sistemabodegaleoapi.controller.dto.request.pagoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransferenciaDetalleDto {

    @NotBlank(message = "El nombre del banco es obligatorio para transferencias.")
    @Size(max = 50, message = "El nombre del banco no debe exceder los 50 caracteres.")
    private String nombreBanco;

    @NotBlank(message = "El nombre del depositante es obligatorio para transferencias.")
    @Size(max = 100, message = "El nombre del depositante no debe exceder los 100 caracteres.")
    private String depositante;

    @NotBlank(message = "El código de operación/referencia es obligatorio para transferencias.")
    @Size(max = 100, message = "El token de transferencia no debe exceder los 100 caracteres.")
    private String tokenTransferencia;
}
