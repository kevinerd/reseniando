package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }
    
    @GetMapping("/login")
    public String login( @RequestParam( required = false ) String error, @RequestParam( required = false ) String logout, ModelMap model ) {
        if( error != null ) {
            model.put("error", "Credenciales invalidas. Intente nuevamente.");
        }
        if( logout != null ) {
            model.put("logout", "Has cerrado sesion." );
        }
        return "login";
    }
    
    @GetMapping("/registro")
    public String registro( ModelMap modelo ) {
        return "registro";
    }
    
    @PostMapping("/registrar")
    public String registrar(
            ModelMap modelo,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String mail,
            @RequestParam String pass1,
            @RequestParam String pass2,
            @RequestParam String dni,
            @RequestParam String direccion
    ) {
        try {
            usuarioServicio.crearUsuario( dni, nombre, apellido, direccion, mail, pass1, pass2 );
        } catch( ErrorServicio ex ) {
            modelo.put( "error", ex.getMessage() );
            modelo.put( "nombre", nombre );
            modelo.put( "apellido", apellido );
            modelo.put( "mail", mail );
            modelo.put( "password1", pass1 );
            modelo.put( "password2", pass2 );
            modelo.put( "dni", dni );
            modelo.put( "direccion", direccion );
            
            return "registro";
        }
        
        modelo.put( "titulo", "¡Bienvenido a Reseniando!" );
        modelo.put( "descripcion", "Tu usuario fue registrado correctamente." );
        
        return "exito";
    }
}
