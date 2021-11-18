package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Prestamo;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.LibroRepositorio;
import com.reseniando.grupo4.repositorios.PrestamoRepositorio;
import com.reseniando.grupo4.repositorios.UsuarioRepositorio;
import com.reseniando.grupo4.servicios.PrestamoServicio;
import java.time.LocalDate;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoServicio prestamoServicio;
    
    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @GetMapping("/crear-prestamo")
    public String crearPrestamo( ModelMap model, HttpSession session ) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if( login == null ) {
            return "redirect:/logout";
        }
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaEstimativa = fechaPrestamo.plusDays(7);

        model.addAttribute("fechaPrestamo", fechaPrestamo);
        model.addAttribute("fechaEstimativa", fechaEstimativa);
        model.addAttribute("libros", libroRepositorio.findAll());
        
        return "crearPrestamo";
    }
    
    @PostMapping("/registrar-prestamo")
    public String registrarPrestamo(
            ModelMap model,
            @RequestParam Long isbn,
            @RequestParam String dni
    ) {
        try {
            if (dni != null) {
                Usuario usuario = usuarioRepositorio.findByDni(dni);
                Libro libro = libroRepositorio.buscarPorIsbn(isbn);
                prestamoServicio.registrarPrestamo(libro, usuario);
            }
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());

            return "crearPrestamo";
        }
        model.put("titulo", "Prestamo registrado exitosamente.");
        model.put("descripcion", "Que disfrutes tu lectura.");

        return "exito";
    }
    
    @GetMapping("/editar-prestamo")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editarPrestamo( @RequestParam String id, ModelMap model, HttpSession session ) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if( login == null ) {
            return "redirect:/logout";
        }
        try {
            Prestamo prestamo = prestamoServicio.findById(id);
            Usuario usuario = usuarioRepositorio.findByDni(prestamo.usuario.getDni());
            Libro libro = libroRepositorio.buscarPorIsbn(prestamo.libro.getIsbn());
            
            model.addAttribute("prestamo", prestamo);
            model.addAttribute("usuario", usuario);
            model.addAttribute("libro", libro);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        return "modificarPrestamo";
    }

    @PostMapping("/actualizar-prestamo")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String actualizar(
            ModelMap modelo,
            HttpSession session,
            @RequestParam String id,
            @RequestParam Boolean devuelto
    ) {
        Prestamo prestamo = new Prestamo();

        try {
            Optional<Prestamo> respuesta = prestamoRepositorio.findById( id );
            if( respuesta.isPresent() ) {
                prestamo = respuesta.get();
                if( !devuelto.equals(prestamo.getDevuelto()) ) {
                    prestamoServicio.modificarPrestamo(id);
                }
            } else {
                modelo.put("titulo", "Prestamo no modificado");
                modelo.put("descripcion", "Prestamo no modificado correctamente.");
                return "exito";
            }
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("prestamo", prestamo);

            return "prestamo";
        }
        modelo.put("titulo", "Préstamo modificado");
        modelo.put("descripcion", "Préstamo modificado correctamente.");
        return "exito";
    }
}
