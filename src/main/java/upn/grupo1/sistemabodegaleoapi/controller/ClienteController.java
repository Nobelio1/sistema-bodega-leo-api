package upn.grupo1.sistemabodegaleoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.clienteDto.CrearClienteDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.clienteRes.ClienteRes;
import upn.grupo1.sistemabodegaleoapi.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
@Tag(name = "Cliente", description = "Endpoints para la gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/registro")
    @Operation(summary = "Registrar cliente", description = "Registra un nuevo cliente en el sistema")
    public ResponseEntity<DataResponse<Object>> registrarCliente(@Valid @RequestBody CrearClienteDto cliente) {
        return ResponseEntity.ok(clienteService.crearCliente(cliente));
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener cliente por ID", description = "Obtiene la información de un cliente por su ID")
    public ResponseEntity<DataResponse<ClienteRes>> obtenerClientePorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar clientes", description = "Obtiene una lista de todos los clientes")
    public ResponseEntity<DataResponse<List<ClienteRes>>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar cliente", description = "Actualiza la información de un cliente")
    public ResponseEntity<DataResponse<Object>> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody CrearClienteDto cliente) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, cliente));
    }
}
