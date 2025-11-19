package org.example.controller;

import org.example.dto.ContribuyenteDTO;
import org.example.dto.VehiculoDTO;
import org.example.service.ANTService;
import org.example.service.SRIService;
import org.example.service.VehiculoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated

public class ConsultaController {

    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);

    private final SRIService sriService;
    private final VehiculoService vehiculoService;
    private final ANTService antService;

    public ConsultaController(SRIService sriService, VehiculoService vehiculoService, ANTService antService) {
        this.sriService = sriService;
        this.vehiculoService = vehiculoService;
        this.antService = antService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/contribuyente/{ruc}")
    public ResponseEntity<?> verificarContribuyente(@PathVariable String ruc) {
        if (ruc == null || ruc.isBlank()) return ResponseEntity.badRequest().body("RUC vacío");
        boolean existe = sriService.existeContribuyente(ruc);
        if (!existe) return ResponseEntity.notFound().build();
        ContribuyenteDTO dto = sriService.obtenerContribuyente(ruc);
        if (dto == null) return ResponseEntity.status(422).body("No es persona natural o no se pudo obtener datos");
        return ResponseEntity.ok(dto);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<?> obtenerVehiculo(@PathVariable String placa) {
        if (placa == null || placa.isBlank()) return ResponseEntity.badRequest().body("Placa vacía");
        VehiculoDTO v = vehiculoService.obtenerPorPlaca(placa);
        if (v == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(v);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/ant/puntos/{cedula}")
    public ResponseEntity<?> obtenerPuntos(@PathVariable String cedula) {
        if (cedula == null || cedula.isBlank()) return ResponseEntity.badRequest().body("Cédula vacía");
        Integer puntos = antService.obtenerPuntosPorCedula(cedula);
        if (puntos == null) {
            return ResponseEntity.status(503).body("ANT no disponible y no existe cache");
        }
        return ResponseEntity.ok(puntos);
    }
}
