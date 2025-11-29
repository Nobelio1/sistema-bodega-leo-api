package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upn.grupo1.sistemabodegaleoapi.config.JwtService;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.LoginRequest;
import upn.grupo1.sistemabodegaleoapi.controller.dto.request.RegisterRequest;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.AuthResponse;
import upn.grupo1.sistemabodegaleoapi.model.Usuario;
import upn.grupo1.sistemabodegaleoapi.model.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var usuario = Usuario.builder()
                .nombreUsuario(request.getNombreUsuario())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .rol(request.getRol())
                .build();

        usuarioRepository.save(usuario);

        var jwtToken = jwtService.generateToken(usuario);

        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNombreUsuario(),
                        request.getContrasena()
                )
        );

        var usuario = usuarioRepository.findByNombreUsuario(request.getNombreUsuario())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(usuario);

        return new AuthResponse(jwtToken);
    }
}