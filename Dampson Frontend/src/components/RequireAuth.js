// src/components/RequireAuth.js
import React, { useContext } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const RequireAuth = ({ children }) => {
  const { isAuthenticated, loading } = useContext(AuthContext);
  const location = useLocation();

  if (loading) {
    return <div>Loading...</div>; // You can replace this with a spinner or some other loading indicator
  }

  if (!isAuthenticated) {
    console.log("User not authenticated, redirecting to login");
    return <Navigate to="/" state={{ from: location }} />;
  }

  console.log("User authenticated, rendering protected route");
  return children;
};

export default RequireAuth;
