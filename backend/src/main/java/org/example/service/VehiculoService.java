package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.dto.VehiculoDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class VehiculoService {

    private static final Logger log = LoggerFactory.getLogger(VehiculoService.class);

    private final WebClient webClient;

    public VehiculoService() {
        this.webClient = WebClient.builder().build();
    }

    @Cacheable(value = "vehiculo", key = "#placa")
    public VehiculoDTO obtenerPorPlaca(String placa) {
        try {
            String url = "https://srienlinea.sri.gob.ec/sri-matriculacion-vehicular-recaudacion-servicio-internet/rest/BaseVehiculo/obtenerPorNumeroPlacaOPorNumeroCampvOPorNumeroCpn?numeroPlacaCampvCpn=" + placa;
            Mono<JsonNode> mono = webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class);
            JsonNode node = mono.block();
            if (node == null) return null;

            String marca = null, modelo = null, placaResp = placa;
            if (node.has("marca")) marca = node.get("marca").asText();
            if (node.has("modelo")) modelo = node.get("modelo").asText();
            if (node.has("placa")) placaResp = node.get("placa").asText();

            return new VehiculoDTO(placaResp, marca, modelo, node);
        } catch (Exception e) {
            log.warn("Error llamando servicio vehicular: {}", e.getMessage());
            return null;
        }
    }
}
