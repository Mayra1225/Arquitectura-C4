package org.example.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class ContribuyenteDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ruc;
    private String nombre;
    private String tipoPersona;
    private JsonNode raw;

    public ContribuyenteDTO() {}

    public ContribuyenteDTO(String ruc, String nombre, String tipoPersona, JsonNode raw) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.tipoPersona = tipoPersona;
        this.raw = raw;
    }

    public String getRuc() {
        return ruc;
    }
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipoPersona() {
        return tipoPersona;
    }
    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
    public JsonNode getRaw() {
        return raw;
    }
    public void setRaw(JsonNode raw) {
        this.raw = raw;
    }
}
