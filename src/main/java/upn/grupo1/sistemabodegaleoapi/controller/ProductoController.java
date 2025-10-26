package upn.grupo1.sistemabodegaleoapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upn.grupo1.sistemabodegaleoapi.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.service.ProductoService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping("/listar")
    public ResponseEntity<DataResponse<List<AllProductoResponse>>> listarProductos(){
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<DataResponse<Object>> subirImagen(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(productoService.subirImagen(id,file));
    }
}
