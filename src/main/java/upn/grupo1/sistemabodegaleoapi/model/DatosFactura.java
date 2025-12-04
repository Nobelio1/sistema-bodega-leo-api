package upn.grupo1.sistemabodegaleoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "datos_factura")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatosFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDatosFactura;

    @Column(nullable = false, length = 11)
    private String ruc;

    @Column(nullable = false)
    private String razonSocial;

    @Column(nullable = false)
    private String direccionFiscal;

    @OneToOne
    @JoinColumn(name = "id_comprobante")
    private Comprobante comprobante;
}
