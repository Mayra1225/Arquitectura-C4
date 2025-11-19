package org.example.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ANTService {

    private static final Logger log = LoggerFactory.getLogger(ANTService.class);
    private final WebClient webClient;

    public ANTService() {
        this.webClient = WebClient.builder().build();
    }

    @Cacheable(value = "ant-puntos", key = "#cedula")
    @CircuitBreaker(name = "antService", fallbackMethod = "fallbackPuntos")
    @Retry(name = "antService")
    public Integer obtenerPuntosPorCedula(String cedula) {
        try {
            String url = "https://consultaweb.ant.gob.ec/PortalWEB/paginas/clientes/clp_grid_citaciones.jsp?ps_tipo_identificacion=CED&ps_identificacion=" + cedula + "&ps_placa=";
            String html = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();
            if (html == null || html.isEmpty()) {
                log.warn("ANT devolvió body vacío para cedula {}", cedula);
                return null;
            }
            return parsePuntosFromHtml(html);
        } catch (Exception e) {
            log.warn("Error consultando ANT: {}", e.getMessage());
            throw e; // para que circuit breaker funcione (fallback se invoca)
        }
    }

    // Fallback method para CircuitBreaker (cuando falla)
    public Integer fallbackPuntos(String cedula, Throwable t) {
        log.warn("Fallback ANT invoked for {} -> {}", cedula, t.toString());
        // Retornamos null para que el controlador pueda decidir (503 si no hay cache)
        return null;
    }

    private Integer parsePuntosFromHtml(String html) {
        try {
            Document doc = Jsoup.parse(html);
            String text = doc.text();
            // Buscamos patrones tipo "Puntos: 5" o "5 puntos" o "PUNTOS 5"
            Pattern p = Pattern.compile("(Puntos[:]?\\s*([0-9]{1,3}))|([0-9]{1,3}\\s*puntos?)", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(text);
            if (m.find()) {
                String match = m.group();
                // Extraer primer número
                Pattern num = Pattern.compile("([0-9]{1,3})");
                Matcher mn = num.matcher(match);
                if (mn.find()) {
                    return Integer.parseInt(mn.group(1));
                }
            }
            // Si no encontramos, intentamos buscar cualquier número en la pagina (last resource)
            Pattern anyNum = Pattern.compile("([0-9]{1,3})");
            Matcher anyM = anyNum.matcher(text);
            if (anyM.find()) {
                return Integer.parseInt(anyM.group(1));
            }
            return 0;
        } catch (Exception e) {
            log.warn("Error parseando HTML ANT: {}", e.getMessage());
            return 0;
        }
    }
}
