package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
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
    private final CategoriaService categoriaService;

    public ConsultaController(
            ProductoService productoService,
            CategoriaService categoriaService) {

        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {

        prepararModelo(
                model,
                productoService.getProductos(false),
                null,
                null,
                "Todos los productos"
        );

        prepararConsultaAmpliada(
                model,
                null,
                null,
                null,
                null
        );

        return "consultas/listado";
    }

    @GetMapping("/derivada")
    public String consultaDerivada(
            @RequestParam BigDecimal precioInf,
            @RequestParam BigDecimal precioSup,
            Model model) {

        prepararModelo(
                model,
                productoService.consultaDerivada(
                        precioInf,
                        precioSup
                ),
                precioInf,
                precioSup,
                "Consulta derivada"
        );

        prepararConsultaAmpliada(
                model,
                null,
                null,
                null,
                null
        );

        return "consultas/listado";
    }

    @GetMapping("/jpql")
    public String consultaJPQL(
            @RequestParam BigDecimal precioInf,
            @RequestParam BigDecimal precioSup,
            Model model) {

        prepararModelo(
                model,
                productoService.consultaJPQL(
                        precioInf,
                        precioSup
                ),
                precioInf,
                precioSup,
                "Consulta JPQL"
        );

        prepararConsultaAmpliada(
                model,
                null,
                null,
                null,
                null
        );

        return "consultas/listado";
    }

    @GetMapping("/sql")
    public String consultaSQL(
            @RequestParam BigDecimal precioInf,
            @RequestParam BigDecimal precioSup,
            Model model) {

        prepararModelo(
                model,
                productoService.consultaSQL(
                        precioInf,
                        precioSup
                ),
                precioInf,
                precioSup,
                "Consulta SQL nativa"
        );

        prepararConsultaAmpliada(
                model,
                null,
                null,
                null,
                null
        );

        return "consultas/listado";
    }

    /*
     * Práctica #2.
     */
    @GetMapping("/ampliada")
    public String consultaAmpliada(
            @RequestParam(required = false)
            String descripcion,

            @RequestParam(required = false)
            Integer idCategoria,

            @RequestParam(required = false)
            BigDecimal precioMin,

            @RequestParam(required = false)
            BigDecimal precioMax,

            Model model) {

        if (precioMin != null
                && precioMax != null
                && precioMin.compareTo(precioMax) > 0) {

            model.addAttribute(
                    "errorConsulta",
                    "El precio mínimo no puede ser mayor "
                    + "que el precio máximo."
            );

            prepararModelo(
                    model,
                    List.of(),
                    null,
                    null,
                    "Consulta ampliada de productos"
            );

        } else {

            List<Producto> resultados
                    = productoService.consultaAmpliada(
                            descripcion,
                            idCategoria,
                            precioMin,
                            precioMax
                    );

            prepararModelo(
                    model,
                    resultados,
                    null,
                    null,
                    "Resultado de la consulta ampliada"
            );
        }

        prepararConsultaAmpliada(
                model,
                descripcion,
                idCategoria,
                precioMin,
                precioMax
        );

        return "consultas/listado";
    }

    private void prepararModelo(
            Model model,
            List<Producto> productos,
            BigDecimal precioInf,
            BigDecimal precioSup,
            String tipoConsulta) {

        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        model.addAttribute("tipoConsulta", tipoConsulta);
    }

    private void prepararConsultaAmpliada(
            Model model,
            String descripcion,
            Integer idCategoria,
            BigDecimal precioMin,
            BigDecimal precioMax) {

        model.addAttribute(
                "categorias",
                categoriaService.getCategorias(false)
        );

        model.addAttribute(
                "descripcionBusqueda",
                descripcion
        );

        model.addAttribute(
                "idCategoriaBusqueda",
                idCategoria
        );

        model.addAttribute(
                "precioMinBusqueda",
                precioMin
        );

        model.addAttribute(
                "precioMaxBusqueda",
                precioMax
        );
    }
}