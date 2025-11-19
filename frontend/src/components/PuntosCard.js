import React from "react";

export default function PuntosCard({ puntos }) {
  if (puntos === null || puntos === undefined) return null;
  return (
    <div className="card">
      <h3>Puntos (ANT)</h3>
      <p style={{ fontSize: 28, margin: 8 }}>{puntos}</p>
    </div>
  );
}
