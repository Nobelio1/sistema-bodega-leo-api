package upn.grupo1.sistemabodegaleoapi.controller.dto.request.categoriaDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearCategoriaDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCategoria;

    @NotBlank(message = "El descripcion es obligatorio")
    private String descripcion;
}
