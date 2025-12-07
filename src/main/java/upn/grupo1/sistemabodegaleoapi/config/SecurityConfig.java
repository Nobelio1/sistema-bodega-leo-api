package upn.grupo1.sistemabodegaleoapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/cliente/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/producto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categoria/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Endpoints para ADMIN y TRABAJADOR
                        .requestMatchers(HttpMethod.POST, "/producto/**").hasAnyAuthority("ADMIN", "TRABAJADOR")
                        .requestMatchers(HttpMethod.PUT, "/producto/**").hasAnyAuthority("ADMIN", "TRABAJADOR")
                        .requestMatchers(HttpMethod.PATCH, "/producto/**").hasAnyAuthority("ADMIN", "TRABAJADOR")
                        .requestMatchers(HttpMethod.DELETE, "/producto/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/categoria/**").hasAnyAuthority("ADMIN", "TRABAJADOR")
                        .requestMatchers(HttpMethod.PUT, "/categoria/**").hasAnyAuthority("ADMIN", "TRABAJADOR")
                        .requestMatchers(HttpMethod.DELETE, "/categoria/**").hasAuthority("ADMIN")

                        // Endpoints de pedidos - Clientes pueden crear y ver sus propios pedidos
                        .requestMatchers(HttpMethod.POST, "/pedido").hasAnyAuthority("CLIENTE", "TRABAJADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/pedido/cliente/**").hasAnyAuthority("CLIENTE", "TRABAJADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/pedido/**").hasAnyAuthority("TRABAJADOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/pedido/**").hasAnyAuthority("TRABAJADOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/pedido/**").hasAnyAuthority("TRABAJADOR", "ADMIN")

                        // Endpoints de pagos
                        .requestMatchers(HttpMethod.POST, "/pago").hasAnyAuthority("CLIENTE", "TRABAJADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/pago/**").hasAnyAuthority("CLIENTE", "TRABAJADOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/pago/**").hasAnyAuthority("TRABAJADOR", "ADMIN")

                        // Endpoints de comprobantes
                        .requestMatchers(HttpMethod.POST, "/comprobante").hasAnyAuthority("TRABAJADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/comprobante/**").hasAnyAuthority("CLIENTE", "TRABAJADOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/comprobante/**").hasAuthority("ADMIN")

                        // Endpoints de clientes
                        .requestMatchers("/cliente/**").hasAnyAuthority("TRABAJADOR", "ADMIN")

                        // Todos los demás requieren autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}