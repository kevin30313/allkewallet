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
      
      const size = 60; // Tamaño de la red
      ctx.setLineDash([2, 10]); // Puntos finos para un look técnico
      ctx.lineWidth = 1;
      
      offset += 0.15; // Velocidad de flujo

      for (let x = -size; x < canvas.width + size; x += size) {
        for (let y = -size; y < canvas.height + size; y += size) {
          // Color cian muy tenue que parpadea levemente
          const alpha = 0.05 + Math.sin((x + y + offset * 10) * 0.02) * 0.03;
          ctx.strokeStyle = `rgba(0, 242, 255, ${alpha})`;
          
          ctx.beginPath();
          // Geometría de rombo isométrica
          const moveX = x + (offset % size);
          ctx.moveTo(moveX, y);
          ctx.lineTo(moveX + size / 2, y + size / 2);
          ctx.lineTo(moveX, y + size);
          ctx.lineTo(moveX - size / 2, y + size / 2);
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

  return (
    <canvas 
      ref={canvasRef} 
      style={{ 
        width: '100%', 
        height: '100%', 
        display: 'block'
      }} 
    />
  );
};

export default TurtleCanvas;