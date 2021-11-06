package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Perfil;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.LibroRepositorio;
import com.reseniando.grupo4.servicios.PerfilServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private PerfilServicio perfilServicio;
    
    @Autowired
    private LibroRepositorio libroRepositorio;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> fotoPerfil(@PathVariable String id) {
        try {
            Perfil perfil = perfilServicio.findById(id);
            if (perfil.getFoto() == null) {
                throw new ErrorServicio("El perfil no tiene una foto asignada.");
            }
            byte[] foto = perfil.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/libro/{isbn}")
    public ResponseEntity<byte[]> portadaLibro(@PathVariable Long isbn) {
        try {
            Libro libro = libroRepositorio.buscarPorIsbn(isbn);
            if (libro.getPortada() == null) {
                throw new ErrorServicio("El libro no tiene una portada asignada.");
            }
            byte[] foto = libro.getPortada().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}