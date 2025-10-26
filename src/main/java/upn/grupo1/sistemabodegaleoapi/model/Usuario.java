package upn.grupo1.sistemabodegaleoapi.model;

import jakarta.persistence.*;
import lombok.*;
import upn.grupo1.sistemabodegaleoapi.model.enums.RolEnum;

@Entity
@Table(name = "usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    private String nombreUsuario;

    @Column(nullable = false)
    private String contraseña;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolEnum rol;


}