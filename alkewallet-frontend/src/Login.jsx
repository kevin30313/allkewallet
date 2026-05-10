import { useState } from 'react';
import api from './api'; // Tu configuración de Axios
import './App.css';

const Login = () => {
    // Cambiamos 'username' a 'email' porque tu Backend busca por email en la DB
    const [credentials, setCredentials] = useState({ email: '', password: '' });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(''); // Limpiamos errores visuales previos
        
        // 1. LIMPIEZA PREVIA: Borramos cualquier rastro de sesiones anteriores
        // Esto evita el error 403 (Forbidden) que vimos en tus capturas
        localStorage.removeItem('token'); 
        
        try {
            // Enviamos los datos a tu API en Render (https://allkewallet-api.onrender.com)
            const response = await api.post('/auth/login', credentials);
            
            // Si el backend responde bien, recibimos el Token JWT
            const token = response.data; 
            
            // 2. GUARDADO: Almacenamos el nuevo token en el navegador
            localStorage.setItem('token', token);
            
            alert('¡Conexión exitosa! Bienvenido a AlkeWallet.');
            console.log("Token guardado con éxito:", token);
            
            // 3. REDIRECCIÓN: Una vez que el usuario da "OK", lo mandamos al home
            window.location.href = '/dashboard'; // O usa navigate('/dashboard') si tienes react-router
            
        } catch (err) {
            console.error("Error en el proceso de login:", err);
            
            // Manejo de errores basado en la respuesta del servidor
            if (err.response?.status === 403) {
                setError('Acceso denegado (403). Intenta borrar la caché del navegador.');
            } else if (err.response?.status === 401) {
                setError('Credenciales incorrectas. Revisa tu correo y contraseña.');
            } else {
                setError('Error: Usuario no encontrado o el servidor de Render está despertando.');
            }
        }
    };

    return (
        <div className="login-container">
            <h2 style={{ letterSpacing: '2px' }}>ALKEWALLET</h2>
            <p style={{ color: '#00f2ff', fontSize: '0.8rem', marginBottom: '20px', textTransform: 'uppercase' }}>
                Secure Cloud Banking
            </p>
            
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '15px' }}>
                    <input
                        name="email"  // Cambiado a email para coincidir con la lógica del Backend
                        type="email"
                        placeholder="CORREO ELECTRÓNICO"
                        className="neon-input"
                        onChange={handleChange}
                        required
                    />
                </div>
                <div style={{ marginBottom: '15px' }}>
                    <input
                        name="password"
                        type="password"
                        placeholder="CONTRASEÑA"
                        className="neon-input"
                        onChange={handleChange}
                        required
                    />
                </div>
                
                <button type="submit" className="neon-button">
                    INICIAR SESIÓN
                </button>
                <div style={{ marginTop: '20px', textAlign: 'center' }}>
            <p style={{ color: '#8892b0' }}>
                ¿No tienes cuenta? {' '}
                <a 
                    href="/register" 
                    onClick={(e) => {
                        e.preventDefault();
                        window.location.href = '/register';
                    }}
                    style={{ color: '#38bdf8', textDecoration: 'none', fontWeight: 'bold' }}
                >
                    Regístrate aquí
                </a>
            </p>
        </div>
            </form>

            {error && (
                <div style={{ 
                    marginTop: '20px', 
                    color: '#ff4d4d', 
                    textShadow: '0 0 8px #ff4d4d',
                    fontSize: '0.8rem',
                    fontWeight: 'bold'
                }}>
                    {error}
                </div>
            )}
        </div>
    );
};

export default Login;