// src/App.js
import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';
import './ToastifyCustom.css';
import Dashboard from './components/Dashboard';
import DoctorDashboard from './components/DoctorDashboard';
import LandingPage from './components/LandingPage';
import LogoutHandler from './components/LogoutHandler';
import RequireAuth from './components/RequireAuth';
import { AuthProvider } from './context/AuthContext';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/dashboard" element={
              <RequireAuth>
                <Dashboard />
              </RequireAuth>
            } />
            <Route path="/doctordashboard" element={
              <RequireAuth>
                <DoctorDashboard />
              </RequireAuth>
            } />
            <Route path="/logout" element={<LogoutHandler />} />
          </Routes>
          <ToastContainer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
