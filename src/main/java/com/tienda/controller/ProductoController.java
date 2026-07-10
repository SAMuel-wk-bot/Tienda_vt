package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("totalProductos", productos.size());
        return "producto/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Producto producto, BindingResult errores,
            @RequestParam(name = "imagenFile", required = false) MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {
        if (errores.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Revise los datos del producto.");
            return "redirect:/producto/listado";
        }
        try {
            productoService.save(producto, imagenFile);
            redirectAttributes.addFlashAttribute("todoOk", "Producto guardado correctamente.");
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/producto/listado";
    }

    @GetMapping("/modificar/{idProducto}")
    public String modificar(@PathVariable Integer idProducto, Model model,
            RedirectAttributes redirectAttributes) {
        var producto = productoService.getProducto(idProducto);
        if (producto.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El producto solicitado no existe.");
            return "redirect:/producto/listado";
        }
        model.addAttribute("producto", producto.get());
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        return "producto/modifica";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProducto,
            RedirectAttributes redirectAttributes) {
        try {
            productoService.delete(idProducto);
            redirectAttributes.addFlashAttribute("todoOk", "Producto eliminado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/producto/listado";
    }
}
