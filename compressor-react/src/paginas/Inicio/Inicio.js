// Inicio.js
import React from 'react';
import './Inicio.css';
import TypingAnimation from '../../componentes/TypingAnimation';

const Inicio = () => {
  const paragraphs = [
    "Bienvenido a ComPi, tu fiel compañero para la gestión de archivos.",
    "Disfruta de velocidad y seguridad a la hora de comprimir y descomprimir ficheros.\n Di adiós a desorbitados tiempos de espera abriendo, descargando o enviando archivos. ",
    "Con ComPi, dispondrás de una interfaz cómoda y sencilla de usar. Además podrás acceder a las funciones de nuestra aplicación con un simple click en el archivo que desees manipular.",
    "Haz click en el botón de descarga ubicado en el menú superior para obtener ComPi ahora."
  ];

  return (
    <div className='inicio'>
      <h2>Inicio</h2>
      <TypingAnimation paragraphs={paragraphs} />
    </div>
  );
};

export default Inicio;
