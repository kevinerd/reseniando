package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.servicios.ReseniaServicio;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/resenias")
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
public class ReseniaController {

    @Autowired
    
    private ReseniaServicio reseniaServicio;
    
    @GetMapping("crear-resenia")
    public String crearResenia(String comentarioResenia,
                                String tituloResenia,
                                ModelMap model){
        
     
        
        model.addAttribute("tituloResenia", tituloResenia);
        model.addAttribute("comentarioResenia", comentarioResenia);
  
        return "registrar-resenia";
    }
    @PostMapping("/registrar-resenia")
    public String registrarResenia(
            ModelMap model,
            @RequestParam String tituloResenia,
            @RequestParam String comentarioResenia)
    {
        try {
            LocalDate fechaResenia = LocalDate.now();
            reseniaServicio.crearResenia(tituloResenia, comentarioResenia, fechaResenia);
            
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "registrar-resenia";
        }
        
        return "exito";

    }
    
    
}
