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
import upn.grupo1.sistemabodegaleoapi.model.repository.PagoRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.PedidoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {
    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public DataResponse<Object> registraPago(RegistrarPagoDto pago) {

        Pedido pedido = pedidoRepository.findById(pago.getIdPedido())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + pago.getIdPedido()));

        Pago pagoDB = Pago.builder()
                .metodo(pago.getMetodoPago())
                .monto(pago.getMonto())
                .estadoPago(EstadoPagoEnum.CONFIRMADO)
                .pedido(pedido)
                .codigoTransaccion(pago.getTransferenciaDetalle().getTokenTransferencia())
                .build();

        pagoRepository.save(pagoDB);

        return DataResponse.builder()
                .success(true)
                .message("Pago registrado exitosamente")
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
                        .build())
                .toList();

        return DataResponse.<List<PagoRes>>builder()
                .success(true)
                .message("Pago encontrado exitosamente")
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
                .build();

        return DataResponse.<PagoRes>builder()
                .success(true)
                .message("Pago encontrado exitosamente")
                .data(pagoRes)
                .build();
    }


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

}
