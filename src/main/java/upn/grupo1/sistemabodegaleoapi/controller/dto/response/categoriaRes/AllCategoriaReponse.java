package upn.grupo1.sistemabodegaleoapi.controller.dto.response.categoriaRes;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllCategoriaReponse {
    private Long idCategoria;
    private String nombre;
    private Integer cantidadProductos;
}
