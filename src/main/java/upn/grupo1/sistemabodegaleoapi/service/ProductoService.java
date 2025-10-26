package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import upn.grupo1.sistemabodegaleoapi.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.model.ImagenProducto;
import upn.grupo1.sistemabodegaleoapi.model.Producto;
import upn.grupo1.sistemabodegaleoapi.repository.ImagenProductoRepository;
import upn.grupo1.sistemabodegaleoapi.repository.ProductoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ImagenProductoRepository imagenProductoRepository;
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

    @Transactional
    public DataResponse<Object> subirImagen(Long idProducto, MultipartFile file) throws IOException {
        Producto producto = productoRepository.findById(idProducto).orElseThrow(
                () -> new RuntimeException("Producto no encontrado")
        );

        String uploadDir = "src/main/resources/static/productos/";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ImagenProducto imagen = new ImagenProducto();
        imagen.setProducto(producto);
        imagen.setUrlImagen("/productos/" + fileName);
        imagenProductoRepository.save(imagen);

        return DataResponse.builder()
                .success(true)
                .message("Imagen subida correctamente")
                .build();
    }

}
