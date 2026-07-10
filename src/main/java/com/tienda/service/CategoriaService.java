package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final FirebaseStorageService firebaseStorageService;

    public CategoriaService(CategoriaRepository categoriaRepository,
            FirebaseStorageService firebaseStorageService) {
        this.categoriaRepository = categoriaRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean soloActivas) {
        return soloActivas ? categoriaRepository.findByActivoTrue() : categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Transactional(rollbackFor = IOException.class)
    public void save(Categoria categoria, MultipartFile imagenFile) throws IOException {
        Categoria guardada = categoriaRepository.save(categoria);
        if (imagenFile != null && !imagenFile.isEmpty()) {
            guardada.setRutaImagen(firebaseStorageService.uploadImage(
                    imagenFile, "categoria", guardada.getIdCategoria()));
            categoriaRepository.save(guardada);
        }
    }

    @Transactional
    public void delete(Integer idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new IllegalArgumentException("La categoría no existe.");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
            categoriaRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("La categoría tiene productos asociados.", ex);
        }
    }
}
