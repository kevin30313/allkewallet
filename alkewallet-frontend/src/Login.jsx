import { useState } from 'react';
import { authApi as api } from "./api"; // Importamos 'authApi' y le ponemos el alias 'api'
import './App.css';
import TurtleCanvas from './TurtleCanvas';

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
            
            // CORRECCIÓN AQUÍ: Extraemos el string dentro de la propiedad 'token'
          const tokenStr = response.data; 
    
        if (tokenStr) {
        localStorage.setItem('token', tokenStr);
        console.log("Token guardado exitosamente:", tokenStr);
        window.location.href = '/dashboard'; 
    }   
        else {
        console.error("No se recibió un token válido:", response.data);
        setError('Error en la estructura de datos del servidor.');
    }
}
        } catch (err) {
            console.error("Error en login:", err.response || err);
            if (err.response?.status === 403) {
                setError('Acceso denegado (403). Intenta borrar la caché o revisar el rol.');
            } else if (err.response?.status === 401) {
                setError('Credenciales incorrectas. Verifica tu correo y contraseña.');
            } else {
                setError('Error: El servidor  está despertando. Reintenta en unos segundos.');
            }
        }
    };

    return (
        <div className="auth-page">
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
                <section className="auth-visual" style={{ position: 'relative', overflow: 'hidden', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                    <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', zIndex: 1 }}>
                        <TurtleCanvas />
                    </div>

                    <div style={{ 
                        position: 'relative', 
                        zIndex: 2, 
                        textAlign: 'center', 
                        padding: '2rem',
                        background: 'radial-gradient(circle, rgba(11,13,14,0.8) 0%, rgba(11,13,14,0) 80%)' 
                    }}>
                        <h2 className="visual-title">Finanzas con <br/><span>ADN Digital</span></h2>
                        <p style={{ color: '#8892b0', marginTop: '15px', fontWeight: '300', letterSpacing: '1px', textTransform: 'uppercase', fontSize: '0.8rem' }}>
                            Infraestructura Cloud Segura
                        </p>
                    </div>
                </section>

                <section className="auth-form-container">
                    <div className="login-container">
                        <h2 className="form-title">Iniciar Sesión</h2>
                        <p className="form-subtitle">SECURE CLOUD BANKING SYSTEM</p>
                        
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
                                <div className="error-message" style={{ color: '#ff4d4d', marginTop: '15px', fontSize: '0.85rem', textAlign: 'center' }}>
                                    {error}
                                </div>
                            )}
                        </form>

                        <div className="auth-options" style={{ marginTop: '20px', textAlign: 'center' }}>
                            <p style={{ color: '#8b949e', fontSize: '0.9rem' }}>
                                ¿No tienes cuenta? <a href="/register" style={{ color: 'var(--neon-blue)', textDecoration: 'none', fontWeight: '600' }}>Regístrate aquí</a>
                            </p>
                        </div>
                    </div>
                </section>
            </main>

            <footer className="auth-footer">
                <p>© 2026 AlkeWallet | Desarrollado por Kevin Rojas | Cloud Engineering Student</p>
                <div className="tech-badges" style={{ marginTop: '10px' }}>
                    <span>Java</span><span>AWS</span><span>Docker</span><span>React</span>
                </div>
            </footer>
        </div>
    );
};

export default Login;