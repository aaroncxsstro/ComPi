import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './componentes/Navbar/Navbar';
import Inicio from './paginas/Inicio/Inicio';
import Descarga from './paginas/Descarga/Descarga';
import Soporte from './paginas/Soporte/Soporte';
import './styles.css';
import './componentes/Boton/Button.css';
import Logo from './assets/img/compi_icono_png.png';

const colors = ['#ff3838', '#ffa564', '#ffe838', '#38ff42', '#386aff', '#9b38ff', '#fc38ff'];

function getNextColorIndex(currentIndex) {
  return (currentIndex + 1) % colors.length;
}

function App() {
  const [hoverText, setHoverText] = useState('¡Hazme click!');
  const [currentIndex, setCurrentIndex] = useState(() => {
    const savedIndex = localStorage.getItem('colorIndex');
    return savedIndex !== null ? Number(savedIndex) : 0;
  });

  const [randomColor, setRandomColor] = useState(colors[currentIndex]);

  useEffect(() => {
    localStorage.setItem('colorIndex', currentIndex);
    setRandomColor(colors[currentIndex]);
  }, [currentIndex]);

  const handleChangeColor = () => {
    setCurrentIndex((prevIndex) => getNextColorIndex(prevIndex));
  };

  return (
    <Router>
      <div className="main-container" style={{ '--random-color': randomColor }}>
        <div className='logo-container transition-color'>
          <img src={Logo} onClick={handleChangeColor} style={{ cursor: 'pointer' }} path="/" alt="Logo" />
        </div>
        <div className='circles-container' style={{ '--random-color': randomColor }}>
          <div className="circles transition-color"></div>
        </div>
        <div className='loading-container' style={{ '--random-color': randomColor }}>
          <div className="loading transition-color">
            <div className="loading-box">
              <div className="WH color l1"></div>
              <div className="ball color"></div>
              <div className="WH color l2"></div>
            </div>
          </div>
        </div>
        <div className='notes-container transition-effect'>
          <div className="notes transition-color">
            <span className="icon">
              <svg xmlns="http://www.w3.org/2000/svg" version="1.1" x="0" y="0" viewBox="0 0 100 100">
                <ellipse transform="rotate(-21.283 49.994 75.642)" cx="50" cy="75.651" rx="19.347" ry="16.432" fill={randomColor}></ellipse>
                <path fill={randomColor} d="M58.474 7.5h10.258v63.568H58.474z"></path>
              </svg>
            </span>
            <span className="icon">
              <svg xmlns="http://www.w3.org/2000/svg" version="1.1" x="0" y="0" viewBox="0 0 100 100">
                <ellipse transform="rotate(-21.283 49.994 75.642)" cx="50" cy="75.651" rx="19.347" ry="16.432" fill={randomColor}></ellipse>
                <path fill={randomColor} d="M58.474 7.5h10.258v63.568H58.474z"></path>
              </svg>
            </span>
            <span className="icon">
              <svg xmlns="http://www.w3.org/2000/svg" version="1.1" x="0" y="0" viewBox="0 0 100 100">
                <ellipse transform="rotate(-21.283 49.994 75.642)" cx="50" cy="75.651" rx="19.347" ry="16.432" fill={randomColor}></ellipse>
                <path fill={randomColor} d="M58.474 7.5h10.258v63.568H58.474z"></path>
              </svg>
            </span>
          </div>
        </div>
        <div className='pac-mans-container' style={{ '--random-color': randomColor }}>
          <div className="pac-mans transition-color">
            <div className="pac-man"></div>
            <div className="point p1"></div>
            <div className="point p2"></div>
          </div>
        </div>

        <div className='boxes-container' style={{ '--random-color': randomColor }}>
          <div className="boxes transition-color">
            <div className="box1"></div>
            <div className="box2"></div>
            <div className="box3"></div>
          </div>
        </div>
        <div className='shuriken-container transition-effect'>
          <div className="shuriken transition-color" style={{ '--random-color': randomColor }}></div>
        </div>

        <div className="quarter transition-color">
          <div className="quater i"></div>
          <div className="quater ii"></div>
        </div>
        <div className='spinner-container'>
          <div className="spinner transition-color" style={{ '--random-color': randomColor }}></div>
        </div>
        <Navbar appName="" randomColor={randomColor} />
        <div className="content-container mt-4">
          <Routes>
            <Route path="/" element={<Inicio />} />
            <Route path="/descarga" element={<Descarga randomColor={randomColor} />} />
            <Route path="/soporte" element={<Soporte />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
