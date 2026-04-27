import { useState } from 'react';
import api from './api'; // Importamos la configuración de Axios que creamos
import './App.css';

const Login = () => {
    // Estado para capturar lo que el usuario escribe
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(''); // Limpiamos errores previos
        
        try {
            // Enviamos los datos a tu API en Render
            const response = await api.post('/auth/login', credentials);
            
            // Si el backend responde bien, nos da el Token (un string largo)
            const token = response.data; 
            
            // Guardamos el token en el navegador para usarlo en otras pantallas
            localStorage.setItem('token', token);
            
            alert('¡Conexión exitosa! Bienvenido a AlkeWallet.');
            console.log("Token guardado:", token);
            
            // TODO: Redireccionar al Dashboard cuando lo tengamos listo
        } catch (err) {
            console.error(err);
            setError('Error: Usuario no encontrado o servidor caído.');
        }
    };

    return (
        <div className="login-container">
            <h2>AlkeWallet</h2>
            <p style={{ color: '#00f2ff', fontSize: '0.8rem', marginBottom: '20px' }}>
                Secure Cloud Banking
            </p>
            
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '15px' }}>
                    <input
                        name="username"
                        type="text"
                        placeholder="NOMBRE DE USUARIO"
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
            </form>

            {error && (
                <div style={{ 
                    marginTop: '20px', 
                    color: '#ff4d4d', 
                    textShadow: '0 0 5px #ff4d4d',
                    fontSize: '0.9rem' 
                }}>
                    {error}
                </div>
            )}
        </div>
    );
};

export default Login;