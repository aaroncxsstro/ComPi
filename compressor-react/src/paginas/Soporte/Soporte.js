import React, { useState } from 'react';
import './Soporte.css';
import TypingAnimation from '../../componentes/TypingAnimation';

const Soporte = () => {
  const paragraphs = [
    "Consulta aquí posibles preguntas que tengas sobre nuestra aplicación.",
    "Preguntas Frecuentes sobre ComPi (FAQ):"
  ];

  const faqData = [
    { question: "1. ¿Qué es ComPi?", answer: "ComPi es una aplicación de escritorio diseñada para facilitar la manipulación, compresión y descompresión de archivos y directorios. Haciendo uso de nuestra propia extensión de archivo y funciones exclusivas de rápido funcionamiento, ComPi logra llevar a cabo útiles tareas en muy poco tiempo y de manera cómoda." },
    { question: "2. ¿Cómo puedo descargar ComPi?", answer: "Puedes descargar ComPi desde nuestra página de descarga, a la que puedes acceder haciendo click en el botón de descarga del menú superior de la página. En esta página verás un botón con el que podrás descargar el instalador de la aplicación, con el que tendrás un rápido acceso a la misma." },
    {question: "3. ¿En qué lenguaje está creado ComPi?", answer: "ComPi está hecho con Java. Su interfaz está hecha con la extensión de JavaFX. Esta página web donde alojamos toda nuestra información y la descarga de la aplicación, está hecha con React."},
    {question: "4. ¿Cuáles son las funciones básicas de ComPi?", answer: "Puedes acceder a las funciones de ComPi de dos maneras principales. La primera es abriendo el ejecutable de la aplicación situado en la carpeta de descarga que obtendrás al instalar ComPi. Aquí dentro podrás acceder a todos los archivos de tu equipo para su fácil manipulación. Otra manera es haciendo click derecho en algún archivo o directorio que desees gestionar con ComPi."},
    {question: "5. ¿Cómo identifico un archivo de ComPi?" , answer: "Los archivos comprimidos con ComPi se convertirán en ficheros con una extensión '.copi' . Estos archivos tendrán un icono diferente de los demás y tendrá un tamaño menor gracias a su compresión. Puedes descomprimir archivos de este tipo en cualquier momento siempre y cuando tengas ComPi instalado en tu equipo."}
  ];

  const [currentIndex, setCurrentIndex] = useState(0);
  const [isVisible, setIsVisible] = useState(false);

  const handlePrev = () => {
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - 1);
    }
  };

  const handleNext = () => {
    if (currentIndex < faqData.length - 1) {
      setCurrentIndex(currentIndex + 1);
    }
  };

  const handleWritingCompleted = () => {
    setIsVisible(true);
  };

  return (
    <div className='soporte'>
      <h2>Soporte</h2>
      <TypingAnimation paragraphs={paragraphs} onWritingCompleted={handleWritingCompleted} />
      <div className={`faq-container ${isVisible ? 'visible' : ''}`}>
        <div className='faq-content'>
          {faqData.map((faq, index) => (
            <div key={index} className={`faq-item ${index === currentIndex ? 'active' : ''}`}>
              <div className='faq-question'>{faq.question}</div>
              <div className='faq-answer'>{faq.answer}</div>
            </div>
          ))}
        </div>
        <div className='faq-navigation'>
          <button onClick={handlePrev} disabled={currentIndex === 0}>Anterior</button>
          <button onClick={handleNext} disabled={currentIndex === faqData.length - 1}>Siguiente</button>
        </div>
      </div>
    </div>
  );
};

export default Soporte;
