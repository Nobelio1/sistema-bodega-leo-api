package upn.grupo1.sistemabodegaleoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import upn.grupo1.sistemabodegaleoapi.controller.dto.response.DataResponse;
import upn.grupo1.sistemabodegaleoapi.model.enums.MetodoEnum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasarelaPagoService {

    public DataResponse<Map<String, Object>> procesarPago(
            BigDecimal monto,
            MetodoEnum metodo,
            String tokenTransferencia) {

        Map<String, Object> resultado = new HashMap<>();

        try {
            Thread.sleep(1000);

            String codigoTransaccion = generarCodigoTransaccion();

            boolean pagoExitoso = false;
            String mensajePasarela = "";

            switch (metodo) {
                case YAPE:
                    pagoExitoso = validarPagoYape(tokenTransferencia, monto);
                    mensajePasarela = pagoExitoso
                            ? "Pago procesado exitosamente con Yape"
                            : "Error al procesar pago con Yape. Token inválido";
                    break;

                case PLIN:
                    pagoExitoso = validarPagoPlin(tokenTransferencia, monto);
                    mensajePasarela = pagoExitoso
                            ? "Pago procesado exitosamente con Plin"
                            : "Error al procesar pago con Plin. Token inválido";
                    break;

                case EFECTIVO:
                    pagoExitoso = true;
                    mensajePasarela = "Pago en efectivo registrado. Debe pagar al recoger el pedido";
                    codigoTransaccion = "EFECTIVO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    break;
            }

            resultado.put("exitoso", pagoExitoso);
            resultado.put("codigoTransaccion", codigoTransaccion);
            resultado.put("metodo", metodo.name());
            resultado.put("monto", monto);
            resultado.put("mensajePasarela", mensajePasarela);

            return DataResponse.<Map<String, Object>>builder()
                    .success(pagoExitoso)
                    .message(mensajePasarela)
                    .data(resultado)
                    .build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            resultado.put("exitoso", false);
            resultado.put("error", "Error al procesar el pago");

            return DataResponse.<Map<String, Object>>builder()
                    .success(false)
                    .message("Error al procesar el pago")
                    .data(resultado)
                    .build();
        }
    }

    private boolean validarPagoYape(String token, BigDecimal monto) {
        if (token == null || token.length() < 10) {
            return false;
        }

        Random random = new Random();
        return random.nextInt(100) < 95;
    }

    private boolean validarPagoPlin(String token, BigDecimal monto) {
        if (token == null || token.length() < 10) {
            return false;
        }

        Random random = new Random();
        return random.nextInt(100) < 95;
    }

    private String generarCodigoTransaccion() {
        return "TXN-" + System.currentTimeMillis() + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public DataResponse<Map<String, Object>> verificarTransaccion(String codigoTransaccion) {
        Map<String, Object> resultado = new HashMap<>();

        resultado.put("codigoTransaccion", codigoTransaccion);
        resultado.put("estado", "CONFIRMADO");
        resultado.put("mensaje", "Transacción confirmada exitosamente");

        return DataResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Transacción verificada")
                .data(resultado)
                .build();
    }
}
