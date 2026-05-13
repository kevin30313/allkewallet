import React, { useRef, useEffect } from 'react';

const TurtleCanvas = () => {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    
    // Ajustar tamaño al contenedor
    canvas.width = canvas.offsetWidth;
    canvas.height = canvas.offsetHeight;

    const nodos = [];
    const numNodos = 12;

    // 1. Equivalente a tu bucle for i in range(10)
    for (let i = 0; i < numNodos; i++) {
      nodos.push({
        x: Math.random() * canvas.width,
        y: Math.random() * canvas.height,
        color: `hsl(${(i / numNodos) * 360}, 100%, 50%)` // Colores dinámicos
      });
    }

    const draw = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      // 2. Dibujar conexiones (for i in range(len(nodos))...)
      nodos.forEach((nodoA, i) => {
        nodos.slice(i + 1).forEach((nodoB, j) => {
          ctx.beginPath();
          ctx.moveTo(nodoA.x, nodoA.y);
          ctx.lineTo(nodoB.x, nodoB.y);
          ctx.strokeStyle = `hsla(${(i + j) * 15}, 100%, 50%, 0.2)`;
          ctx.lineWidth = 1;
          ctx.stroke();
        });
      });

      // 3. Dibujar nodos (dibujar_nodo)
      nodos.forEach(nodo => {
        ctx.beginPath();
        ctx.arc(nodo.x, nodo.y, 6, 0, Math.PI * 2);
        ctx.fillStyle = nodo.color;
        ctx.shadowBlur = 15;
        ctx.shadowColor = nodo.color;
        ctx.fill();
      });
    };

    draw();
  }, []);

  return <canvas ref={canvasRef} style={{ width: '100%', height: '100%' }} />;
};

export default TurtleCanvas;