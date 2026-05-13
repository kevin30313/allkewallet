import React, { useRef, useEffect } from 'react';

const TurtleCanvas = () => {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    let animationFrameId;

    const resize = () => {
      canvas.width = canvas.offsetWidth;
      canvas.height = canvas.offsetHeight;
    };
    window.addEventListener('resize', resize);
    resize();

    let offset = 0;

    const drawGrid = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      
      const size = 50; // Tamaño de los cubos
      ctx.setLineDash([5, 15]); // Líneas punteadas para un look más técnico
      ctx.lineWidth = 1;
      
      offset += 0.2; // Velocidad del movimiento

      for (let x = -size; x < canvas.width + size; x += size) {
        for (let y = -size; y < canvas.height + size; y += size) {
          // Color azul neón muy suave para no opacar el texto
          ctx.strokeStyle = `rgba(0, 242, 255, ${0.1 + Math.sin((x + y + offset) * 0.05) * 0.05})`;
          
          ctx.beginPath();
          // Dibujo de rombos isométricos
          ctx.moveTo(x + (offset % size), y);
          ctx.lineTo(x + size / 2 + (offset % size), y + size / 2);
          ctx.lineTo(x + (offset % size), y + size);
          ctx.lineTo(x - size / 2 + (offset % size), y + size / 2);
          ctx.closePath();
          ctx.stroke();
        }
      }

      animationFrameId = requestAnimationFrame(drawGrid);
    };

    drawGrid();

    return () => {
      cancelAnimationFrame(animationFrameId);
      window.removeEventListener('resize', resize);
    };
  }, []);

  return <canvas ref={canvasRef} style={{ width: '100%', height: '100%', opacity: 0.6 }} />;
};

export default TurtleCanvas;