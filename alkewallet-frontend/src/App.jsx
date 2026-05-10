import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './Login';
import './App.css';
import Register from './Register';
import Dashboard from './Dashboard';
function App() {
  return (
    <Router>
      <Routes>
        {/* Ruta para el Login */}
        <Route path="/login" element={<Login />} />

        {/* NUEVA RUTA: Registro */}
        <Route path="/register" element={<Register />} />

        {/* Ruta para el Dashboard */}
        <Route path="/dashboard" element={<Dashboard />} />

        {/* Redirección por defecto: si la ruta no existe, al login */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App; // 
