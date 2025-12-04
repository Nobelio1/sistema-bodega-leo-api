package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pagoDto.RegistrarPagoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.pagoRes.PagoRes;
import upn.grupo1.sistemabodegaleoapi.model.Pago;
import upn.grupo1.sistemabodegaleoapi.model.Pedido;
import upn.grupo1.sistemabodegaleoapi.model.enums.EstadoPagoEnum;
import upn.grupo1.sistemabodegaleoapi.model.enums.MetodoEnum;
import upn.grupo1.sistemabodegaleoapi.model.repository.PagoRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.PedidoRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PagoService {
    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final PasarelaPagoService pasarelaPagoService;

    @Transactional
    public DataResponse<Object> registraPago(RegistrarPagoDto pago) {

        Pedido pedido = pedidoRepository.findById(pago.getIdPedido())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + pago.getIdPedido()));

        String tokenTransferencia = null;
        if (pago.getMetodoPago() == MetodoEnum.YAPE || pago.getMetodoPago() == MetodoEnum.PLIN) {
            if (pago.getTransferenciaDetalle() == null ||
                    pago.getTransferenciaDetalle().getTokenTransferencia() == null) {
                return DataResponse.builder()
                        .success(false)
                        .message("Debe proporcionar los detalles de la transferencia para pagos con " +
                                pago.getMetodoPago().name())
                        .build();
            }
            tokenTransferencia = pago.getTransferenciaDetalle().getTokenTransferencia();
        }

        DataResponse<Map<String, Object>> resultadoPasarela = pasarelaPagoService.procesarPago(
                pago.getMonto(),
                pago.getMetodoPago(),
                tokenTransferencia
        );

        if (!resultadoPasarela.getSuccess()) {
            Pago pagoDB = Pago.builder()
                    .metodo(pago.getMetodoPago())
                    .monto(pago.getMonto())
                    .estadoPago(EstadoPagoEnum.FALLIDO)
                    .pedido(pedido)
                    .codigoTransaccion("FALLIDO-" + System.currentTimeMillis())
                    .build();

            pagoRepository.save(pagoDB);

            return DataResponse.builder()
                    .success(false)
                    .message(resultadoPasarela.getMessage())
                    .data(resultadoPasarela.getData())
                    .build();
        }

        Map<String, Object> datosTransaccion = resultadoPasarela.getData();
        String codigoTransaccion = (String) datosTransaccion.get("codigoTransaccion");

        EstadoPagoEnum estadoPago = pago.getMetodoPago() == MetodoEnum.EFECTIVO
                ? EstadoPagoEnum.PENDIENTE
                : EstadoPagoEnum.CONFIRMADO;

        Pago pagoDB = Pago.builder()
                .metodo(pago.getMetodoPago())
                .monto(pago.getMonto())
                .estadoPago(estadoPago)
                .pedido(pedido)
                .codigoTransaccion(codigoTransaccion)
                .build();

        pagoRepository.save(pagoDB);

        return DataResponse.builder()
                .success(true)
                .message("Pago registrado exitosamente")
                .data(datosTransaccion)
                .build();
    }


    @Transactional(readOnly = true)
    public DataResponse<List<PagoRes>> obtenerPagoPorIdPedido(Long idPedido) {
        List<Pago> pagos = pagoRepository.findByPedidoIdPedido(idPedido);

        List<PagoRes> pago = pagos.stream().map(p -> PagoRes.builder()
                        .idPago(p.getIdPago())
                        .monto(p.getMonto())
                        .metodo(p.getMetodo().name())
                        .estadoPago(p.getEstadoPago().name())
                        .fechaPago(p.getFechaPago())
                        .codigoTransaccion(p.getCodigoTransaccion())
                        .build())
                .toList();

        return DataResponse.<List<PagoRes>>builder()
                .success(true)
                .message("Pagos encontrados exitosamente")
                .data(pago)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<PagoRes> obtenerPagoPorId(Long idPago) {
        Pago pago = pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con ID: " + idPago));

        PagoRes pagoRes = PagoRes.builder()
                .idPago(pago.getIdPago())
                .monto(pago.getMonto())
                .metodo(pago.getMetodo().name())
                .estadoPago(pago.getEstadoPago().name())
                .fechaPago(pago.getFechaPago())
                .codigoTransaccion(pago.getCodigoTransaccion())
                .build();

        return DataResponse.<PagoRes>builder()
                .success(true)
                .message("Pago encontrado exitosamente")
                .data(pagoRes)
                .build();
    }

    @Transactional
    public DataResponse<Object> confirmarPago(Long idPago) {
        Pago pago = pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con ID: " + idPago));

        if (pago.getEstadoPago() == EstadoPagoEnum.CONFIRMADO) {
            return DataResponse.builder()
                    .success(false)
                    .message("El pago ya está confirmado")
                    .build();
        }

        pago.setEstadoPago(EstadoPagoEnum.CONFIRMADO);
        pagoRepository.save(pago);

        return DataResponse.builder()
                .success(true)
                .message("Pago confirmado exitosamente")
                .build();
    }

    @Transactional
    public DataResponse<Object> eliminarPago(Long idPago) {
        Pago pago = pagoRepository.findByIdPago(idPago)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con ID: " + idPago));

        pago.setEstadoPago(EstadoPagoEnum.FALLIDO);
        pagoRepository.delete(pago);

        return DataResponse.builder()
                .success(true)
                .message("Pago eliminado exitosamente")
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<Map<String, Object>> verificarTransaccion(String codigoTransaccion) {
        return pasarelaPagoService.verificarTransaccion(codigoTransaccion);
    }
}
