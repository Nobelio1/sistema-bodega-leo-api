package upn.grupo1.sistemabodegaleoapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upn.grupo1.sistemabodegaleoapi.dto.response.AllProductoResponse;
import upn.grupo1.sistemabodegaleoapi.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.service.ProductoService;

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
}
