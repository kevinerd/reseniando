package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Resenia;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.ReseniaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReseniaServicio {

    @Autowired
    private ReseniaRepositorio rr;

    @Transactional
    public Resenia crearResenia(Resenia resenia) throws ErrorServicio {
        if (resenia.getComentario() == null || resenia.getComentario().trim().isEmpty() == true || resenia.getComentario().length() < 4) {
            throw new ErrorServicio("El comentario de la reseña no puede ser nulo o estar vacio.");
        }
        if (resenia.getTitulo() == null || resenia.getTitulo().trim().isEmpty() == true) {
            throw new ErrorServicio("El título de la reseña no puede ser nulo o estar vacio.");
        }
        if (resenia.getFecha() == null) {
            throw new ErrorServicio("La fecha de la reseña no puede ser nula.");
        }
        return rr.save(resenia);
    }

    @Transactional
    public Resenia crearResenia(String titulo, String comentario, Date fecha) {
        Resenia resenia = new Resenia();
        resenia.setTitulo(titulo);
        resenia.setComentario(comentario);
        resenia.setFecha(fecha);

        return rr.save(resenia);
    }

    public List<Resenia> mostrarLasReseñas() {
        return rr.findAll();
    }

    public List<Resenia> buscarReseniasPorNickname(String id) {
        return rr.buscarTodosPorIdPerfil(id);
    }

    public Resenia encontrarPorId(String id) {
        Resenia resenia = null;
        Optional<Resenia> op = rr.findById(id);
        if (op.isPresent()) {
            resenia = op.get();
        }
        return resenia;
    }

    @Transactional
    public void borrarResenia(Resenia resenia) {
        rr.delete(resenia);
    }                                                          
}
