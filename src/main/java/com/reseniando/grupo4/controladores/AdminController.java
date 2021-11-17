package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.servicios.LibroServicio;
import com.reseniando.grupo4.servicios.PrestamoServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

        return "librosAdmin";
    }
    
    
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String listarPrestamos( Model model, HttpSession session ) {
        model.addAttribute("prestamos", prestamoServicio.listarTodo());
        
        return "prestamos";
    }

}
