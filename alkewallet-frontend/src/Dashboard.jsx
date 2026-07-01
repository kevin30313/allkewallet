import { useState, useEffect } from 'react';
import { authApi as api } from "./api";
import './Dashboard.css';
import TurtleCanvas from './TurtleCanvas';

const Dashboard = () => {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null); // Nuevo estado para ver el error

    useEffect(() => {
        const loadDashboard = async () => {
            try {
                const token = localStorage.getItem('token');
                console.log("Token actual:", token); // DEBUG

                if (!token) {
                    console.log("No hay token, redirigiendo...");
                    window.location.href = '/login';
                    return;
                }

                const response = await api.get('/auth/user', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                
                console.log("Respuesta Backend:", response.data); // DEBUG
                setUserData(response.data);
            } catch (err) {
                console.error("Error detallado:", err.response || err);
                setError("Error de autenticación o servidor");
                // Comenta la línea de abajo temporalmente para ver el error en pantalla
                // window.location.href = '/login'; 
            } finally {
                setLoading(false);
            }
        };
        loadDashboard();
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token');
        window.location.href = '/login';
    };

    if (loading) return <div className="loading-screen">Sincronizando...</div>;
    
    // Si hay un error, lo mostramos para saber qué falló
    if (error) return (
        <div style={{color: 'white', textAlign: 'center', marginTop: '50px'}}>
            <h2>{error}</h2>
            <button onClick={handleLogout}>Volver al Login</button>
        </div>
    );

    return (
        <div className="dashboard-page">
            <div className="dashboard-bg-art"><TurtleCanvas /></div>
            <header className="dash-header">
                <div className="dash-logo">
                    <span className="logo-icon">⚡</span>
                    <span className="logo-text">ALKE<span>WALLET</span></span>
                </div>
                <div className="user-profile-header">
                    <span className="user-name">Hola, {userData?.username || userData?.nombre || 'Usuario'}</span>
                    <button onClick={handleLogout} className="logout-mini-btn">Salir</button>
                </div>
            </header>

            <main className="dash-content">
                <section className="balance-section">
                    <div className="balance-card">
                        <div className="card-accent"></div>
                        <p className="label-dim">SALDO DISPONIBLE</p>
                        <h1 className="balance-amount">
                            ${userData?.saldo ? userData.saldo.toLocaleString('es-CL') : '0'}
                        </h1>
                        <div className="dash-quick-actions">
                            <button className="action-btn main">Enviar</button>
                            <button className="action-btn">Ingresar</button>
                        </div>
                    </div>
                </section>

                <section className="transactions-section">
                    <h2 className="section-title">Movimientos Recientes</h2>
                    <div className="transactions-container">
                        {userData?.transacciones?.length > 0 ? (
                            userData.transacciones.map((t, i) => (
                                <div key={i} className="t-row">
                                    <div className="t-info">
                                        <span className="t-desc">{t.descripcion}</span>
                                        <span className="t-date">{new Date(t.fecha).toLocaleDateString()}</span>
                                    </div>
                                    <span className={`t-value ${t.monto < 0 ? 'negative' : 'positive'}`}>
                                        {t.monto < 0 ? '' : '+'}${t.monto.toLocaleString('es-CL')}
                                    </span>
                                </div>
                            ))
                        ) : (
                            <p className="no-data">No hay movimientos aún.</p>
                        )}
                    </div>
                </section>
            </main>
        </div>
    );
};

export default Dashboard;