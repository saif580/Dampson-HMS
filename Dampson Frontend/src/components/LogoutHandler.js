// src/components/LogoutHandler.js
import { useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const LogoutHandler = () => {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    console.log("Calling logout");
    logout();
    console.log("Navigating to landing page");
    navigate('/');
  }, [logout, navigate]);

  return null;
};

export default LogoutHandler;
