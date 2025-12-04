package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto.ActualizarPedidoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto.CrearPedidoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto.ListaProductoPedidoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.pedidoRes.DetallePedidoRes;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.pedidoRes.PedidoDetalleRes;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.pedidoRes.PedidoRes;
import upn.grupo1.sistemabodegaleoapi.model.*;
import upn.grupo1.sistemabodegaleoapi.model.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteRepository clienteRepository;
    private final EstadoRepository estadoRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public DataResponse<Object> crearPedido(CrearPedidoDto nuevoPedido) {
        Cliente cliente = clienteRepository.findByIdCliente(nuevoPedido.getIdCliente()).orElseThrow(
                () -> new RuntimeException("Cliente no encontrado")
        );

        Estado estado = estadoRepository.findByIdEstado(nuevoPedido.getIdEstado()).orElseThrow(
                () -> new RuntimeException("Estado no encontrado")
        );

        String codigoRecojo = generarCodigoRecojo();

        Pedido pedido = Pedido.builder()
                .fechaPedido(LocalDate.now())
                .horaPedido(LocalTime.now())
                .montoTotal(nuevoPedido.getMontoTotal())
                .codigoRecojo(codigoRecojo)
                .cliente(cliente)
                .estado(estado)
                .build();

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        for (ListaProductoPedidoDto detalle : nuevoPedido.getDetallePedido()) {
            Producto producto = productoRepository.findById((long) detalle.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getIdProducto()));

            if (producto.getStockActual() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            DetallePedido detallePedido = DetallePedido.builder()
                    .cantidad(detalle.getCantidad())
                    .precioUnitario(detalle.getPrecio())
                    .subtotal(detalle.getSubtotal())
                    .pedido(pedidoGuardado)
                    .producto(producto)
                    .build();

            detallePedidoRepository.save(detallePedido);

            producto.setStockActual(producto.getStockActual() - detalle.getCantidad());
            productoRepository.save(producto);
        }

        return DataResponse.builder()
                .success(true)
                .message("Pedido creado exitosamente con código: " + codigoRecojo)
                .data(codigoRecojo)
                .build();
    }

    @Transactional
    public DataResponse<Object> actualizarPedido(Long idPedido, ActualizarPedidoDto actualizarDto) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getEstado().getNombreEstado().equalsIgnoreCase("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden modificar pedidos en estado PENDIENTE");
        }

        if (actualizarDto.getIdEstado() != null) {
            Estado nuevoEstado = estadoRepository.findById(actualizarDto.getIdEstado())
                    .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
            pedido.setEstado(nuevoEstado);
        }

        pedidoRepository.save(pedido);

        return DataResponse.builder()
                .success(true)
                .message("Pedido actualizado exitosamente")
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<PedidoDetalleRes> obtenerPedidoPorId(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoDetalleRes pedidoRes = mapearPedidoDetalle(pedido);

        return DataResponse.<PedidoDetalleRes>builder()
                .success(true)
                .message("Pedido obtenido exitosamente")
                .data(pedidoRes)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<Page<PedidoRes>> listarPedidos(int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina, limite, Sort.by("fechaPedido").descending());
        Page<Pedido> pedidosPage = pedidoRepository.findAll(pageable);

        Page<PedidoRes> pedidosRes = pedidosPage.map(this::mapearPedido);

        return DataResponse.<Page<PedidoRes>>builder()
                .success(true)
                .message("Lista de pedidos obtenida exitosamente")
                .data(pedidosRes)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<Page<PedidoRes>> listarPedidosPorCliente(Long idCliente, int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina, limite, Sort.by("fechaPedido").descending());
        Page<Pedido> pedidosPage = pedidoRepository.findByClienteIdCliente(idCliente, pageable);

        Page<PedidoRes> pedidosRes = pedidosPage.map(this::mapearPedido);

        return DataResponse.<Page<PedidoRes>>builder()
                .success(true)
                .message("Pedidos del cliente obtenidos exitosamente")
                .data(pedidosRes)
                .build();
    }

    @Transactional
    public DataResponse<Object> cancelarPedido(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstado().getNombreEstado().equalsIgnoreCase("ENTREGADO") ||
                pedido.getEstado().getNombreEstado().equalsIgnoreCase("CANCELADO")) {
            throw new RuntimeException("No se puede cancelar un pedido en este estado");
        }

        Estado estadoCancelado = estadoRepository.findByNombreEstado("CANCELADO")
                .orElseThrow(() -> new RuntimeException("Estado CANCELADO no encontrado en la base de datos"));

        pedido.setEstado(estadoCancelado);

        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        pedidoRepository.save(pedido);

        return DataResponse.builder()
                .success(true)
                .message("Pedido cancelado exitosamente")
                .build();
    }

    @Transactional
    public DataResponse<Object> cambiarEstadoPedido(Long idPedido, Long idEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Estado nuevoEstado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);

        return DataResponse.builder()
                .success(true)
                .message("Estado del pedido actualizado exitosamente")
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<PedidoDetalleRes> buscarPorCodigoRecojo(String codigoRecojo) {
        Pedido pedido = pedidoRepository.findByCodigoRecojo(codigoRecojo)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún pedido con ese código de recojo"));

        PedidoDetalleRes pedidoRes = mapearPedidoDetalle(pedido);

        return DataResponse.<PedidoDetalleRes>builder()
                .success(true)
                .message("Pedido encontrado exitosamente")
                .data(pedidoRes)
                .build();
    }

    private String generarCodigoRecojo() {
        return "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PedidoRes mapearPedido(Pedido pedido) {
        BigDecimal totalPagado = pedido.getPagos() != null
                ? pedido.getPagos().stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                : BigDecimal.ZERO;

        return PedidoRes.builder()
                .idPedido(pedido.getIdPedido())
                .fechaPedido(pedido.getFechaPedido())
                .horaPedido(pedido.getHoraPedido())
                .montoTotal(pedido.getMontoTotal())
                .codigoRecojo(pedido.getCodigoRecojo())
                .nombreCliente(pedido.getCliente().getNombre())
                .estadoPedido(pedido.getEstado().getNombreEstado())
                .totalPagado(totalPagado)
                .build();
    }

    private PedidoDetalleRes mapearPedidoDetalle(Pedido pedido) {
        List<DetallePedidoRes> detalles = pedido.getDetalles().stream()
                .map(detalle -> DetallePedidoRes.builder()
                        .idDetalle(detalle.getIdDetalle())
                        .nombreProducto(detalle.getProducto().getNombre())
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalPagado = pedido.getPagos() != null
                ? pedido.getPagos().stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                : BigDecimal.ZERO;

        return PedidoDetalleRes.builder()
                .idPedido(pedido.getIdPedido())
                .fechaPedido(pedido.getFechaPedido())
                .horaPedido(pedido.getHoraPedido())
                .montoTotal(pedido.getMontoTotal())
                .codigoRecojo(pedido.getCodigoRecojo())
                .nombreCliente(pedido.getCliente().getNombre())
                .telefonoCliente(pedido.getCliente().getTelefono())
                .estadoPedido(pedido.getEstado().getNombreEstado())
                .totalPagado(totalPagado)
                .detalles(detalles)
                .build();
    }
}
