package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ContribuyenteDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SRIService {

    private static final Logger log = LoggerFactory.getLogger(SRIService.class);

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public SRIService() {
        this.webClient = WebClient.builder().build();
    }

    /**
     * Verifica existencia (boolean-like) desde SRI.
     * Cacheado con key RUC.
     */
    @Cacheable(value = "sri-existe", key = "#ruc")
    public boolean existeContribuyente(String ruc) {
        try {
            String url = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet/rest/ConsolidadoContribuyente/existePorNumeroRuc?numeroRuc=" + ruc;
            Mono<String> mono = webClient.get().uri(url).retrieve().bodyToMono(String.class);
            String body = mono.block();
            if (body == null) return false;
            // El servicio puede devolver "true" o "false" o JSON; intentamos parsear
            String trimmed = body.trim().toLowerCase();
            if ("true".equals(trimmed) || trimmed.contains("true")) return true;
            return false;
        } catch (Exception e) {
            log.warn("Error llamando SRI existePorNumeroRuc: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene datos del contribuyente como JsonNode y lo envuelve en ContribuyenteDTO.
     */
    @Cacheable(value = "sri-contribuyente", key = "#ruc")
    public ContribuyenteDTO obtenerContribuyente(String ruc) {
        try {
            String url = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet/rest/ConsolidadoContribuyente/obtenerPorNumerosRuc?&ruc=" + ruc;
            Mono<JsonNode> mono = webClient.get().uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class);
            JsonNode node = mono.block();
            if (node == null) return null;

            // Intentar extraer campos comunes según respuesta: ruc, nombre, tipoPersona
            String nombre = null;
            String tipoPersona = null;
            String rucResp = null;


            if (node.has("ruc")) rucResp = node.get("numeroRuc").asText();
            if (node.has("nombre")) nombre = node.get("razonSocial").asText();
            if (node.has("razonSocial")) nombre = nombre == null ? node.get("razonSocial").asText() : nombre;
            if (node.has("tipoPersona")) tipoPersona = node.get("tipoContribuyente").asText();
            if (node.has("tipo")) tipoPersona = tipoPersona == null ? node.get("tipo").asText() : tipoPersona;

            ContribuyenteDTO dto = new ContribuyenteDTO(rucResp == null ? ruc : rucResp, nombre, tipoPersona, node);

            if (dto.getTipoPersona() != null && !dto.getTipoPersona().isEmpty()) {
                if (!dto.getTipoPersona().toUpperCase().contains("NAT")) {

                    return null;
                }
            }
            return dto;
        } catch (Exception e) {
            log.warn("Error llamando SRI obtenerPorNumerosRuc: {}", e.getMessage());
            return null;
        }
    }
}
