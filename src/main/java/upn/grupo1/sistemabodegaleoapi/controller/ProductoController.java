package upn.grupo1.sistemabodegaleoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.ListarProductoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.service.ProductoService;
import org.springframework.data.domain.Page;

import java.io.IOException;

@RestController
@RequestMapping("/producto")
@RequiredArgsConstructor
@Tag(name = "Producto", description = "Endpoints para la gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping()
    @Operation(summary = "Listar productos", description = "Obtiene una lista paginada de productos con filtros opcionales")
    public ResponseEntity<DataResponse<Page<AllProductoResponse>>> listarProductos(
            @Valid ListarProductoDto filtros) {
        return ResponseEntity.ok(productoService.listarProductos(filtros));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Listar producto por ID", description = "Obtiene un producto por su ID")
    public ResponseEntity<DataResponse<AllProductoResponse>> productoById(
            @PathVariable Long id) {
        return ResponseEntity.ok(productoService.productoById(id));
    }

    @PostMapping("/{id}/imagen")
    @Operation(summary = "Subir imagen de producto", description = "Sube una imagen al producto por su ID")
    public ResponseEntity<DataResponse<Object>> subirImagen(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(productoService.subirImagen(id, file));
    }
}
