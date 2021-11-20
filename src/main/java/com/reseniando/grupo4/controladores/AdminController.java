package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.enumeraciones.Generos;
import com.reseniando.grupo4.servicios.LibroServicio;
import com.reseniando.grupo4.servicios.PrestamoServicio;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdminController {
    
    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private PrestamoServicio prestamoServicio;
    
    @GetMapping("/libros")
    public String listarLibros( Model model ) {
        model.addAttribute("libros", libroServicio.listarTodo());
        
        HashMap<String, String> generos = new HashMap();
        for (Generos nombreGen : Generos.values()) {
            String nombre = nombreGen.name();
            String valueGen = nombreGen.getGen();
            generos.put(nombre, valueGen);
        }
        model.addAttribute("generos", generos);

        return "librosAdmin";
    }
    
    
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String listarPrestamos( ModelMap model ) {
        HashMap<String, String> generos = new HashMap();
        for (Generos nombreGen : Generos.values()) {
            String nombre = nombreGen.name();
            String valueGen = nombreGen.getGen();
            generos.put(nombre, valueGen);
        }
        model.addAttribute("generos", generos);
        model.addAttribute("prestamos", prestamoServicio.listarTodo());
        
        return "prestamos";
    }

}
