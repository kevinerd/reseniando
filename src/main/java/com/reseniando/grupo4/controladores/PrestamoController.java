package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Prestamo;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.LibroRepositorio;
import com.reseniando.grupo4.servicios.LibroServicio;
import com.reseniando.grupo4.servicios.PrestamoServicio;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoServicio prestamoServicio;

    @Autowired
    private LibroRepositorio lr;

    @GetMapping("/lista")
    public String listarPrestamos(Model model, @RequestParam(required = false) String id) {

        model.addAttribute("prestamos-lista", prestamoServicio.listarTodo());

        return "prestamos-lista";
    }

    @GetMapping("/prestamoForm")
    public String formularioprestamo(ModelMap model) {

        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaEstimativa = fechaPrestamo.plusDays(7);

        model.addAttribute("fechaPrestamo", fechaPrestamo);
        model.addAttribute("fechaEstimativa", fechaEstimativa);
        model.addAttribute("prestamoNuevo", new Prestamo());
        model.addAttribute("libro", lr.findAll());
        
        return "prestamoForm";

    }

    @PostMapping("/guardar-prestamo")
    public String guardar(@RequestParam LocalDate fechaPrestamo, @RequestParam LocalDate fechaEstimativa, @RequestParam(required=false) LocalDate fechaDevolucion, @RequestParam(required=false) Boolean devuelto, @RequestParam Libro libro, @RequestParam(required= false) Usuario usuario) throws ErrorServicio {

        prestamoServicio.agregarPrestamo(fechaPrestamo, fechaEstimativa, fechaDevolucion, devuelto, libro, usuario);

        return "exito";
    }
}
