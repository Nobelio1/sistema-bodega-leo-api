package upn.grupo1.sistemabodegaleoapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.comprobanteRes.ComprobanteRes;
import upn.grupo1.sistemabodegaleoapi.service.ComprobantePDFService;
import upn.grupo1.sistemabodegaleoapi.service.ComprobanteService;

@RestController
@RequestMapping("/comprobante")
@RequiredArgsConstructor
@Tag(name = "Comprobante - Descarga", description = "Endpoints para descargar comprobantes en diferentes formatos")
@SecurityRequirement(name = "bearerAuth")
public class ComprobanteDescargarController {

    private final ComprobanteService comprobanteService;
    private final ComprobantePDFService comprobantePDFService;

    @GetMapping("/{id}/pdf")
    @Operation(
            summary = "Descargar comprobante en PDF",
            description = "Descarga el comprobante en formato PDF"
    )
    public ResponseEntity<byte[]> descargarPDF(@PathVariable Long id) {
        ComprobanteRes comprobante = comprobanteService.obtenerComprobantePorId(id).getData();

        byte[] pdfBytes = comprobantePDFService.generarPDF(comprobante);

        String filename = comprobante.getNumero().replace("-", "_") + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/{id}/html")
    @Operation(
            summary = "Ver comprobante en HTML",
            description = "Visualiza el comprobante en formato HTML"
    )
    public ResponseEntity<String> verHTML(@PathVariable Long id) {
        ComprobanteRes comprobante = comprobanteService.obtenerComprobantePorId(id).getData();

        String html = comprobantePDFService.generarHTMLComprobante(comprobante);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @GetMapping("/{id}/termico")
    @Operation(
            summary = "Obtener formato para impresora térmica",
            description = "Obtiene el comprobante en formato de texto para impresoras térmicas"
    )
    public ResponseEntity<String> obtenerFormatoTermico(@PathVariable Long id) {
        ComprobanteRes comprobante = comprobanteService.obtenerComprobantePorId(id).getData();

        String texto = comprobantePDFService.generarTextoComprobanteTermico(comprobante);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(texto);
    }

    @GetMapping("/pedido/{idPedido}/pdf")
    @Operation(
            summary = "Descargar comprobante por pedido",
            description = "Descarga el comprobante asociado a un pedido en formato PDF"
    )
    public ResponseEntity<byte[]> descargarPDFPorPedido(@PathVariable Long idPedido) {
        ComprobanteRes comprobante = comprobanteService.obtenerComprobantePorPedido(idPedido).getData();

        byte[] pdfBytes = comprobantePDFService.generarPDF(comprobante);

        String filename = comprobante.getNumero().replace("-", "_") + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
