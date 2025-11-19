import React from "react";

export default function ContribuyenteCard({ contribuyente }) {
  if (!contribuyente) return null;
  return (
    <div className="card">
      <h3>Contribuyente</h3>
      <p><strong>RUC:</strong> {contribuyente.ruc || "N/A"}</p>
      <p><strong>Nombre:</strong> {contribuyente.raw[0].razonSocial || "N/A"}</p>
      <p><strong>Tipo:</strong> {contribuyente.raw[0].tipoContribuyente || "N/A"}</p>
      <details>
        <summary>Ver JSON completo</summary>
        <pre className="mono">{JSON.stringify(contribuyente.raw || contribuyente, null, 2)}</pre>
      </details>
    </div>
  );
}
