import { useState, useEffect } from 'react';
import { authApi, accountApi } from "./api";
import './Dashboard.css';
import TurtleCanvas from './TurtleCanvas';

const Dashboard = () => {
    const [userData, setUserData] = useState(null);
    const [account, setAccount] = useState(null);
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadAccountData = async () => {
        const [accRes, txRes] = await Promise.all([
            accountApi.get('/accounts/me'),
            accountApi.get('/accounts/me/transactions')
        ]);
        setAccount(accRes.data);
        setTransactions(txRes.data);
    };

    useEffect(() => {
        const loadDashboard = async () => {
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    window.location.href = '/login';
                    return;
                }
                const response = await authApi.get('/auth/me', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setUserData(response.data);
                await loadAccountData();
            } catch (err) {
                console.error("Error detallado:", err.response || err);
                setError("Error de autenticación o servidor");
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

    const handleIngresar = async () => {
        const monto = prompt("¿Cuánto quieres ingresar?");
        if (!monto || isNaN(monto) || Number(monto) <= 0) return;
        try {
            await accountApi.post('/accounts/deposit', { amount: Number(monto) });
            await loadAccountData();
        } catch (err) {
            alert(err.response?.data?.message || "No se pudo procesar el depósito");
        }
    };

    const handleEnviar = async () => {
        const destinationUserId = prompt("ID del usuario destino:");
        if (!destinationUserId) return;
        const monto = prompt("¿Cuánto quieres enviar?");
        if (!monto || isNaN(monto) || Number(monto) <= 0) return;
        try {
            await accountApi.post('/accounts/transfer', {
                destinationUserId: Number(destinationUserId),
                amount: Number(monto)
            });
            await loadAccountData();
        } catch (err) {
            alert(err.response?.data?.message || "No se pudo procesar la transferencia");
        }
    };

    if (loading) return <div className="loading-screen">Sincronizando...</div>;

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
                    <span className="user-name">Hola, {userData?.username || 'Usuario'}</span>
                    <button onClick={handleLogout} className="logout-mini-btn">Salir</button>
                </div>
            </header>

            <main className="dash-content">
                <section className="balance-section">
                    <div className="balance-card">
                        <div className="card-accent"></div>
                        <p className="label-dim">SALDO DISPONIBLE</p>
                        <h1 className="balance-amount">
                            ${account?.balance ? account.balance.toLocaleString('es-CL') : '0'}
                        </h1>
                        <div className="dash-quick-actions">
                            <button className="action-btn main" onClick={handleEnviar}>Enviar</button>
                            <button className="action-btn" onClick={handleIngresar}>Ingresar</button>
                        </div>
                    </div>
                </section>

                <section className="transactions-section">
                    <h2 className="section-title">Movimientos Recientes</h2>
                    <div className="transactions-container">
                        {transactions.length > 0 ? (
                            transactions.map((t, i) => (
                                <div key={i} className="t-row">
                                    <div className="t-info">
                                        <span className="t-desc">{t.type}</span>
                                        <span className="t-date">{new Date(t.createdAt).toLocaleDateString()}</span>
                                    </div>
                                    <span className={`t-value ${t.destinationUserId !== account?.userId ? 'negative' : 'positive'}`}>
                                        ${t.amount.toLocaleString('es-CL')}
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
