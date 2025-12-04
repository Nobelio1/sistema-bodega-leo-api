package upn.grupo1.sistemabodegaleoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pagoDto.RegistrarPagoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.pagoRes.PagoRes;
import upn.grupo1.sistemabodegaleoapi.model.Pago;
import upn.grupo1.sistemabodegaleoapi.service.PagoService;

import java.util.List;

@RestController
@RequestMapping("/pago")
@RequiredArgsConstructor
@Tag(name = "Pago", description = "Endpoints para la proceso de pago")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping()
    @Operation(summary = "Registrar Pago", description = "Registra un nuevo pago para un pedido")
    public ResponseEntity<DataResponse<Object>> registrarPago(@RequestBody RegistrarPagoDto pago) {
        return ResponseEntity.ok(pagoService.registraPago(pago));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<PagoRes>> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(this.pagoService.obtenerPagoPorId(id));
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<DataResponse<List<PagoRes>>> obtenerPagosPorPedido(@PathVariable Long idPedido) {
        return ResponseEntity.ok(this.pagoService.obtenerPagoPorIdPedido(idPedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<Object>> eliminarPago(@PathVariable Long id) {
        return ResponseEntity.ok(this.pagoService.eliminarPago(id));
    }


}
