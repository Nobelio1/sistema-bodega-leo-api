package upn.grupo1.sistemabodegaleoapi.controller.dto.response.clienteRes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRes {
    private Long idCliente;
    private String nombre;
    private String telefono;
    private String direccion;
    private String correo;
    private String nombreUsuario;
}
