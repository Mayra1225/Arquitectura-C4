import React from "react";

export default function VehiculoCard({ vehiculo }) {
  if (!vehiculo) return null;
  return (
    <div className="card">
      <h3>Vehículo</h3>
      <p><strong>Placa:</strong> {vehiculo.placa || "N/A"}</p>
      <p><strong>Marca:</strong> {vehiculo.raw.descripcionMarca || "N/A"}</p>
      <p><strong>Modelo:</strong> {vehiculo.raw.descripcionModelo || "N/A"}</p>
      <details>
        <summary>Ver JSON completo</summary>
        <pre className="mono">{JSON.stringify(vehiculo.raw || vehiculo, null, 2)}</pre>
      </details>
    </div>
  );
}
