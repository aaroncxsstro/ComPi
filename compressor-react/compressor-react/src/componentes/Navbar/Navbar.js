// Navbar.js
import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css'; // Asegúrate de importar los estilos existentes
import '../Boton/Button.css'; // Importa los nuevos estilos

const Navbar = ({ appName, logo, randomColor }) => {
  return (
    <div className="navbar-container">
      <div className="header">
        <Link to="/">
          {/* <img src={logo} alt="Logo" className="logo" /> */}
          <h1 className="app-title">{appName}</h1>
        </Link>
      </div>
      <nav className="navbar">
        <ul className="navbar-nav">
          <li className="nav-item">
            <Link className="ui-btn" to="/" style={{ '--random-color': randomColor }}>
              <span>Inicio</span>
            </Link>
          </li>
          <li className="nav-item">
            <Link className="ui-btn" to="/descarga" style={{ '--random-color': randomColor }}>
              <span>Descarga</span>
            </Link>
          </li>
          <li className="nav-item">
            <Link className="ui-btn" to="/soporte" style={{ '--random-color': randomColor }}>
              <span>Soporte</span>
            </Link>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Navbar;
