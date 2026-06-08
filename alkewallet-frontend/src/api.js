import axios from 'axios';

const api = axios.create({
    // Corregido el texto duplicado de la URL
    baseURL: 'https://account-service-1041045148793.us-central1.run.app',
});

// Este "Interceptor" es como un guardia de seguridad: 
// Antes de cada petición, revisa si tienes un token en el navegador
// y lo pega automáticamente en el header de Authorization.
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        // Corregido: Sin la comilla simple intermedia, solo espacio limpio
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;