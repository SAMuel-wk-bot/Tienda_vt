package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.ProductoService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ProductoService productoService;

    public ConsultaController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        prepararModelo(model, productoService.getProductos(false), null, null, "Todos los productos");
        return "consultas/listado";
    }

    @GetMapping("/derivada")
    public String consultaDerivada(@RequestParam BigDecimal precioInf,
            @RequestParam BigDecimal precioSup, Model model) {
        prepararModelo(model, productoService.consultaDerivada(precioInf, precioSup),
                precioInf, precioSup, "Consulta derivada");
        return "consultas/listado";
    }

    @GetMapping("/jpql")
    public String consultaJPQL(@RequestParam BigDecimal precioInf,
            @RequestParam BigDecimal precioSup, Model model) {
        prepararModelo(model, productoService.consultaJPQL(precioInf, precioSup),
                precioInf, precioSup, "Consulta JPQL");
        return "consultas/listado";
    }

    @GetMapping("/sql")
    public String consultaSQL(@RequestParam BigDecimal precioInf,
            @RequestParam BigDecimal precioSup, Model model) {
        prepararModelo(model, productoService.consultaSQL(precioInf, precioSup),
                precioInf, precioSup, "Consulta SQL nativa");
        return "consultas/listado";
    }

    private void prepararModelo(Model model, List<Producto> productos,
            BigDecimal precioInf, BigDecimal precioSup, String tipoConsulta) {
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        model.addAttribute("tipoConsulta", tipoConsulta);
    }
}
