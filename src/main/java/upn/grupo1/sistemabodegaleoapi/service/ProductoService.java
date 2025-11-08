package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import upn.grupo1.sistemabodegaleoapi.dto.request.ListarProductoDto;
import upn.grupo1.sistemabodegaleoapi.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.model.ImagenProducto;
import upn.grupo1.sistemabodegaleoapi.model.Producto;
import upn.grupo1.sistemabodegaleoapi.repository.ImagenProductoRepository;
import upn.grupo1.sistemabodegaleoapi.repository.ProductoRepository;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ImagenProductoRepository imagenProductoRepository;

    @Transactional(readOnly = true)
    public DataResponse<Page<AllProductoResponse>> listarProductos(
            ListarProductoDto filtros
    ) {
        Sort sort = filtros.getOrden().equalsIgnoreCase("desc")
                ? Sort.by("precioUnitario").descending()
                : Sort.by("precioUnitario").ascending();

        Pageable pageable = PageRequest.of(filtros.getPagina(), filtros.getLimite(), sort);

        Page<Producto> productoPage;

        if (filtros.getCategoria() != null || filtros.getPrecioMin() != null || filtros.getPrecioMax() != null) {
            productoPage = productoRepository.findByFiltros(
                    filtros.getCategoria(),
                    filtros.getPrecioMin(),
                    filtros.getPrecioMax(),
                    pageable
            );
        } else {
            productoPage = productoRepository.findAll(pageable);
        }

        Page<AllProductoResponse> productos = productoPage
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
                        .build());

        return DataResponse.<Page<AllProductoResponse>>builder()
                .success(true)
                .message("Lista de productos obtenida correctamente")
                .data(productos)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<AllProductoResponse> productoById(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto).orElseThrow(
                () -> new RuntimeException("Producto no encontrado")
        );

        AllProductoResponse productoResponse = new AllProductoResponse();

        productoResponse.setIdProducto(producto.getIdProducto());
        productoResponse.setNombre(producto.getNombre());
        productoResponse.setDescripcion(producto.getDescripcion());
        productoResponse.setPrecio(producto.getPrecioUnitario().doubleValue());
        productoResponse.setCantidad(producto.getStockActual().longValue());
        productoResponse.setNombreCategoria(producto.getCategoria() != null
                ? producto.getCategoria().getNombreCategoria()
                : null);

        if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
            productoResponse.setImagen(producto.getImagenes().get(0).getUrlImagen());
        }

        return DataResponse.<AllProductoResponse>builder()
                .success(true)
                .message("Producto obtenido correctamente")
                .data(productoResponse)
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
