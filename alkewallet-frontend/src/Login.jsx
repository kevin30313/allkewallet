import { useState } from 'react';
import api from './api'; 
import './App.css';

const Login = () => {
    const [credentials, setCredentials] = useState({ email: '', password: '' });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        localStorage.removeItem('token'); 
        
        try {
            const response = await api.post('/auth/login', credentials);
            const token = response.data; 
            localStorage.setItem('token', token);
            alert('¡Conexión exitosa! Bienvenido a AlkeWallet.');
            window.location.href = '/dashboard'; 
        } catch (err) {
            if (err.response?.status === 403) {
                setError('Acceso denegado (403). Intenta borrar la caché.');
            } else if (err.response?.status === 401) {
                setError('Credenciales incorrectas.');
            } else {
                setError('Error: El servidor de Render está despertando...');
            }
        }
    };

    return (
        <div className="auth-page">
            {/* 1. HEADER PRO */}
            <header className="auth-header">
                <div className="logo-container">
                    <span className="logo-icon">⚡</span>
                    <span className="logo-text">ALKEWALLET</span>
                </div>
                <nav className="auth-nav">
                    <a href="https://github.com/kevin30313" target="_blank" rel="noreferrer">GitHub</a>
                </nav>
            </header>

            <main className="auth-main">
                {/* 2. LADO IZQUIERDO: TU ARTE DE TURTLE */}
                <section className="auth-visual">
                    <div className="turtle-art-container">
                        {/* Aquí puedes poner una <img src={tuImagen} /> de tus diseños de Turtle */}
                        <h2 className="visual-title">Finanzas con <br/><span>ADN Digital</span></h2>
                        <p style={{color: '#8892b0', marginTop: '10px'}}>Inspirado en patrones geométricos de Python</p>
                    </div>
                </section>

                {/* 3. LADO DERECHO: TU FORMULARIO LOGIK */}
                <section className="auth-form-container">
                    <div className="login-container">
                        <h2 className="form-title">Iniciar Sesión</h2>
                        <p className="form-subtitle">Secure Cloud Banking System</p>
                        
                        <form onSubmit={handleSubmit} className="auth-form">
                            <input
                                name="email"
                                type="email"
                                placeholder="CORREO ELECTRÓNICO"
                                className="neon-input"
                                onChange={handleChange}
                                required
                            />
                            <input
                                name="password"
                                type="password"
                                placeholder="CONTRASEÑA"
                                className="neon-input"
                                onChange={handleChange}
                                required
                            />
                            
                            <button type="submit" className="neon-button">
                                INGRESAR
                            </button>

                            {error && <div className="error-message">{error}</div>}
                        </form>

                        <div className="auth-options">
                            <p>¿No tienes cuenta? <a href="/register">Regístrate aquí</a></p>
                        </div>
                    </div>
                </section>
            </main>

            {/* 4. FOOTER PRO */}
            <footer className="auth-footer">
                <p>© 2026 AlkeWallet | Desarrollado por Kevin Rojas | Cloud Engineering Student</p>
                <div className="tech-badges">
                    <span>Java</span><span>AWS</span><span>Docker</span><span>React</span>
                </div>
            </footer>
        </div>
    );
};

export default Login;