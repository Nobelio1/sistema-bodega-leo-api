package upn.grupo1.sistemabodegaleoapi.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllProductoResponse {
    private Long idProducto;
    private String nombre;
    private String descripcion;
    private String imagen;
    private Double precio;
    private Long cantidad;
    private String nombreCategoria;
}
