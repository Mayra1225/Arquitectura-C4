package org.example.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class VehiculoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String placa;
    private String marca;
    private String modelo;
    private JsonNode raw;

    public VehiculoDTO() {}

    public VehiculoDTO(String placa, String marca, String modelo, JsonNode raw) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.raw = raw;
    }

    public String getPlaca() {
        return placa;
    }
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public JsonNode getRaw() {
        return raw;
    }
    public void setRaw(JsonNode raw) {
        this.raw = raw;
    }
}
