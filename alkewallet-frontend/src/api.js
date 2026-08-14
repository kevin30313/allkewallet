import axios from "axios";

export const authApi = axios.create({
  baseURL: "https://auth-service-1041045148793.us-central1.run.app/api",
  headers: {
    "Content-Type": "application/json",
  }
});

export const accountApi = axios.create({
  baseURL: "https://account-service-1041045148793.us-central1.run.app/api",
  headers: {
    "Content-Type": "application/json",
  }
});

accountApi.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);