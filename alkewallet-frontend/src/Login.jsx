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
        // Limpieza de seguridad antes de iniciar nueva sesión
        localStorage.removeItem('token'); 
        
        try {
            // Petición a tu API en Render
            const response = await api.post('/auth/login', credentials);
            const token = response.data; 
            
            // Guardamos el JWT
            localStorage.setItem('token', token);
            alert('¡Conexión exitosa! Bienvenido a AlkeWallet.');
            
            // Redirección manual al dashboard
            window.location.href = '/dashboard'; 
        } catch (err) {
            if (err.response?.status === 403) {
                setError('Acceso denegado (403). Intenta borrar la caché o revisar el rol.');
            } else if (err.response?.status === 401) {
                setError('Credenciales incorrectas. Verifica tu correo y contraseña.');
            } else {
                setError('Error: El servidor de Render está despertando. Reintenta en unos segundos.');
            }
        }
    };

    return (
        <div className="auth-page">
            {/* 1. HEADER CON LOGO NEÓN */}
            <header className="auth-header">
                <div className="logo-container">
                    <div className="logo-glow-wrapper">
                        <span className="logo-icon-neon">⚡</span>
                    </div>
                    <span className="logo-text-neon">ALKE<span>WALLET</span></span>
                </div>
                <nav className="auth-nav">
                    <a href="https://github.com/kevin30313" target="_blank" rel="noreferrer">GitHub</a>
                </nav>
            </header>

            <main className="auth-main">
                {/* 2. LADO IZQUIERDO: ARTE GEOMÉTRICO (TURTLE) */}
                <section className="auth-visual">
                    <div className="turtle-art-container">
                        <h2 className="visual-title">Finanzas con <br/><span>ADN Digital</span></h2>
                        <p style={{color: '#8892b0', marginTop: '10px', fontWeight: '300'}}>
                            Inspirado en patrones geométricos de Python
                        </p>
                    </div>
                </section>

                {/* 3. LADO DERECHO: FORMULARIO DE ACCESO */}
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

                            {error && (
                                <div className="error-message" style={{ color: '#ff4d4d', marginTop: '15px', fontSize: '0.85rem' }}>
                                    {error}
                                </div>
                            )}
                        </form>

                        <div className="auth-options" style={{ marginTop: '20px' }}>
                            <p style={{ color: '#8b949e', fontSize: '0.9rem' }}>
                                ¿No tienes cuenta? <a href="/register" style={{ color: 'var(--neon-blue)', textDecoration: 'none', fontWeight: '600' }}>Regístrate aquí</a>
                            </p>
                        </div>
                    </div>
                </section>
            </main>

            {/* 4. FOOTER CON STACK TECNOLÓGICO */}
            <footer className="auth-footer">
                <p>© 2026 AlkeWallet | Desarrollado por Kevin Rojas | Cloud Engineering Student</p>
                <div className="tech-badges" style={{ marginTop: '10px' }}>
                    <span>Java</span>
                    <span>AWS</span>
                    <span>Docker</span>
                    <span>React</span>
                </div>
            </footer>
        </div>
    );
};

export default Login;