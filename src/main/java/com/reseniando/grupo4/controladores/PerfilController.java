package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Perfil;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.UsuarioRepositorio;
import com.reseniando.grupo4.servicios.PerfilServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private PerfilServicio perfilServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @GetMapping("/crear-perfil")
    public String crearPerfil( HttpSession session ) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if( login == null ) {
            return "redirect:/logout";
        }
        return "crearPerfil";
    }

    @PostMapping("/registrar")
    public String registrar(
            ModelMap model,
            HttpSession session,
            MultipartFile archivo,
            @RequestParam String nickname,
            @RequestParam String bio
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if( usuario == null ) {
            return "redirect:/logout";
        }
        
        try {
            Perfil perfil = perfilServicio.registrar(archivo, nickname, bio);
            usuario.setPerfil(perfil);
            usuarioRepositorio.save(usuario);
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            model.put("nickname", nickname);
            model.put("bio", bio);

            return "crearPerfil";
        }
        model.put("titulo", "¡Bienvenido a Reseñando!");
        model.put("descripcion", "Ya puedes disfrutar de toda la plataforma.");
        session.setAttribute("usuariosession", usuario);
        return "exito";
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil( HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if( login == null ) {
            return "redirect:/logout";
        }
        try {
            Perfil perfil = perfilServicio.findById( login.getPerfil().getId() );
            model.addAttribute("perfil", perfil);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        return "modificarPerfil";
    }

    @PostMapping("/actualizar-perfil")
    public String actualizar(
            ModelMap modelo,
            HttpSession session,
            MultipartFile archivo,
            @RequestParam String id,
            @RequestParam String nickname,
            @RequestParam String bio
    ) {
        Perfil perfil = null;

        try {
            perfil = perfilServicio.findById(id);
            perfilServicio.modificarPerfil(archivo, id, nickname, bio);
            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put( "archivo", archivo );
            modelo.put("perfil", perfil);

            return "modificarPerfil";
        }
    }
}
