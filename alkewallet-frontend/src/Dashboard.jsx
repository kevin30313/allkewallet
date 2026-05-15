import { useState, useEffect } from 'react';
import api from './api'; 
import './Dashboard.css';
import TurtleCanvas from './TurtleCanvas';

const Dashboard = () => {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadDashboard = async () => {
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    window.location.href = '/login';
                    return;
                }

                // Llamada al endpoint real que acabas de subir al backend
                const response = await api.get('/auth/user', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                
                setUserData(response.data);
            } catch (err) {
                console.error("Error cargando dashboard", err);
                // Si hay error de autenticación, limpiamos y redirigimos
                localStorage.removeItem('token');
                window.location.href = '/login';
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

    if (loading) return (
        <div className="loading-screen">
            <div className="loader"></div>
            <p>Sincronizando con la red AlkeWallet...</p>
        </div>
    );

    return (
        <div className="dashboard-page">
            {/* Fondo artístico con la tortuga */}
            <div className="dashboard-bg-art">
                <TurtleCanvas />
            </div>

            <header className="dash-header">
                <div className="dash-logo">
                    <span className="logo-icon">⚡</span>
                    <span className="logo-text">ALKE<span>WALLET</span></span>
                </div>
                <div className="user-profile-header">
                    {/* Usamos el username o nombre que viene de la DB */}
                    <span className="user-name">Hola, {userData?.username || userData?.nombre || 'Usuario'}</span>
                    <button onClick={handleLogout} className="logout-mini-btn">Cerrar Sesión</button>
                </div>
            </header>

            <main className="dash-content">
                {/* Sección de Saldo Real */}
                <section className="balance-section">
                    <div className="balance-card">
                        <div className="card-accent"></div>
                        <p className="label-dim">SALDO DISPONIBLE</p>
                        <h1 className="balance-amount">
                            {/* Formato de moneda chilena: $1.234.567 */}
                            ${userData?.saldo ? userData.saldo.toLocaleString('es-CL') : '0'}
                        </h1>
                        <div className="dash-quick-actions">
                            <button className="action-btn main">Enviar</button>
                            <button className="action-btn">Ingresar</button>
                        </div>
                    </div>
                </section>

                {/* Sección de Transacciones */}
                <section className="transactions-section">
                    <h2 className="section-title">Movimientos Recientes</h2>
                    <div className="transactions-container">
                        {userData?.transacciones && userData.transacciones.length > 0 ? (
                            userData.transacciones.map((t, index) => (
                                <div key={index} className="t-row">
                                    <div className="t-info">
                                        <span className="t-desc">{t.descripcion}</span>
                                        <span className="t-date">{new Date(t.fecha).toLocaleDateString()}</span>
                                    </div>
                                    <span className={`t-value ${t.tipo === 'EGRESO' ? 'negative' : 'positive'}`}>
                                        {t.tipo === 'EGRESO' ? '-' : '+'}${t.monto.toLocaleString('es-CL')}
                                    </span>
                                </div>
                            ))
                        ) : (
                            <div className="no-data">
                                <p>No hay movimientos registrados aún.</p>
                                <button className="start-btn">Realiza tu primera operación</button>
                            </div>
                        )}
                    </div>
                </section>
            </main>
        </div>
    );
};

export default Dashboard;