package upn.grupo1.sistemabodegaleoapi.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upn.grupo1.sistemabodegaleoapi.model.enums.RolEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String usuario;
    private RolEnum rol;
    private String token;
}