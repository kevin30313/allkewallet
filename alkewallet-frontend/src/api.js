import axios from "axios";

// 1. Instancia exclusiva para el Servicio de Autenticación (auth-service)
export const authApi = axios.create({
  baseURL: "https://auth-service-1041045148793.us-central1.run.app/api",
  headers: {
    "Content-Type": "application/json",
  }
});

// 2. Instancia exclusiva para el Servicio de Cuentas y Saldos (account-service)
export const accountApi = axios.create({
  baseURL: "https://account-service-1041045148793.us-central1.run.app/api", // <-- Asegúrate de que esta sea la URL real de tu Cloud Run para accounts
  headers: {
    "Content-Type": "application/json",
  }
});

// Opcional: Interceptor automático para adjuntar el token JWT en accountApi
accountApi.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token"); // O como estés guardando el JWT en tu front
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);