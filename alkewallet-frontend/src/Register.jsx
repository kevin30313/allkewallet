import React, { useState } from 'react';
import api from './api'; // Usamos tu configuración de Axios para Render
import './App.css';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            // Enviamos los datos a tu endpoint /api/auth/register en Render
            const response = await api.post('/auth/register', formData);

            console.log("Registro exitoso:", response.data);
            alert('¡Usuario creado con éxito! Ahora puedes iniciar sesión.');
            
            // REDIRECCIÓN: Igual que en tu Login, lo mandamos a la entrada
            window.location.href = '/login';
        } catch (err) {
            console.error("Error en el proceso de registro:", err);
            
            if (err.response && err.response.status === 400) {
                setError('El usuario o email ya existe, o los datos son inválidos.');
            } else {
                setError('Hubo un problema al conectar con el servidor. Revisa los logs de Render.');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <form className="auth-form" onSubmit={handleSubmit}>
                <h2 className="title">ALKEWALLET</h2>
                <p className="subtitle">CREAR NUEVA CUENTA</p>

                {error && <p className="error-message" style={{ color: 'red' }}>{error}</p>}

                <div className="input-group">
                    <input
                        type="text"
                        name="username"
                        placeholder="Nombre de usuario"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="input-group">
                    <input
                        type="email"
                        name="email"
                        placeholder="Correo electrónico"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="input-group">
                    <input
                        type="password"
                        name="password"
                        placeholder="Contraseña"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button type="submit" className="login-button" disabled={loading}>
                    {loading ? 'REGISTRANDO...' : 'REGISTRARSE'}
                </button>

                <div className="auth-links">
                    <a href="/login">¿Ya tienes cuenta? Inicia sesión aquí</a>
                </div>
            </form>
        </div>
    );
};

export default Register;