package upn.grupo1.sistemabodegaleoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.categoriaDto.CrearCategoriaDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.categoriaRes.AllCategoriaReponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.categoriaRes.CategoriaRes;
import upn.grupo1.sistemabodegaleoapi.service.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
@Tag(name = "Categoria", description = "Endpoints para la gestión de categorías")
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping()
    @Operation(summary = "Listar categorías", description = "Obtiene una lista de todas las categorías disponibles")
    public ResponseEntity<DataResponse<List<AllCategoriaReponse>>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Obtiene los detalles")
    public ResponseEntity<DataResponse<CategoriaRes>> obtenerCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @PostMapping()
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría en el sistema")
    public ResponseEntity<DataResponse<Object>> crearCategoria(@RequestBody CrearCategoriaDto nuevaCategoria) {
        return ResponseEntity.ok(categoriaService.crearCategoria(nuevaCategoria));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría por su ID")
    public ResponseEntity<DataResponse<Object>> eliminarCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.eliminarCategoria(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza los datos de una categoría existente")
    public ResponseEntity<DataResponse<Object>> actualizarCategoria(@PathVariable Long id, @RequestBody CrearCategoriaDto categoriaDto) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, categoriaDto));
    }
}
