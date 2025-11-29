package upn.grupo1.sistemabodegaleoapi.service.mappers;

import upn.grupo1.sistemabodegaleoapi.controller.dto.request.productoDto.CrearProductoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.model.Producto;

public class ProductoServiceMapper {

    public static AllProductoResponse toAllProducto(Producto producto) {
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
        return productoResponse;
    }

    public static Producto toNewProducto(CrearProductoDto nuevoProducto) {
        Producto producto = new Producto();

        producto.setNombre(nuevoProducto.getNombre());
        producto.setDescripcion(nuevoProducto.getDescripcion());
        producto.setPrecioUnitario(nuevoProducto.getPrecioUnitario());
        producto.setStockActual(nuevoProducto.getStockActual());
        producto.setRefrigerado(nuevoProducto.getRefrigerado());

        return producto;
    }

}

