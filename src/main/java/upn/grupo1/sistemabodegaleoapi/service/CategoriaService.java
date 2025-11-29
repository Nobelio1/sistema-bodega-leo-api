package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.categoriaDto.CrearCategoriaDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.categoriaRes.AllCategoriaReponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.categoriaRes.CategoriaRes;
import upn.grupo1.sistemabodegaleoapi.model.Categoria;
import upn.grupo1.sistemabodegaleoapi.model.repository.CategoriaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public DataResponse<CategoriaRes> findById(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Categoría no encontrada")
        );

        CategoriaRes categoriaRes = CategoriaRes.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombre(categoria.getNombreCategoria())
                .descripcion(categoria.getDescripcion())
                .build();

        return DataResponse.<CategoriaRes>builder()
                .success(true)
                .message("Categoría obtenida correctamente")
                .data(categoriaRes)
                .build();

    }

    @Transactional(readOnly = true)
    public DataResponse<List<AllCategoriaReponse>> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();

        List<AllCategoriaReponse> listCategorias = categorias.stream().map(categoria ->
                AllCategoriaReponse.builder()
                        .idCategoria(categoria.getIdCategoria())
                        .nombre(categoria.getNombreCategoria())
                        .cantidadProductos(categoria.getProductos() != null ? categoria.getProductos().size() : 0)
                        .build()
        ).toList();

        return DataResponse.<List<AllCategoriaReponse>>builder()
                .success(true)
                .message("Categorias obtenidas correctamente")
                .data(listCategorias)
                .build();
    }

    @Transactional()
    public DataResponse<Object> crearCategoria(CrearCategoriaDto nuevaCategoria) {

        Categoria categoria = Categoria.builder()
                .nombreCategoria(nuevaCategoria.getNombreCategoria())
                .descripcion(nuevaCategoria.getDescripcion())
                .build();

        categoriaRepository.save(categoria);
        return DataResponse.builder()
                .success(true)
                .message("Categoría creada correctamente")
                .build();
    }

    @Transactional()
    public DataResponse<Object> eliminarCategoria(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            return DataResponse.builder()
                    .success(false)
                    .message("Categoría no encontrada")
                    .build();
        }

        categoriaRepository.deleteById(idCategoria);
        return DataResponse.builder()
                .success(true)
                .message("Categoría eliminada correctamente")
                .build();
    }

    @Transactional()
    public DataResponse<Object> actualizarCategoria(Long idCategoria, CrearCategoriaDto categoria) {

        Categoria categoriaExistente = categoriaRepository.findById(idCategoria).orElseThrow(
                () -> new RuntimeException("Categoría no encontrada")
        );

        categoriaExistente.setNombreCategoria(categoria.getNombreCategoria());
        categoriaExistente.setDescripcion(categoria.getDescripcion());
        categoriaRepository.save(categoriaExistente);
        return DataResponse.builder()
                .success(true)
                .message("Categoría actualizada correctamente")
                .build();
    }


}

