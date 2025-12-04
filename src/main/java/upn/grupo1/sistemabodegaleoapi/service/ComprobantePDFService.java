package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.comprobanteRes.ComprobanteRes;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.comprobanteRes.DetalleComprobanteRes;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ComprobantePDFService {

    public String generarHTMLComprobante(ComprobanteRes comprobante) {
        StringBuilder html = new StringBuilder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = comprobante.getFechaEmision().format(formatter);

        html.append("<!DOCTYPE html>");
        html.append("<html lang='es'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>").append(comprobante.getTipo()).append(" ").append(comprobante.getNumero()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append(".header { text-align: center; border-bottom: 2px solid #000; padding-bottom: 10px; margin-bottom: 20px; }");
        html.append(".info-section { margin-bottom: 20px; }");
        html.append(".info-label { font-weight: bold; display: inline-block; width: 150px; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append(".totals { margin-top: 20px; text-align: right; }");
        html.append(".total-row { margin: 5px 0; }");
        html.append(".total-label { display: inline-block; width: 200px; font-weight: bold; }");
        html.append(".footer { margin-top: 40px; text-align: center; font-size: 12px; color: #666; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

        html.append("<div class='header'>");
        html.append("<h1>BODEGA LEO</h1>");
        html.append("<p>RUC: 20123456789</p>");
        html.append("<p>Av. Principal 123, Lima - Perú</p>");
        html.append("<p>Tel: (01) 234-5678</p>");
        html.append("<h2>").append(comprobante.getTipo()).append("</h2>");
        html.append("<h3>").append(comprobante.getNumero()).append("</h3>");
        html.append("</div>");

        html.append("<div class='info-section'>");
        html.append("<div><span class='info-label'>Fecha de Emisión:</span> ").append(fechaFormateada).append("</div>");
        html.append("<div><span class='info-label'>Código de Pedido:</span> ").append(comprobante.getCodigoPedido()).append("</div>");
        html.append("</div>");

        html.append("<div class='info-section'>");
        html.append("<h3>DATOS DEL CLIENTE</h3>");

        if (comprobante.getTipo().equals("FACTURA")) {
            html.append("<div><span class='info-label'>RUC:</span> ").append(comprobante.getRuc()).append("</div>");
            html.append("<div><span class='info-label'>Razón Social:</span> ").append(comprobante.getRazonSocial()).append("</div>");
            html.append("<div><span class='info-label'>Dirección Fiscal:</span> ").append(comprobante.getDireccionFiscal()).append("</div>");
        } else {
            html.append("<div><span class='info-label'>Nombre:</span> ").append(comprobante.getNombreCliente()).append("</div>");
            html.append("<div><span class='info-label'>Documento:</span> ").append(comprobante.getDocumentoCliente()).append("</div>");
            html.append("<div><span class='info-label'>Dirección:</span> ").append(comprobante.getDireccionCliente()).append("</div>");
        }
        html.append("</div>");

        html.append("<h3>DETALLE DE PRODUCTOS</h3>");
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Descripción</th>");
        html.append("<th style='text-align: center;'>Cantidad</th>");
        html.append("<th style='text-align: right;'>P. Unitario</th>");
        html.append("<th style='text-align: right;'>Subtotal</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");

        for (DetalleComprobanteRes detalle : comprobante.getDetalles()) {
            html.append("<tr>");
            html.append("<td>").append(detalle.getDescripcion()).append("</td>");
            html.append("<td style='text-align: center;'>").append(detalle.getCantidad()).append("</td>");
            html.append("<td style='text-align: right;'>S/ ").append(formatearMonto(detalle.getPrecioUnitario())).append("</td>");
            html.append("<td style='text-align: right;'>S/ ").append(formatearMonto(detalle.getSubtotal())).append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody>");
        html.append("</table>");

        html.append("<div class='totals'>");
        html.append("<div class='total-row'>");
        html.append("<span class='total-label'>SUBTOTAL:</span> S/ ").append(formatearMonto(comprobante.getSubtotal()));
        html.append("</div>");
        html.append("<div class='total-row'>");
        html.append("<span class='total-label'>IGV (18%):</span> S/ ").append(formatearMonto(comprobante.getIgv()));
        html.append("</div>");
        html.append("<div class='total-row' style='font-size: 18px; font-weight: bold;'>");
        html.append("<span class='total-label'>TOTAL:</span> S/ ").append(formatearMonto(comprobante.getMontoTotal()));
        html.append("</div>");
        html.append("</div>");

        html.append("<div class='footer'>");
        html.append("<p>Gracias por su compra</p>");
        html.append("<p>Este comprobante es una representación impresa del comprobante electrónico</p>");
        html.append("</div>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    public String generarTextoComprobanteTermico(ComprobanteRes comprobante) {
        StringBuilder texto = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        texto.append("=====================================\n");
        texto.append("          BODEGA LEO\n");
        texto.append("       RUC: 20123456789\n");
        texto.append("   Av. Principal 123, Lima\n");
        texto.append("      Tel: (01) 234-5678\n");
        texto.append("=====================================\n\n");

        texto.append("    ").append(comprobante.getTipo()).append("\n");
        texto.append("    ").append(comprobante.getNumero()).append("\n\n");

        texto.append("Fecha: ").append(comprobante.getFechaEmision().format(formatter)).append("\n");
        texto.append("Pedido: ").append(comprobante.getCodigoPedido()).append("\n\n");

        if (comprobante.getTipo().equals("FACTURA")) {
            texto.append("RUC: ").append(comprobante.getRuc()).append("\n");
            texto.append("Razón Social: ").append(comprobante.getRazonSocial()).append("\n\n");
        } else {
            texto.append("Cliente: ").append(comprobante.getNombreCliente()).append("\n\n");
        }

        texto.append("-------------------------------------\n");
        texto.append("DETALLE\n");
        texto.append("-------------------------------------\n");

        for (DetalleComprobanteRes detalle : comprobante.getDetalles()) {
            texto.append(detalle.getDescripcion()).append("\n");
            texto.append(String.format("%d x S/ %s = S/ %s\n",
                    detalle.getCantidad(),
                    formatearMonto(detalle.getPrecioUnitario()),
                    formatearMonto(detalle.getSubtotal())
            ));
        }

        texto.append("-------------------------------------\n");
        texto.append(String.format("SUBTOTAL:      S/ %s\n", formatearMonto(comprobante.getSubtotal())));
        texto.append(String.format("IGV (18%%):     S/ %s\n", formatearMonto(comprobante.getIgv())));
        texto.append("=====================================\n");
        texto.append(String.format("TOTAL:         S/ %s\n", formatearMonto(comprobante.getMontoTotal())));
        texto.append("=====================================\n\n");

        texto.append("      Gracias por su compra\n");
        texto.append("=====================================\n");

        return texto.toString();
    }

    private String formatearMonto(BigDecimal monto) {
        return String.format("%.2f", monto);
    }

    public byte[] generarPDF(ComprobanteRes comprobante) {

        String html = generarHTMLComprobante(comprobante);

        return html.getBytes();
    }
}
