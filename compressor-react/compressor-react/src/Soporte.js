// Soporte.js
import React from 'react';
import './Soporte.css';
import TypingAnimation from './TypingAnimation';

const Soporte = () => {
  const paragraphs = [
    "Consulta aquí posibles preguntas que tengas sobre nuestra aplicación.",
    "Preguntas Frecuentes sobre ComPi (FAQ):"
  ];
  return (
    <div className='soporte'>
      <h2>Soporte</h2>
      <TypingAnimation paragraphs={paragraphs} />
    </div>
  );
};

export default Soporte;
