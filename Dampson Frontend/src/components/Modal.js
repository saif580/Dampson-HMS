// src/components/Modal.js

import React from 'react';
import './Modal.css';

const Modal = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;

  return (
    <>
      <div className="modal">
        <button className="btn--close-modal" onClick={onClose}>&times;</button>
        {children}
      </div>
      <div className="overlay" onClick={onClose}></div>
    </>
  );
};

export default Modal;
