package com.tienda.controller;

import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public IndexController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/")
    public String inicio(@RequestParam(required = false) Integer idCategoria, Model model) {
        var categorias = categoriaService.getCategorias(true);
        var productos = idCategoria == null
                ? productoService.getProductos(true)
                : productoService.getProductosPorCategoria(idCategoria);

        model.addAttribute("categorias", categorias);
        model.addAttribute("productos", productos);
        model.addAttribute("idCategoriaSeleccionada", idCategoria);
        return "index";
    }
}
