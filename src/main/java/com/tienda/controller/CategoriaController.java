package com.tienda.controller;

import com.tienda.domain.Categoria;
import com.tienda.service.CategoriaService;
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
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("totalCategorias", categorias.size());
        return "categoria/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Categoria categoria, BindingResult errores,
            @RequestParam(name = "imagenFile", required = false) MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {
        if (errores.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Revise los datos de la categoría.");
            return "redirect:/categoria/listado";
        }
        try {
            categoriaService.save(categoria, imagenFile);
            redirectAttributes.addFlashAttribute("todoOk", "Categoría guardada correctamente.");
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/categoria/listado";
    }

    @GetMapping("/modificar/{idCategoria}")
    public String modificar(@PathVariable Integer idCategoria, Model model,
            RedirectAttributes redirectAttributes) {
        var categoria = categoriaService.getCategoria(idCategoria);
        if (categoria.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La categoría solicitada no existe.");
            return "redirect:/categoria/listado";
        }
        model.addAttribute("categoria", categoria.get());
        return "categoria/modifica";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCategoria,
            RedirectAttributes redirectAttributes) {
        try {
            categoriaService.delete(idCategoria);
            redirectAttributes.addFlashAttribute("todoOk", "Categoría eliminada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/categoria/listado";
    }
}
