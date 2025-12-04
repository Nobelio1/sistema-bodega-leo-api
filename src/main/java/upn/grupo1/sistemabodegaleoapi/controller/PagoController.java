package upn.grupo1.sistemabodegaleoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pagoDto.RegistrarPagoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.pagoRes.PagoRes;
import upn.grupo1.sistemabodegaleoapi.service.PagoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pago")
@RequiredArgsConstructor
@Tag(name = "Pago", description = "Endpoints para el proceso de pago")
@SecurityRequirement(name = "bearerAuth")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    @Operation(summary = "Registrar pago", description = "Registra un nuevo pago para un pedido y procesa la transacción")
    public ResponseEntity<DataResponse<Object>> registrarPago(@Valid @RequestBody RegistrarPagoDto pago) {
        return ResponseEntity.ok(pagoService.registraPago(pago));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Obtiene los detalles de un pago específico")
    public ResponseEntity<DataResponse<PagoRes>> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(this.pagoService.obtenerPagoPorId(id));
    }

    @GetMapping("/pedido/{idPedido}")
    @Operation(summary = "Obtener pagos por pedido", description = "Obtiene todos los pagos asociados a un pedido")
    public ResponseEntity<DataResponse<List<PagoRes>>> obtenerPagosPorPedido(@PathVariable Long idPedido) {
        return ResponseEntity.ok(this.pagoService.obtenerPagoPorIdPedido(idPedido));
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar pago", description = "Confirma un pago pendiente (principalmente para pagos en efectivo)")
    public ResponseEntity<DataResponse<Object>> confirmarPago(@PathVariable Long id) {
        return ResponseEntity.ok(this.pagoService.confirmarPago(id));
    }

    @GetMapping("/verificar/{codigoTransaccion}")
    @Operation(summary = "Verificar transacción", description = "Verifica el estado de una transacción en la pasarela de pagos")
    public ResponseEntity<DataResponse<Map<String, Object>>> verificarTransaccion(@PathVariable String codigoTransaccion) {
        return ResponseEntity.ok(this.pagoService.verificarTransaccion(codigoTransaccion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago", description = "Elimina o marca como fallido un pago")
    public ResponseEntity<DataResponse<Object>> eliminarPago(@PathVariable Long id) {
        return ResponseEntity.ok(this.pagoService.eliminarPago(id));
    }
}
