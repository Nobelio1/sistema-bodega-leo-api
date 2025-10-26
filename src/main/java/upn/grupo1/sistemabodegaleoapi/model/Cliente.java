package upn.grupo1.sistemabodegaleoapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    private String nombre;
    private String telefono;
    private String direccion;
    private String correo;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
