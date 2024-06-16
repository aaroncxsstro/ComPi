import React, { useState } from 'react';
import './Descarga.css';
import TypingAnimation from '../../componentes/TypingAnimation';


const Descarga = ({randomColor}) => {
  const paragraphs = [
    "Descarga ComPi ahora.",
    "Haz click en el botón de descarga y disfruta de las ventajas que ofrece nuestra aplicación.",
    "Ejecuta el instalador y sigue unos sencillos pasos para obtener ComPi gratis hoy."
  ];

  const [buttonVisible, setButtonVisible] = useState(false);

  const handleWritingCompleted = () => {
    setButtonVisible(true);
  };

  return (
    <div className='descarga'>
      <h2>Descarga</h2>
      <div className='contenido-descarga'>
      <TypingAnimation paragraphs={paragraphs} onWritingCompleted={handleWritingCompleted} /> 
      </div>

      <a href='https://www.mediafire.com/file/736w18rbnpvfqaj/compisetup.exe/file' download>
      <div className={`button-container ${buttonVisible ? 'visible' : ''}`}>
      
        <button className="button" type="button" style={{ '--random-color': randomColor }}>
            <span className="button__text">Descarga ComPi</span>
            <span className="button__icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 35 35" id="bdd05811-e15d-428c-bb53-8661459f9307" data-name="Layer 2" className="svg">
                <path d="M17.5,22.131a1.249,1.249,0,0,1-1.25-1.25V2.187a1.25,1.25,0,0,1,2.5,0V20.881A1.25,1.25,0,0,1,17.5,22.131Z"></path>
                <path d="M17.5,22.693a3.189,3.189,0,0,1-2.262-.936L8.487,15.006a1.249,1.249,0,0,1,1.767-1.767l6.751,6.751a.7.7,0,0,0,.99,0l6.751-6.751a1.25,1.25,0,0,1,1.768,1.767l-6.752,6.751A3.191,3.191,0,0,1,17.5,22.693Z"></path>
                <path d="M31.436,34.063H3.564A3.318,3.318,0,0,1,.25,30.749V22.011a1.25,1.25,0,0,1,2.5,0v8.738a.815.815,0,0,0,.814.814H31.436a.815.815,0,0,0,.814-.814V22.011a1.25,1.25,0,1,1,2.5,0v8.738A3.318,3.318,0,0,1,31.436,34.063Z"></path>
              </svg>
            </span>
          </button>
      
        
      </div>
      </a>
    </div>
  );
};

export default Descarga;
