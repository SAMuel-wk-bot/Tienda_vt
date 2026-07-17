package com.tienda.service;

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final FirebaseStorageService firebaseStorageService;

    public ProductoService(ProductoRepository productoRepository,
            FirebaseStorageService firebaseStorageService) {
        this.productoRepository = productoRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean soloActivos) {
        return soloActivos ? productoRepository.findByActivoTrue() : productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductosPorCategoria(Integer idCategoria) {
        return productoRepository.findByCategoria_IdCategoriaAndActivoTrue(idCategoria);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaDerivada(BigDecimal precioInf, BigDecimal precioSup) {
        return productoRepository.findByPrecioBetweenOrderByPrecioAsc(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaJPQL(BigDecimal precioInf, BigDecimal precioSup) {
        return productoRepository.consultaJPQL(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaSQL(BigDecimal precioInf, BigDecimal precioSup) {
        return productoRepository.consultaSQL(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Transactional(rollbackFor = IOException.class)
    public void save(Producto producto, MultipartFile imagenFile) throws IOException {
        Producto guardado = productoRepository.save(producto);
        if (imagenFile != null && !imagenFile.isEmpty()) {
            guardado.setRutaImagen(firebaseStorageService.uploadImage(
                    imagenFile, "producto", guardado.getIdProducto()));
            productoRepository.save(guardado);
        }
    }

    @Transactional
    public void delete(Integer idProducto) {
        if (!productoRepository.existsById(idProducto)) {
            throw new IllegalArgumentException("El producto no existe.");
        }
        productoRepository.deleteById(idProducto);
    }
    @Transactional(readOnly = true)
public List<Producto> consultaAmpliada(
        String descripcion,
        Integer idCategoria,
        BigDecimal precioMin,
        BigDecimal precioMax) {

    String textoBusqueda = descripcion;

    if (textoBusqueda != null && textoBusqueda.isBlank()) {
        textoBusqueda = null;
    }

    return productoRepository.consultaAmpliada(
            textoBusqueda,
            idCategoria,
            precioMin,
            precioMax
    );
}
}
