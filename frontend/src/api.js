const API_BASE = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

async function getJSON(url) {
  const res = await fetch(url, { headers: { "Accept": "application/json" }});
  const text = await res.text();
  try {
    return { ok: res.ok, data: text ? JSON.parse(text) : null, status: res.status, text };
  } catch (e) {
    // no JSON (p. ej. texto o error)
    return { ok: res.ok, data: null, status: res.status, text };
  }
}

export async function fetchContribuyente(ruc) {
  return getJSON(`${API_BASE}/contribuyente/${encodeURIComponent(ruc)}`);
}

export async function fetchVehiculo(placa) {
  return getJSON(`${API_BASE}/vehiculo/${encodeURIComponent(placa)}`);
}

export async function fetchPuntos(cedula) {
  return getJSON(`${API_BASE}/ant/puntos/${encodeURIComponent(cedula)}`);
}
