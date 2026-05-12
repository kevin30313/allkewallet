import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from './api'; 
import './App.css';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    
    const navigate = useNavigate(); // Hook para redirección suave

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
            // Petición al backend en Render
            const response = await api.post('/auth/register', formData);

            console.log("Registro exitoso:", response.data);
            alert('¡Usuario creado con éxito! Ahora puedes iniciar sesión.');
            
            // Redirección al Login usando el router
            navigate('/login');
        } catch (err) {
            console.error("Error en el proceso de registro:", err);
            
            if (err.response && err.response.status === 400) {
                setError('El usuario o email ya existe, o los datos son inválidos.');
            } else {
                setError('Hubo un problema al conectar con el servidor.');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-container"> {/* Usando tu clase de App.css */}
            <form onSubmit={handleSubmit}>
                <h2 className="title">ALKEWALLET</h2>
                <p className="subtitle" style={{ color: 'var(--text-dim)', marginBottom: '1.5rem' }}>
                    CREAR NUEVA CUENTA
                </p>

                {error && (
                    <p className="error-message" style={{ color: '#ff4d4d', fontSize: '0.9rem', marginBottom: '1rem' }}>
                        {error}
                    </p>
                )}

                <div className="input-group">
                    <input
                        type="text"
                        name="username"
                        className="neon-input"
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
                        className="neon-input"
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
                        className="neon-input"
                        placeholder="Contraseña"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button type="submit" className="neon-button" disabled={loading}>
                    {loading ? 'REGISTRANDO...' : 'REGISTRARSE'}
                </button>

                <div style={{ marginTop: '1.5rem' }}>
                    <Link to="/login" style={{ color: 'var(--neon-blue)', textDecoration: 'none', fontSize: '0.9rem' }}>
                        ¿Ya tienes cuenta? Inicia sesión aquí
                    </Link>
                </div>
            </form>
        </div>
    );
};

export default Register;