package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.AllCategoriaReponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.model.Categoria;
import upn.grupo1.sistemabodegaleoapi.model.repository.CategoriaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public DataResponse<List<AllCategoriaReponse>> listarCategorias(){
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

}
