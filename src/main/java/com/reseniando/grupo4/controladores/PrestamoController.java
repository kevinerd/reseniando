
package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Prestamo;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.servicios.PrestamoServicio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
@RequestMapping("/prestamos")
public class PrestamoController {
    
    @Autowired
    private PrestamoServicio prestamoServicio;
        
    @GetMapping("/lista")
    public String listarPrestamos(Model model, @RequestParam(required = false) String id){
        
            model.addAttribute("prestamos-lista", prestamoServicio.listarTodo());
        
        return "prestamos-lista";
    }
    
    @GetMapping("/formulario-prestamo")
    public String formularioprestamo(Model model, @RequestParam(required = false) String id){
        
        if (id != null) {
            Optional<Prestamo> opcional = prestamoServicio.buscarPorid(id);

            if (opcional.isPresent()) {
                model.addAttribute("prestamo", opcional.get());
            } else {
                return "redirect: prestamo/formulario-prestamo";
            }

        } else {
            model.addAttribute("prestamoNuevo", new Prestamo());
        }
        
        return "prestamo-formulario";
        
    }
    
    @PostMapping("/guardar-prestamo")
    public String guardar(@ModelAttribute Prestamo  prestamoNuevo ,@RequestParam Date fechaPrestamo,@RequestParam  Date fechaEstimativa,@RequestParam  Date fechaDevolucion,@RequestParam  Boolean devuelto,@RequestParam  Libro libro,@RequestParam  Usuario usuario) throws ErrorServicio{
     
        prestamoServicio.agregarPrestamo(prestamoNuevo);
        
        
        return "redirect: prestamo/formulario-prestamo";
    }
}
