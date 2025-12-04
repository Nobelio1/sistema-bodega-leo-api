package upn.grupo1.sistemabodegaleoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto.ActualizarPedidoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto.CrearPedidoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.pedidoRes.PedidoDetalleRes;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.pedidoRes.PedidoRes;
import upn.grupo1.sistemabodegaleoapi.service.PedidoService;

@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
@Tag(name = "Pedido", description = "Endpoints para la gestión de pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido en el sistema")
    public ResponseEntity<DataResponse<Object>> crearPedido(@Valid @RequestBody CrearPedidoDto pedido) {
        return ResponseEntity.ok(pedidoService.crearPedido(pedido));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Obtiene el detalle completo de un pedido por su ID")
    public ResponseEntity<DataResponse<PedidoDetalleRes>> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Obtiene una lista paginada de todos los pedidos")
    public ResponseEntity<DataResponse<Page<PedidoRes>>> listarPedidos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(pedidoService.listarPedidos(pagina, limite));
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Listar pedidos por cliente", description = "Obtiene los pedidos de un cliente específico")
    public ResponseEntity<DataResponse<Page<PedidoRes>>> listarPedidosPorCliente(
            @PathVariable Long idCliente,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorCliente(idCliente, pagina, limite));
    }

    @GetMapping("/codigo/{codigoRecojo}")
    @Operation(summary = "Buscar por código de recojo", description = "Busca un pedido por su código de recojo")
    public ResponseEntity<DataResponse<PedidoDetalleRes>> buscarPorCodigoRecojo(@PathVariable String codigoRecojo) {
        return ResponseEntity.ok(pedidoService.buscarPorCodigoRecojo(codigoRecojo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido", description = "Actualiza la información de un pedido")
    public ResponseEntity<DataResponse<Object>> actualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPedidoDto pedido) {
        return ResponseEntity.ok(pedidoService.actualizarPedido(id, pedido));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Cancela un pedido y devuelve el stock de los productos")
    public ResponseEntity<DataResponse<Object>> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    @PatchMapping("/{id}/estado/{idEstado}")
    @Operation(summary = "Cambiar estado de pedido", description = "Cambia el estado de un pedido")
    public ResponseEntity<DataResponse<Object>> cambiarEstadoPedido(
            @PathVariable Long id,
            @PathVariable Long idEstado) {
        return ResponseEntity.ok(pedidoService.cambiarEstadoPedido(id, idEstado));
    }
}
