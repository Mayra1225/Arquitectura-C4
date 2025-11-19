import React, { useState } from "react";
import { fetchContribuyente, fetchVehiculo, fetchPuntos } from "./api";
import ContribuyenteCard from "./components/ContribuyenteCard";
import VehiculoCard from "./components/VehiculoCard";
import PuntosCard from "./components/PuntosCard";

function App() {
  const [email, setEmail] = useState("");
  const [ruc, setRuc] = useState("");
  const [placa, setPlaca] = useState("");
  const [cedula, setCedula] = useState("");

  const [loading, setLoading] = useState(false);
  const [contribuyente, setContribuyente] = useState(null);
  const [vehiculo, setVehiculo] = useState(null);
  const [puntos, setPuntos] = useState(undefined);
  const [error, setError] = useState(null);

  const buscarContribuyente = async () => {
    setError(null);
    setLoading(true);
    try {
      const res = await fetchContribuyente(ruc);
      if (!res.ok) {
        setContribuyente(null);
        setError(res.text || `Error ${res.status}`);
      } else {
        setContribuyente(res.data || null);
      }
    } catch (e) {
      setError(e.message);
      setContribuyente(null);
    } finally {
      setLoading(false);
    }
  };

  const buscarVehiculo = async () => {
    setError(null);
    setLoading(true);
    try {
      const res = await fetchVehiculo(placa);
      if (!res.ok) {
        setVehiculo(null);
        setError(res.text || `Error ${res.status}`);
      } else {
        setVehiculo(res.data || null);
      }
    } catch (e) {
      setError(e.message);
      setVehiculo(null);
    } finally {
      setLoading(false);
    }
  };

  const buscarPuntos = async () => {
    setError(null);
    setLoading(true);
    try {
      const res = await fetchPuntos(cedula);
      if (!res.ok) {
        // ANT puede devolver 503 con texto
        setPuntos(null);
        setError(res.text || `Error ${res.status}`);
      } else {
        setPuntos(res.data ?? null);
      }
    } catch (e) {
      setError(e.message);
      setPuntos(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <h1>Arquitectura C4 — Consulta SRI / Vehículo / ANT</h1>

      <div style={{ marginBottom: 12 }}>
        <label style={{ fontSize: 13, color: "#9fb7ff" }}>Email (opcional)</label>
        <input
          type="email"
          placeholder="tu@correo.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          style={{ width: "100%", padding: 8, borderRadius: 6, marginTop: 6 }}
        />
      </div>

      <div className="form-row">
        <label>RUC</label>
        <input value={ruc} onChange={(e) => setRuc(e.target.value)} placeholder="Ingrese RUC" />
        <button className="btn" onClick={buscarContribuyente} disabled={loading || !ruc}>
          Buscar Contribuyente
        </button>
      </div>

      <div className="form-row">
        <label>Placa</label>
        <input value={placa} onChange={(e) => setPlaca(e.target.value)} placeholder="Ingrese placa" />
        <button className="btn" onClick={buscarVehiculo} disabled={loading || !placa}>
          Buscar Vehículo
        </button>
      </div>

      <div className="form-row">
        <label>Cédula</label>
        <input value={cedula} onChange={(e) => setCedula(e.target.value)} placeholder="Ingrese cédula" />
        <button className="btn" onClick={buscarPuntos} disabled={loading || !cedula}>
          Consultar Puntos
        </button>
      </div>

      {loading && <p style={{ color: "#9fb7ff" }}>Cargando...</p>}
      {error && <p style={{ color: "#ffb4b4" }}>Error: {error}</p>}

      <div className="row">
        <ContribuyenteCard contribuyente={contribuyente} />
        <VehiculoCard vehiculo={vehiculo} />
      </div>

      <div style={{ marginTop: 16 }}>
        <PuntosCard puntos={puntos} />
      </div>
    </div>
  );
}

export default App;
