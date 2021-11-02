package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Perfil;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.UsuarioRepositorio;
import com.reseniando.grupo4.servicios.PerfilServicio;
import java.util.Optional;
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

    @Autowired
    private UsuarioRepositorio ur;

    @GetMapping("/crear-perfil")
    public String crearPerfil(@RequestParam(required = true) String dni) {
        
        return "perfilForm";
    }

    @PostMapping("/registrar")
    public String registrar(
            ModelMap model,
            MultipartFile archivo,
            @RequestParam String nickname,
            @RequestParam String bio,
            @RequestParam String dni
    ) {
        try {
            if (dni != null) {
                Usuario usuario = ur.findByDni(dni);
                Perfil perfil = perfilServicio.registrar(archivo, nickname, bio);
                usuario.setPerfil(perfil);
                ur.save(usuario);
            }
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            model.put("nickname", nickname);
            model.put("bio", bio);

            return "perfilForm";
        }
        model.put("titulo", "¡Bienvenido a Reseñando!");
        model.put("descripcion", "Ya puedes disfrutar de toda la plataforma.");

        return "exito";
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(@RequestParam String id, ModelMap model) {
        try {
            Perfil perfil = perfilServicio.findById(id);
            model.addAttribute("perfil", perfil);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        return "perfil";
    }

    @PostMapping("/actualizar-perfil")
    public String registrar1(
            ModelMap modelo,
            MultipartFile foto,
            @RequestParam String id,
            @RequestParam String nickname,
            @RequestParam String bio
    ) {
        Perfil perfil = null;

        try {
            perfil = perfilServicio.findById(id);
            perfilServicio.modificarPerfil(foto, id, nickname, bio);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", perfil);

            return "registro";
        }

        return "inicio";
    }
}
