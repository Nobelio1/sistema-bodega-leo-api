package upn.grupo1.sistemabodegaleoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.comprobanteDto.GenerarComprobanteDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.comprobanteRes.ComprobanteRes;
import upn.grupo1.sistemabodegaleoapi.service.ComprobanteService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comprobante")
@RequiredArgsConstructor
@Tag(name = "Comprobante", description = "Endpoints para la gestión de comprobantes (Boletas y Facturas)")
@SecurityRequirement(name = "bearerAuth")
public class ComprobanteController {

    private final ComprobanteService comprobanteService;

    @PostMapping
    @Operation(
            summary = "Generar comprobante",
            description = "Genera una boleta o factura para un pedido. El pedido debe tener al menos un pago confirmado."
    )
    public ResponseEntity<DataResponse<ComprobanteRes>> generarComprobante(
            @Valid @RequestBody GenerarComprobanteDto dto) {
        return ResponseEntity.ok(comprobanteService.generarComprobante(dto));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener comprobante por ID",
            description = "Obtiene el detalle completo de un comprobante por su ID"
    )
    public ResponseEntity<DataResponse<ComprobanteRes>> obtenerComprobantePorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(comprobanteService.obtenerComprobantePorId(id));
    }

    @GetMapping("/numero/{numero}")
    @Operation(
            summary = "Obtener comprobante por número",
            description = "Busca un comprobante por su número (Ej: B001-00000001 o F001-00000001)"
    )
    public ResponseEntity<DataResponse<ComprobanteRes>> obtenerComprobantePorNumero(
            @PathVariable String numero) {
        return ResponseEntity.ok(comprobanteService.obtenerComprobantePorNumero(numero));
    }

    @GetMapping("/pedido/{idPedido}")
    @Operation(
            summary = "Obtener comprobante por pedido",
            description = "Obtiene el comprobante asociado a un pedido específico"
    )
    public ResponseEntity<DataResponse<ComprobanteRes>> obtenerComprobantePorPedido(
            @PathVariable Long idPedido) {
        return ResponseEntity.ok(comprobanteService.obtenerComprobantePorPedido(idPedido));
    }

    @GetMapping
    @Operation(
            summary = "Listar comprobantes",
            description = "Obtiene una lista paginada de comprobantes. Se puede filtrar por tipo (BOLETA o FACTURA)"
    )
    public ResponseEntity<DataResponse<Page<ComprobanteRes>>> listarComprobantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(required = false) String tipo) {
        return ResponseEntity.ok(comprobanteService.listarComprobantes(pagina, limite, tipo));
    }

    @GetMapping("/fecha")
    @Operation(
            summary = "Listar comprobantes por fecha",
            description = "Obtiene los comprobantes emitidos en un rango de fechas"
    )
    public ResponseEntity<DataResponse<List<ComprobanteRes>>> listarComprobantesPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(comprobanteService.listarComprobantesPorFecha(fechaInicio, fechaFin));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Anular comprobante",
            description = "Anula un comprobante existente (Solo ADMIN)"
    )
    public ResponseEntity<DataResponse<Object>> anularComprobante(@PathVariable Long id) {
        return ResponseEntity.ok(comprobanteService.anularComprobante(id));
    }
}
