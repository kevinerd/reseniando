package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.enumeraciones.Generos;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.LibroRepositorio;
import com.reseniando.grupo4.servicios.LibroServicio;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private LibroRepositorio libroRepositorio;
    
    @GetMapping("/lista")
    public String listarLibros( Model model, @RequestParam(required=false) String query ) {
        if( query != null ) {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("query", query);
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
        
        return "libros";
    }
    
    @GetMapping("/generos/")
    public String listarPorGenero( Model model, @RequestParam Generos genero ) {
        HashMap<String, String> generos = new HashMap();
        for (Generos nombreGen : Generos.values()) {
            String nombre = nombreGen.name();
            String valueGen = nombreGen.getGen();
            generos.put(nombre, valueGen);
        }
        model.addAttribute("genero", genero.getGen());
        model.addAttribute("generos", generos);
        model.addAttribute("libros", libroServicio.listarPorGenero(genero));
        return "generos";
    }
    
    @GetMapping("/libro")
    public String libro( Model model, @RequestParam Long isbn ) {
        Libro libro = libroRepositorio.buscarPorIsbn(isbn);
        
        HashMap<String, String> generos = new HashMap();
        for (Generos nombreGen : Generos.values()) {
            String nombre = nombreGen.name();
            String valueGen = nombreGen.getGen();
            generos.put(nombre, valueGen);
        }
        model.addAttribute("generos", generos);
        model.addAttribute("libro", libro);
        model.addAttribute("resenias", libro.getResenias());
        
        return "libro";
    }

    @GetMapping("/crear-libro")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String crearLibro( ModelMap model, HttpSession session ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        HashMap<String, String> generos = new HashMap();
        for (Generos nombreGen : Generos.values()) {
            String nombre = nombreGen.name();
            String valueGen = nombreGen.getGen();
            generos.put(nombre, valueGen);
        }
        model.addAttribute("generos", generos);
        
        if( usuario == null ) {
            return "redirect:/logout";
        }
        model.put("genero", Generos.values());
        
        return "crearLibro";
    }

    @PostMapping("/registrar-libro")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String registrarLibro(
            ModelMap modelo,
            HttpSession session,
            MultipartFile archivo,
            @RequestParam Long isbn,
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam String editorial,
            @RequestParam Integer anio,
            @RequestParam Generos genero,
            @RequestParam String sinopsis,
            @RequestParam Integer ejemplares,
            @RequestParam Integer ejemplaresPrestados,
            @RequestParam Boolean destacado
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        
        if( usuario == null ) {
            return "redirect:/logout";
        }
        try {
            libroServicio.agregarLibro(archivo, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresPrestados, Boolean.FALSE, autor, editorial, sinopsis, genero, destacado);
        } catch (ErrorServicio ex) {
            System.out.println("Hola, se cagó algo.");
            return "registro";
        }
        HashMap<String, String> generos = new HashMap();
        for (Generos nombreGen : Generos.values()) {
            String nombre = nombreGen.name();
            String valueGen = nombreGen.getGen();
            generos.put(nombre, valueGen);
        }
        modelo.addAttribute("generos", generos);
        modelo.put("titulo", "¡Libro ingresado!");
        modelo.put("descripcion", "Libro ingresado.");
        return "exito";
    }
    
    @GetMapping("/modificar-libro")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String modificarLibro( ModelMap model, HttpSession session, @RequestParam Long isbn ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Libro libro = libroRepositorio.buscarPorIsbn(isbn);
        
        if( usuario == null || libro == null) {
            return "redirect:/logout";
        }
        
        HashMap<String, String> generos = new HashMap();
        for (Generos nombreGen : Generos.values()) {
            String nombre = nombreGen.name();
            String valueGen = nombreGen.getGen();
            generos.put(nombre, valueGen);
        }
        model.addAttribute("generos", generos);
        model.addAttribute("libro", libro);
        model.put("genero", Generos.values());
        
        return "modificarLibro";
    }
    
    @PostMapping("/actualizar-libro")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String actualizarLibro( ModelMap modelo,
            MultipartFile archivo,
            @RequestParam Long isbn,
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam String editorial,
            @RequestParam Integer anio,
            @RequestParam Generos genero,
            @RequestParam String sinopsis,
            @RequestParam Integer ejemplares,
            @RequestParam Integer ejemplaresPrestados,
            @RequestParam Boolean alta,
            @RequestParam Boolean destacado
    ) {
        try {
            libroServicio.modificarLibro( archivo, isbn, titulo, autor, editorial, anio, genero, sinopsis, ejemplares, ejemplaresPrestados, alta, destacado);
        } catch ( ErrorServicio ex ) {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            modelo.addAttribute("generos", generos);
            return "registro";
        }
        HashMap<String, String> generos = new HashMap();
        for (Generos nombreGen : Generos.values()) {
            String nombre = nombreGen.name();
            String valueGen = nombreGen.getGen();
            generos.put(nombre, valueGen);
        }
        modelo.addAttribute("generos", generos);
        modelo.put("titulo", "¡Libro modificado!");
        modelo.put("descripcion", "Libro modificado correctamente.");
        return "exito";
    }
}
