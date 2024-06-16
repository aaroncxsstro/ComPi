// Inicio.js
import React from 'react';
import './Inicio.css';
import TypingAnimation from './TypingAnimation';

const Inicio = () => {
  const paragraphs = [
    "Bienvenido a ComPi, tu fiel compañero para la gestión de archivos.",
    "Disfruta de velocidad y seguridad a la hora de comprimir y descomprimir ficheros.\n Di adiós a desorbitados tiempos de espera abriendo, descargando o enviando archivos. "
  ];

  return (
    <div className='inicio'>
      <h2>Inicio</h2>
      <TypingAnimation paragraphs={paragraphs} />
    </div>
  );
};

export default Inicio;
