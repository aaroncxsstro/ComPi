// Content.js
import React from 'react';

const Content = () => {
  return (
    <div className="container mt-4">
      <section id="inicio">
        <h2>Inicio</h2>
        <p>Información sobre la aplicación y su propósito.</p>
      </section>
      <section id="descarga" className="mt-5">
        <h2>Descarga</h2>
        <p>Enlaces de descarga de la aplicación.</p>
      </section>
      <section id="soporte" className="mt-5">
        <h2>Soporte</h2>
        <p>Información de contacto para soporte técnico.</p>
      </section>
      {/* Agrega más secciones según necesites */}
    </div>
  );
};

export default Content;
