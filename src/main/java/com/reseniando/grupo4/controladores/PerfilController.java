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
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
public class PerfilController {

    @Autowired
    private PerfilServicio perfilServicio;

    @Autowired
    private UsuarioRepositorio ur;

    @GetMapping("/crear-perfil")
    public String crearPerfil() {
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
    public String editarPerfil(@RequestParam(required = false) String id, ModelMap model) {
        if (id != null) {
            try {
                Perfil perfil = perfilServicio.findById(id);
                model.addAttribute("perfil", perfil);
            } catch (ErrorServicio e) {
                model.addAttribute("error", e.getMessage());
            }
            return "perfil";
        }else
            return "redirect:/"; //Para borrar
    }

    @PostMapping("/actualizar-perfil")
    public String actualizar(
            ModelMap modelo,
            HttpSession session,
            MultipartFile foto,
            @RequestParam String id,
            @RequestParam String nickname,
            @RequestParam String bio
    ) {
        Perfil perfil = null;

        try {
            perfil = perfilServicio.findById(id);
            perfilServicio.modificarPerfil(foto, id, nickname, bio);
            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", perfil);

            return "perfil";
        }
    }
}
