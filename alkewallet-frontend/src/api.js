import axios from 'axios';

const api = axios.create({
    // Tu URL de Render (asegúrate de que no tenga un "/" al final de "api")
    baseURL: 'https://allkewallet-api.onrender.com/api',
});

// Este "Interceptor" es como un guardia de seguridad: 
// Antes de cada petición, revisa si tienes un token en el navegador
// y lo pega automáticamente en el header de Authorization.
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;