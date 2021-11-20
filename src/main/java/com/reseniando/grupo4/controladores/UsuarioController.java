package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.enumeraciones.Generos;
import com.reseniando.grupo4.servicios.UsuarioServicio;
import com.reseniando.grupo4.errores.ErrorServicio;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
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
    public String editarUsuario( HttpSession session, @RequestParam String dni, ModelMap model ) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getDni().equals(dni)) {
            model.addAttribute( "error", "Sólo puede modificar su usuario." );
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            return "redirect:/perfil";
        }
        try {
            Usuario usuario = usuarioServicio.encontrarPorDni( dni );
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            model.addAttribute( "usuario", usuario );
        } catch( ErrorServicio e ) {
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombre = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombre, valueGen);
            }
            model.addAttribute("generos", generos);
            model.addAttribute( "error", e.getMessage() );
        }
        
        return "modificarUsuario";
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
            usuario = usuarioServicio.encontrarPorDni(dni);
            if (usuario == null || !login.getDni().equals(dni)) {
                modelo.put( "error", "Sólo puede modificar su usuario." );
                HashMap<String, String> generos = new HashMap();
                for (Generos nombreGen : Generos.values()) {
                    String nombreG = nombreGen.name();
                    String valueGen = nombreGen.getGen();
                    generos.put(nombreG, valueGen);
                }
                modelo.addAttribute("generos", generos);
                return "redirect:/perfil/";
            }
            
            usuarioServicio.modificarUsuario( dni, nombre, apellido, direccion, mail, password1, password2 );
        } catch( ErrorServicio ex ) {
            modelo.put( "error", ex.getMessage() );
            
            modelo.addAttribute("usuario", usuario );
            HashMap<String, String> generos = new HashMap();
            for (Generos nombreGen : Generos.values()) {
                String nombreG = nombreGen.name();
                String valueGen = nombreGen.getGen();
                generos.put(nombreG, valueGen);
            }
            modelo.addAttribute("generos", generos);
            
            return "modificarUsuario";
        }
        
        return "inicio";
    }
}
