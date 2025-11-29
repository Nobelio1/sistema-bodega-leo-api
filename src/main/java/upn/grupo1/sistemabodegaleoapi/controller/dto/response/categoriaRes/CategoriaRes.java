package upn.grupo1.sistemabodegaleoapi.controller.dto.response.categoriaRes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRes {
    private Long idCategoria;
    private String nombre;
    private String descripcion;
}
