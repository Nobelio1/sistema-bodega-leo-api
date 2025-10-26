package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upn.grupo1.sistemabodegaleoapi.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.repository.ProductoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    @Transactional(readOnly = true)
    public DataResponse<List<AllProductoResponse>> listarProductos() {
        List<AllProductoResponse> productos = productoRepository.findAll()
                .stream()
                .map(producto -> AllProductoResponse.builder()
                        .idProducto(producto.getIdProducto())
                        .nombre(producto.getNombre())
                        .descripcion(producto.getDescripcion())
                        .imagen(producto.getImagenes() != null && !producto.getImagenes().isEmpty()
                                ? producto.getImagenes().get(0).getUrlImagen()
                                : null)
                        .precio(producto.getPrecioUnitario().doubleValue())
                        .cantidad(producto.getStockActual().longValue())
                        .nombreCategoria(producto.getCategoria() != null
                                ? producto.getCategoria().getNombreCategoria()
                                : null)
                        .build())
                .toList();

        return DataResponse.<List<AllProductoResponse>>builder()
                .success(true)
                .message("Lista de productos obtenida correctamente")
                .data(productos)
                .build();
    }

}
