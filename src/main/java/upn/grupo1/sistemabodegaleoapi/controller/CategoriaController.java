package upn.grupo1.sistemabodegaleoapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upn.grupo1.sistemabodegaleoapi.dto.response.AllCategoriaReponse;
import upn.grupo1.sistemabodegaleoapi.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.service.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping("listar")
    public ResponseEntity<DataResponse<List<AllCategoriaReponse>>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }
}
