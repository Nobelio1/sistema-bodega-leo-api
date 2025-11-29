package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.productoDto.ActualizarProductoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.productoDto.ActualizarStockDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.productoDto.CrearProductoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.productoDto.ListarProductoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.model.ImagenProducto;
import upn.grupo1.sistemabodegaleoapi.model.Producto;
import upn.grupo1.sistemabodegaleoapi.model.repository.ImagenProductoRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import upn.grupo1.sistemabodegaleoapi.service.mappers.ProductoServiceMapper;

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

        if (!producto.getActivo()) {
            throw new RuntimeException("Producto no disponible");
        }

        AllProductoResponse productoResponse = ProductoServiceMapper.toAllProducto(producto);

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

    @Transactional
    public DataResponse<Object> crearProducto(CrearProductoDto nuevoProducto) {
        Producto producto = ProductoServiceMapper.toNewProducto(nuevoProducto);
        productoRepository.save(producto);
        return DataResponse.builder()
                .success(true)
                .message("Producto creado correctamente")
                .build();
    }

    @Transactional
    public DataResponse<Object> cambiarEstadoProducto(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto).orElseThrow(
                () -> new RuntimeException("Producto no encontrado")
        );

        producto.setActivo(!producto.getActivo());
        productoRepository.save(producto);

        return DataResponse.builder()
                .success(true)
                .message("Producto eliminado correctamente")
                .build();
    }

    @Transactional
    public DataResponse<Object> actualizarProducto(Long idProducto, ActualizarProductoDto producto) {
        Producto productoExistente = productoRepository.findById(idProducto).orElseThrow(
                () -> new RuntimeException("Producto no encontrado")
        );

        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecioUnitario(producto.getPrecioUnitario());

        productoRepository.save(productoExistente);

        return DataResponse.builder()
                .success(true)
                .message("Producto actualizado correctamente")
                .build();
    }

    @Transactional
    public DataResponse<Object> actualizarStockProducto(Long idProducto, ActualizarStockDto producto) {
        Producto productoExistente = productoRepository.findById(idProducto).orElseThrow(
                () -> new RuntimeException("Producto no encontrado")
        );

        Integer stockActual = productoExistente.getStockActual();
        Integer cantidadActualizar = producto.getStock();

        if (producto.getTipoActualizacion()) {
            productoExistente.setStockActual(stockActual + cantidadActualizar);
        } else {
            if (stockActual - cantidadActualizar < 0) {
                throw new RuntimeException("El stock no puede ser negativo");
            }
            productoExistente.setStockActual(stockActual - cantidadActualizar);
        }

        productoRepository.save(productoExistente);

        return DataResponse.builder()
                .success(true)
                .message("Stock del producto actualizado correctamente")
                .build();
    }


}
