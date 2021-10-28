package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.servicios.UsuarioServicio;
import com.reseniando.grupo4.errores.ErrorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/editar-usuario")
    public String editarUsuario( @RequestParam Long dni, ModelMap model ) {
        try {
            Usuario usuario = usuarioServicio.buscarPorDni( dni );
            model.addAttribute( "usuario", usuario );
        } catch( ErrorServicio e ) {
            model.addAttribute( "error", e.getMessage() );
        }
        
        return "perfil";
    }
    
    @PostMapping("/actualizar-usuario")
    public String actualizarUsuario(
            ModelMap modelo,
            @RequestParam Long dni,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String mail,
            @RequestParam String password1,
            @RequestParam String password2,
            @RequestParam String direccion
    ) {
        Usuario usuario = null;
        
        try {
            usuario = usuarioServicio.buscarPorDni( dni );
            usuarioServicio.modificar( dni, nombre, apellido, mail, password1, password2, direccion );
        } catch( ErrorServicio ex ) {
            modelo.put( "error", ex.getMessage() );
            modelo.put( "usuario", usuario );
            
            return "registro.html";
        }
        
        return "inicio";
    }
}
