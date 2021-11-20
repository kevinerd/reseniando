package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.enumeraciones.Generos;
import com.reseniando.grupo4.servicios.LibroServicio;
import com.reseniando.grupo4.servicios.PrestamoServicio;
import com.reseniando.grupo4.servicios.UsuarioServicio;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdminController {
    
    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private PrestamoServicio prestamoServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/libros")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String listarLibros( Model model, HttpSession session, @RequestParam(required=false) String query ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if( usuario == null ) {
            return "redirect:/logout";
        }
        
        if( query != null ) {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            model.addAttribute("libros", libroServicio.listarLibrosByQuery(query));
        } else {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            model.addAttribute("libros", libroServicio.listarTodo());
        }
        
        return "librosAdmin";
    }
    
    
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String listarPrestamos( ModelMap model, HttpSession session, @RequestParam(required=false) String query ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if( usuario == null ) {
            return "redirect:/logout";
        }
        
        if( query != null ) {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            model.addAttribute("prestamos", prestamoServicio.listarPrestamosByQuery(query));
        } else {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            model.addAttribute("prestamos", prestamoServicio.listarTodo());
        }
        
        return "prestamos";
    }
    
    @GetMapping("/usuarios")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String listarUsuarios( ModelMap model, HttpSession session, @RequestParam(required=false) String query ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if( usuario == null ) {
            return "redirect:/logout";
        }
        
        if( query != null ) {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            model.addAttribute("usuarios", usuarioServicio.listarUsuariosByQuery(query));
        } else {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            model.addAttribute("usuarios", usuarioServicio.listarUsuarios());
        }
        
        return "usuarios";
    }

}
