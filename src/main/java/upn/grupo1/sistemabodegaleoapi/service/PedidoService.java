package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.pedidoDto.CrearPedidoDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.model.Cliente;
import upn.grupo1.sistemabodegaleoapi.model.Estado;
import upn.grupo1.sistemabodegaleoapi.model.repository.ClienteRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.DetallePedidoRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.EstadoRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.PedidoRepository;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteRepository clienteRepository;
    private final EstadoRepository estadoRepository;

    @Transactional
    public DataResponse<Object> crearPedido(CrearPedidoDto nuevoPedido) {
        Cliente cliente = clienteRepository.findByIdCliente(nuevoPedido.getIdCliente()).orElseThrow(
                () -> new RuntimeException("Cliente no encontrado")
        );

        Estado estado = estadoRepository.findByIdEstado(nuevoPedido.getIdEstado()).orElseThrow(
                () -> new RuntimeException("Estado no encontrado")
        );








    }


    //ACTUALIZAR PEDIDO
    //OBTENER UN PEDIDO ID
    //OBTENER LISTA DE PERDIDOS
    //ELIMINAR PEDIDO (ACTIVO - CANCELADO)   ----- ? Crear sp para modificar a cancelado si no se recoge a tiempo


}
