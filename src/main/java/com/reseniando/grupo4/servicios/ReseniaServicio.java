package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Perfil;
import com.reseniando.grupo4.entidades.Resenia;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.LibroRepositorio;
import com.reseniando.grupo4.repositorios.PerfilRepositorio;
import com.reseniando.grupo4.repositorios.ReseniaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReseniaServicio {

    @Autowired
    private ReseniaRepositorio reseniaRepositorio;
    
    @Autowired
    private PerfilRepositorio perfilRepositorio;
    
    @Autowired
    private LibroRepositorio libroRepositorio;

    @Transactional
    public Resenia crearResenia( Resenia resenia, Perfil perfil, Libro libro ) throws ErrorServicio {
        validar( resenia );
        
        List<Resenia> reseniasPerfil = new ArrayList<>(); // Creo una lista local.
        reseniasPerfil = perfil.getResenias(); // Guardo en la lista las resenias que el perfil ya tenga.
        
        List<Resenia> reseniasLibro = new ArrayList<>(); // Creo una lista local.
        reseniasLibro = libro.getResenias(); // Guardo en la lista las resenias que el libro ya tenga.
        
        reseniaRepositorio.save(resenia); // Guardo la resenia en la DB.
        
        reseniasPerfil.add(resenia); // Añado la resenia a la lista del perfil.
        perfil.setResenias(reseniasPerfil); // Seteo la lista actualizada al perfil.
        perfilRepositorio.save(perfil); // Guardo el perfil.
        
        reseniasLibro.add(resenia); // Añado la resenia a la lista del libro.
        libro.setResenias(reseniasLibro); // Seteo la lista actualizada al libro.
        libroRepositorio.save(libro); // Guardo el libro.
        
        return resenia;
    }
    
    @Transactional
    public void modificarResenia( Resenia reseniaEdit ) throws ErrorServicio {
        
        validar(reseniaEdit);

        Optional<Resenia> respuesta = reseniaRepositorio.findById(reseniaEdit.getId());
        
        if( respuesta.isPresent() ) {
            Resenia resenia = respuesta.get();    
                
            if( !reseniaEdit.getTitulo().equals(resenia.getTitulo()) ) {
                resenia.setTitulo(reseniaEdit.getTitulo());
            }
            if (!reseniaEdit.getComentario().equals(resenia.getComentario())) {
                resenia.setComentario(reseniaEdit.getComentario());
            }
            
        } else {
            throw new ErrorServicio( "No se encontró la resenia solicitada." );
        }
    }
    
    public Resenia findById(String id) {
        Resenia resenia = null;
        Optional<Resenia> respuesta = reseniaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            resenia = respuesta.get();
        }
        return resenia;
    }

    @Transactional
    public void borrarResenia(Resenia resenia) {
        reseniaRepositorio.delete(resenia);
    }
    
    public List<Resenia> listarTodo (){
        return reseniaRepositorio.findAll();
    }
    
    private void validar( Resenia resenia ) throws ErrorServicio {
        if (resenia.getComentario() == null || resenia.getComentario().trim().isEmpty() == true || resenia.getComentario().length() < 4) {
            throw new ErrorServicio("El comentario de la reseña no puede ser nulo o estar vacio.");
        }
        if (resenia.getTitulo() == null || resenia.getTitulo().trim().isEmpty() == true) {
            throw new ErrorServicio("El título de la reseña no puede ser nulo o estar vacio.");
        }
        if (resenia.getFecha() == null) {
            throw new ErrorServicio("La fecha de la reseña no puede ser nula.");
        }
    }
}
