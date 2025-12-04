package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.comprobanteDto.GenerarComprobanteDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.comprobanteRes.ComprobanteRes;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.comprobanteRes.DetalleComprobanteRes;
import upn.grupo1.sistemabodegaleoapi.model.*;
import upn.grupo1.sistemabodegaleoapi.model.enums.EstadoPagoEnum;
import upn.grupo1.sistemabodegaleoapi.model.repository.ComprobanteRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.DatosFacturaRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.PedidoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComprobanteService {

    private final ComprobanteRepository comprobanteRepository;
    private final DatosFacturaRepository datosFacturaRepository;
    private final PedidoRepository pedidoRepository;

    private static final BigDecimal IGV_RATE = new BigDecimal("0.18"); // 18% IGV en Perú

    @Transactional
    public DataResponse<ComprobanteRes> generarComprobante(GenerarComprobanteDto dto) {

        // Validar que el pedido existe
        Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + dto.getIdPedido()));

        // Validar que el pedido no tenga ya un comprobante
        if (comprobanteRepository.existsByPedidoIdPedido(dto.getIdPedido())) {
            return DataResponse.<ComprobanteRes>builder()
                    .success(false)
                    .message("Este pedido ya tiene un comprobante generado")
                    .build();
        }

        // Validar que el pedido tenga al menos un pago confirmado
        boolean tienePagoConfirmado = pedido.getPagos().stream()
                .anyMatch(pago -> pago.getEstadoPago() == EstadoPagoEnum.CONFIRMADO);

        if (!tienePagoConfirmado) {
            return DataResponse.<ComprobanteRes>builder()
                    .success(false)
                    .message("El pedido debe tener al menos un pago confirmado para generar el comprobante")
                    .build();
        }

        // Validar datos de factura si el tipo es FACTURA
        if (dto.getTipoComprobante() == Comprobante.Tipo.FACTURA) {
            if (dto.getDatosFactura() == null) {
                return DataResponse.<ComprobanteRes>builder()
                        .success(false)
                        .message("Debe proporcionar los datos fiscales para generar una factura")
                        .build();
            }
        }

        // Calcular montos
        BigDecimal subtotal = calcularSubtotal(pedido.getMontoTotal());
        BigDecimal igv = calcularIGV(subtotal);
        BigDecimal montoTotal = pedido.getMontoTotal();

        // Generar número de comprobante
        String numeroComprobante = generarNumeroComprobante(dto.getTipoComprobante());

        // Crear comprobante
        Comprobante comprobante = Comprobante.builder()
                .tipo(dto.getTipoComprobante())
                .numero(numeroComprobante)
                .subtotal(subtotal)
                .igv(igv)
                .montoTotal(montoTotal)
                .pedido(pedido)
                .build();

        Comprobante comprobanteGuardado = comprobanteRepository.save(comprobante);

        // Si es factura, guardar datos fiscales
        if (dto.getTipoComprobante() == Comprobante.Tipo.FACTURA) {
            DatosFactura datosFactura = DatosFactura.builder()
                    .ruc(dto.getDatosFactura().getRuc())
                    .razonSocial(dto.getDatosFactura().getRazonSocial())
                    .direccionFiscal(dto.getDatosFactura().getDireccionFiscal())
                    .comprobante(comprobanteGuardado)
                    .build();

            datosFacturaRepository.save(datosFactura);
            comprobanteGuardado.setDatosFactura(datosFactura);
        }

        ComprobanteRes comprobanteRes = mapearComprobante(comprobanteGuardado);

        return DataResponse.<ComprobanteRes>builder()
                .success(true)
                .message("Comprobante generado exitosamente: " + numeroComprobante)
                .data(comprobanteRes)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<ComprobanteRes> obtenerComprobantePorId(Long idComprobante) {
        Comprobante comprobante = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado con ID: " + idComprobante));

        ComprobanteRes comprobanteRes = mapearComprobante(comprobante);

        return DataResponse.<ComprobanteRes>builder()
                .success(true)
                .message("Comprobante obtenido exitosamente")
                .data(comprobanteRes)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<ComprobanteRes> obtenerComprobantePorNumero(String numero) {
        Comprobante comprobante = comprobanteRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado con número: " + numero));

        ComprobanteRes comprobanteRes = mapearComprobante(comprobante);

        return DataResponse.<ComprobanteRes>builder()
                .success(true)
                .message("Comprobante obtenido exitosamente")
                .data(comprobanteRes)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<ComprobanteRes> obtenerComprobantePorPedido(Long idPedido) {
        Comprobante comprobante = comprobanteRepository.findByPedidoIdPedido(idPedido)
                .orElseThrow(() -> new RuntimeException("No se encontró comprobante para el pedido: " + idPedido));

        ComprobanteRes comprobanteRes = mapearComprobante(comprobante);

        return DataResponse.<ComprobanteRes>builder()
                .success(true)
                .message("Comprobante obtenido exitosamente")
                .data(comprobanteRes)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<Page<ComprobanteRes>> listarComprobantes(int pagina, int limite, String tipo) {
        Pageable pageable = PageRequest.of(pagina, limite, Sort.by("fechaEmision").descending());

        Page<Comprobante> comprobantesPage;

        if (tipo != null && !tipo.isEmpty()) {
            Comprobante.Tipo tipoEnum = Comprobante.Tipo.valueOf(tipo.toUpperCase());
            comprobantesPage = comprobanteRepository.findByTipo(tipoEnum, pageable);
        } else {
            comprobantesPage = comprobanteRepository.findAll(pageable);
        }

        Page<ComprobanteRes> comprobantesRes = comprobantesPage.map(this::mapearComprobante);

        return DataResponse.<Page<ComprobanteRes>>builder()
                .success(true)
                .message("Lista de comprobantes obtenida exitosamente")
                .data(comprobantesRes)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<List<ComprobanteRes>> listarComprobantesPorFecha(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        List<Comprobante> comprobantes = comprobanteRepository.findByFechaEmisionBetween(
                fechaInicio, fechaFin);

        List<ComprobanteRes> comprobantesRes = comprobantes.stream()
                .map(this::mapearComprobante)
                .collect(Collectors.toList());

        return DataResponse.<List<ComprobanteRes>>builder()
                .success(true)
                .message("Comprobantes del período obtenidos exitosamente")
                .data(comprobantesRes)
                .build();
    }

    @Transactional
    public DataResponse<Object> anularComprobante(Long idComprobante) {
        Comprobante comprobante = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado con ID: " + idComprobante));

        // En un sistema real, aquí se marcaría como anulado pero no se eliminaría
        // Por simplificación, lo eliminamos
        comprobanteRepository.delete(comprobante);

        return DataResponse.builder()
                .success(true)
                .message("Comprobante anulado exitosamente")
                .build();
    }

    // Métodos auxiliares

    private String generarNumeroComprobante(Comprobante.Tipo tipo) {
        String serie = tipo == Comprobante.Tipo.BOLETA ? "B001" : "F001";

        // Obtener el último comprobante del tipo
        Pageable pageable = PageRequest.of(0, 1);
        List<Comprobante> ultimosComprobantes = comprobanteRepository
                .findFirstByTipoOrderByNumeroDesc(tipo, pageable);

        int siguienteNumero = 1;

        if (!ultimosComprobantes.isEmpty()) {
            String ultimoNumero = ultimosComprobantes.get(0).getNumero();
            // Extraer el número: B001-00000123 -> 123
            String[] partes = ultimoNumero.split("-");
            if (partes.length == 2) {
                siguienteNumero = Integer.parseInt(partes[1]) + 1;
            }
        }

        // Formato: B001-00000001 o F001-00000001
        return String.format("%s-%08d", serie, siguienteNumero);
    }

    private BigDecimal calcularSubtotal(BigDecimal montoTotal) {
        // Subtotal = MontoTotal / (1 + IGV)
        return montoTotal.divide(BigDecimal.ONE.add(IGV_RATE), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularIGV(BigDecimal subtotal) {
        // IGV = Subtotal * 0.18
        return subtotal.multiply(IGV_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    private ComprobanteRes mapearComprobante(Comprobante comprobante) {
        Pedido pedido = comprobante.getPedido();
        Cliente cliente = pedido.getCliente();

        // Mapear detalles del pedido
        List<DetalleComprobanteRes> detalles = pedido.getDetalles().stream()
                .map(detalle -> DetalleComprobanteRes.builder()
                        .descripcion(detalle.getProducto().getNombre())
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        ComprobanteRes.ComprobanteResBuilder builder = ComprobanteRes.builder()
                .idComprobante(comprobante.getIdComprobante())
                .tipo(comprobante.getTipo().name())
                .numero(comprobante.getNumero())
                .fechaEmision(comprobante.getFechaEmision())
                .subtotal(comprobante.getSubtotal())
                .igv(comprobante.getIgv())
                .montoTotal(comprobante.getMontoTotal())
                .nombreCliente(cliente.getNombre())
                .documentoCliente(cliente.getTelefono()) // Podría ser DNI si lo agregas
                .direccionCliente(cliente.getDireccion())
                .codigoPedido(pedido.getCodigoRecojo())
                .detalles(detalles);

        // Si es factura, agregar datos fiscales
        if (comprobante.getDatosFactura() != null) {
            DatosFactura datosFactura = comprobante.getDatosFactura();
            builder.ruc(datosFactura.getRuc())
                    .razonSocial(datosFactura.getRazonSocial())
                    .direccionFiscal(datosFactura.getDireccionFiscal());
        }

        return builder.build();
    }
}
