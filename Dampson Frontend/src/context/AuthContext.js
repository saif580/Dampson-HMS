// src/context/AuthContext.js
import React, { createContext, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [username, setUsername] = useState('');
  const [role, setRole] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const storedUsername = localStorage.getItem('username');
    const storedRole = localStorage.getItem('role');
    if (token && storedUsername && storedRole) {
      setIsAuthenticated(true);
      setUsername(storedUsername);
      setRole(storedRole);
      console.log("User authenticated on reload");
    } else {
      console.log("User not authenticated on reload");
    }
    setLoading(false);
  }, []);

  const login = (username, token, role) => {
    localStorage.setItem('username', username);
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    setIsAuthenticated(true);
    setUsername(username);
    setRole(role);
    console.log("User logged in");
  };

  const logout = () => {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    setIsAuthenticated(false);
    setUsername('');
    setRole('');
    toast.success("You have been logged out successfully.");
    console.log("User logged out");
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, username, role, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};
