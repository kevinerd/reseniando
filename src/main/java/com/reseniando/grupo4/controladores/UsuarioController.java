package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.servicios.UsuarioServicio;
import com.reseniando.grupo4.errores.ErrorServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
public class UsuarioController {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/editar-usuario")
    public String editarUsuario( HttpSession session, @RequestParam String dni, ModelMap model ) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getDni().equals(dni)) {
            return "redirect:/inicio";
        }
        try {
            Usuario usuario = usuarioServicio.encontrarPorDni( dni );
            model.addAttribute( "usuario", usuario );
        } catch( ErrorServicio e ) {
            model.addAttribute( "error", e.getMessage() );
        }
        
        return "perfil";
    }
    
    @PostMapping("/actualizar-usuario")
    public String actualizarUsuario(
            ModelMap modelo, 
            HttpSession session,
            @RequestParam String dni,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String mail,
            @RequestParam String password1,
            @RequestParam String password2,
            @RequestParam String direccion
    ) {
        Usuario usuario = null;
        
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null || !login.getDni().equals(dni)) {
                return "redirect:/inicio";
            }
        
            usuario = usuarioServicio.encontrarPorDni( dni );
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(mail);
            usuario.setPass(password1);
            usuario.setDomicilio(direccion);
            usuarioServicio.modificarUsuario( dni, nombre, apellido, direccion, mail, password1, password2 );
        } catch( ErrorServicio ex ) {
            modelo.put( "error", ex.getMessage() );
            modelo.put( "usuario", usuario );
            
            return "registro.html";
        }
        
        return "inicio";
    }
}
