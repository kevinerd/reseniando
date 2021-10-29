package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Perfil;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.servicios.PerfilServicio;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @GetMapping("/crear-perfil")
    public String crearPerfil() {
        return "perfilForm";
    }
    
    @PostMapping("/registrar")
    public String registrar(
            ModelMap model,
            MultipartFile archivo,
            @RequestParam String nickname,
            @RequestParam String bio
    ) {
        try {
            perfilServicio.registrar( archivo, nickname, bio );
        } catch( ErrorServicio e ) {
            model.put( "error", e.getMessage() );
            model.put( "nickname", nickname );
            model.put( "bio", bio );
            
            return "perfilForm";
        }
        model.put( "titulo", "¡Bienvenido a Reseñando!" );
        model.put( "descripcion", "Ya puedes disfrutar de toda la plataforma." );
        
        return "exito";
    }
    
    @GetMapping("/editar-perfil")
    public String editarPerfil( @RequestParam String id, ModelMap model ) {
        try {
            Perfil perfil = perfilServicio.findById(id);
            model.addAttribute( "perfil", perfil );
        } catch( ErrorServicio e ) {
            model.addAttribute( "error", e.getMessage() );
        }
        
        return "perfil";
    }
    
    @PostMapping("/actualizar-perfil")
    public String registrar(
            ModelMap modelo,
            MultipartFile foto,
            @RequestParam String id,
            @RequestParam String nickname,
            @RequestParam String bio
    ) {
        Perfil perfil = null;
        
        try {
            perfil = perfilServicio.findById( id );
            perfilServicio.modificarPerfil( foto, id, nickname, bio );
        } catch( ErrorServicio ex ) {
            modelo.put( "error", ex.getMessage() );
            modelo.put( "perfil", perfil );
            
            return "registro";
        }
        
        return "inicio";
    }
}
