package upn.grupo1.sistemabodegaleoapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upn.grupo1.sistemabodegaleoapi.dto.request.ListarProductoDto;
import upn.grupo1.sistemabodegaleoapi.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.service.ProductoService;
import org.springframework.data.domain.Page;

import java.io.IOException;

@RestController
@RequestMapping("/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping("/listar")
    public ResponseEntity<DataResponse<Page<AllProductoResponse>>> listarProductos(
            @Valid ListarProductoDto filtros) {
        return ResponseEntity.ok(productoService.listarProductos(filtros));
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<DataResponse<AllProductoResponse>> productoById(
            @PathVariable Long id) {
        return ResponseEntity.ok(productoService.productoById(id));
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<DataResponse<Object>> subirImagen(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(productoService.subirImagen(id, file));
    }
}
