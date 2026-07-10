CREATE DATABASE IF NOT EXISTS tienda_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda_db;

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL,
    ruta_imagen VARCHAR(1024),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO categoria (descripcion, ruta_imagen, activo) VALUES
('Tecnología', 'https://images.unsplash.com/photo-1518770660439-4636190af475', TRUE),
('Hogar', 'https://images.unsplash.com/photo-1484101403633-562f891dc89a', TRUE),
('Accesorios', 'https://images.unsplash.com/photo-1523779917675-b6ed3a42a561', TRUE);

CREATE TABLE IF NOT EXISTS producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    descripcion VARCHAR(50) NOT NULL,
    detalle VARCHAR(1600),
    precio DECIMAL(10,2) NOT NULL,
    existencias INT NOT NULL DEFAULT 0,
    ruta_imagen VARCHAR(1024),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);
