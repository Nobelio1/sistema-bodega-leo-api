package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.clienteDto.CrearClienteDto;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.clienteRes.ClienteRes;
import upn.grupo1.sistemabodegaleoapi.model.Cliente;
import upn.grupo1.sistemabodegaleoapi.model.Usuario;
import upn.grupo1.sistemabodegaleoapi.model.enums.RolEnum;
import upn.grupo1.sistemabodegaleoapi.model.repository.ClienteRepository;
import upn.grupo1.sistemabodegaleoapi.model.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public DataResponse<Object> crearCliente(CrearClienteDto nuevoCliente) {
        if (usuarioRepository.findByNombreUsuario(nuevoCliente.getNombreUsuario()).isPresent()) {
            return DataResponse.builder()
                    .success(false)
                    .message("El nombre de usuario ya está en uso")
                    .build();
        }

        Usuario usuario = Usuario.builder()
                .nombreUsuario(nuevoCliente.getNombreUsuario())
                .contrasena(passwordEncoder.encode(nuevoCliente.getContrasena()))
                .rol(RolEnum.CLIENTE)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Cliente cliente = Cliente.builder()
                .nombre(nuevoCliente.getNombre())
                .telefono(nuevoCliente.getTelefono())
                .direccion(nuevoCliente.getDireccion())
                .correo(nuevoCliente.getCorreo())
                .usuario(usuarioGuardado)
                .build();

        clienteRepository.save(cliente);

        return DataResponse.builder()
                .success(true)
                .message("Cliente registrado exitosamente")
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<ClienteRes> obtenerClientePorId(Long idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        ClienteRes clienteRes = mapearCliente(cliente);

        return DataResponse.<ClienteRes>builder()
                .success(true)
                .message("Cliente obtenido exitosamente")
                .data(clienteRes)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponse<List<ClienteRes>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();

        List<ClienteRes> clientesRes = clientes.stream()
                .map(this::mapearCliente)
                .collect(Collectors.toList());

        return DataResponse.<List<ClienteRes>>builder()
                .success(true)
                .message("Lista de clientes obtenida exitosamente")
                .data(clientesRes)
                .build();
    }

    @Transactional
    public DataResponse<Object> actualizarCliente(Long idCliente, CrearClienteDto clienteDto) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(clienteDto.getNombre());
        cliente.setTelefono(clienteDto.getTelefono());
        cliente.setDireccion(clienteDto.getDireccion());
        cliente.setCorreo(clienteDto.getCorreo());

        clienteRepository.save(cliente);

        return DataResponse.builder()
                .success(true)
                .message("Cliente actualizado exitosamente")
                .build();
    }

    private ClienteRes mapearCliente(Cliente cliente) {
        return ClienteRes.builder()
                .idCliente(cliente.getIdCliente())
                .nombre(cliente.getNombre())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .correo(cliente.getCorreo())
                .nombreUsuario(cliente.getUsuario() != null ? cliente.getUsuario().getNombreUsuario() : null)
                .build();
    }
}
