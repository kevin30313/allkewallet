import React from 'react';

const Dashboard = () => {
    return (
        <div style={{ padding: '20px', color: 'white' }}>
            <h1>Bienvenido a tu AlkeWallet</h1>
            <p>Próximamente: Tu saldo y transacciones.</p>
            <button onClick={() => window.location.href = '/login'}>Cerrar Sesión</button>
        </div>
    );
};

export default Dashboard;