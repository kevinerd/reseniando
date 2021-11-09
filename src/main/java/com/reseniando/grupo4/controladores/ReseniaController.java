package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Resenia;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.repositorios.LibroRepositorio;
import com.reseniando.grupo4.servicios.ReseniaServicio;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/resenias")
public class ReseniaController {

    @Autowired
    private ReseniaServicio reseniaServicio;
    
    @Autowired
    private LibroRepositorio libroRepositorio;
    
    @GetMapping("/lista")
    public String listarResenias( Model model ) {
        model.addAttribute("resenias", reseniaServicio.listarTodo());
        
        return "resenias";
    }

    @GetMapping("/crear-resenia")
    public String crearResenia( ModelMap model, HttpSession session, @RequestParam(required = false) Long isbn ){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Libro libro = libroRepositorio.buscarPorIsbn(isbn);
        
        if( usuario.getPerfil() == null ) {
            return "redirect:/inicio";
        }
        if( libro == null) {
            return "redirect:/inicio";
        }
        
        model.addAttribute("libro", libro);
        
        return "crearReseña";
    }
    
    @PostMapping("/registrar-resenia")
    public String registrarResenia(
            ModelMap model,
            HttpSession session,
            @RequestParam String titulo,
            @RequestParam String comentario,
            @RequestParam Long isbn
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if( usuario.getPerfil() == null ) {
            return "redirect:/logout";
        }
        
        try {
            Resenia resenia = new Resenia();
            resenia.setTitulo( titulo );
            resenia.setComentario( comentario );
            resenia.setFecha( LocalDate.now() );
            Libro libro = libroRepositorio.buscarPorIsbn(isbn);
            reseniaServicio.crearResenia( resenia, usuario.getPerfil(), libro );
        } catch (Exception e) {
            model.put("error", e.getMessage());
            model.put("titulo", titulo);
            model.put("comentario", comentario);
            return "crearReseña";
        }
        model.put("titulo", "¡Reseña registrada!");
        model.put("descripcion", "Gracias por compartir tu opinión.");
        return "exito";
    }
    
    @GetMapping("/editar-resenia")
    public String editarResenia( HttpSession session, ModelMap model, String id) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if( login == null ) {
            return "redirect:/logout";
        }
        try {
            List <Resenia> resenias = login.getPerfil().getResenias();
            Resenia resenia = reseniaServicio.findById(id);
            
            for(Resenia res : resenias) { 
                if( res.getId().equals(id) ) {
                    model.addAttribute("resenia", resenia);
                    return "modificarResenia";
                } else {
                    model.addAttribute("error", "No se pudo encontrar la reseña solicitada.");
                    return "/inicio";
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "modificarResenia";
    }

    @PostMapping("/actualizar-resenia")
    public String editarResenia(
        ModelMap model,
        HttpSession session,
        @RequestParam String id,
        @RequestParam String titulo,
        @RequestParam String comentario
    ){
        Resenia resenia = new Resenia();
        
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if( usuario.getPerfil() == null ) {
            return "redirect:/logout";
        }
        
        try {
            resenia = reseniaServicio.findById(id);
            resenia.setComentario(comentario);
            resenia.setTitulo(titulo);
            reseniaServicio.modificarResenia(resenia);
        } catch (Exception e) {
            model.put("error", e.getMessage());
            model.addAttribute("resenia", resenia);
            return "modificarResenia";
        }
        
        model.put("titulo", "¡Reseña editada!");
        model.put("descripcion", "Gracias por compartir tu opinión.");
        return "exito";
    }
}