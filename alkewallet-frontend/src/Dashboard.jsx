import { useState, useEffect } from 'react';
import './Dashboard.css';
import TurtleCanvas from './TurtleCanvas'; // Reutilizamos el arte pero en modo sutil

const Dashboard = () => {
    const [user, setUser] = useState({ name: 'Kevin Rojas', balance: 5420900 });
    const [transactions, setTransactions] = useState([
        { id: 1, desc: 'Transferencia Recibida', date: '12 May', amount: 150000, type: 'income' },
        { id: 2, desc: 'Pago AWS Cloud', date: '10 May', amount: -45000, type: 'expense' },
        { id: 3, desc: 'Suscripción Docker Hub', date: '08 May', amount: -12000, type: 'expense' },
        { id: 4, desc: 'Depósito por Cajero', date: '05 May', amount: 80000, type: 'income' },
    ]);

    const handleLogout = () => {
        localStorage.removeItem('token');
        window.location.href = '/login';
    };

    return (
        <div className="dashboard-page">
            {/* Fondo de red técnica sutil (modo marca de agua) */}
            <div className="dashboard-bg-art">
                <TurtleCanvas />
            </div>

            {/* BARRA SUPERIOR */}
            <header className="dash-header">
                <div className="dash-logo">
                    <span className="logo-icon">⚡</span>
                    <span className="logo-text">ALKE<span>WALLET</span></span>
                </div>
                <div className="user-profile">
                    <span className="user-name">{user.name}</span>
                    <button onClick={handleLogout} className="logout-btn">SALIR</button>
                </div>
            </header>

            <main className="dash-content">
                {/* SECCIÓN DE SALDO (LA CARD PRINCIPAL) */}
                <section className="balance-section">
                    <div className="balance-card">
                        <div className="accent-bar"></div>
                        <p className="label-dim">SALDO TOTAL DISPONIBLE</p>
                        <h1 className="balance-amount">
                            ${user.balance.toLocaleString('es-CL')}
                        </h1>
                        <div className="dash-actions">
                            <button className="action-btn primary">ENVIAR DINERO</button>
                            <button className="action-btn secondary">DEPOSITAR</button>
                        </div>
                    </div>
                </section>

                {/* LISTA DE TRANSACCIONES (LETRAS MARCADAS) */}
                <section className="transactions-section">
                    <h2 className="section-title">Últimos Movimientos</h2>
                    <div className="transactions-list">
                        {transactions.map(t => (
                            <div key={t.id} className="transaction-item">
                                <div className="t-info">
                                    <span className="t-desc">{t.desc}</span>
                                    <span className="t-date">{t.date}</span>
                                </div>
                                <span className={`t-amount ${t.type}`}>
                                    {t.amount > 0 ? '+' : ''}{t.amount.toLocaleString('es-CL')}
                                </span>
                            </div>
                        ))}
                    </div>
                </section>
            </main>
        </div>
    );
};

export default Dashboard;