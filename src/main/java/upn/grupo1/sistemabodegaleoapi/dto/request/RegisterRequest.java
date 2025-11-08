package upn.grupo1.sistemabodegaleoapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upn.grupo1.sistemabodegaleoapi.model.enums.RolEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String nombreUsuario;
    private String contrasena;
    private RolEnum rol;
}