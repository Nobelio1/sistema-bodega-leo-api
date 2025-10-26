package upn.grupo1.sistemabodegaleoapi.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trabajador")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTrabajador;

    private String nombre;
    private String dni;
    private String rol;
    private String telefono;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}