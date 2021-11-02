package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroServicio ls;

    @GetMapping("/form")
    public String form() {
        return "crearLibro";
    }

    @PostMapping("/save")
    public String registrarLibro(
            ModelMap modelo,
            MultipartFile archivo,
            @RequestParam Long isbn,
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam String editorial,
            @RequestParam Integer anio,
            @RequestParam String sinopsis,
            @RequestParam Integer ejemplares,
            @RequestParam Integer ejemplaresPrestados
    ) {
        try {
            ls.agregarLibro(archivo, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresPrestados, Boolean.FALSE, autor, editorial, sinopsis);
        } catch (ErrorServicio ex) {
            System.out.println("Hola, se cagó algo.");
            return "registro";
        }

        modelo.put("titulo", "¡Libro ingresado!");
        modelo.put("descripcion", "Libro ingresado.");
        return "exito";
    }
}
