package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Foto;
import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.repositorios.LibroRepositorio;
import com.reseniando.grupo4.errores.ErrorServicio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public void agregarLibro(MultipartFile archivo, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Boolean alta, String autor, String editorial) throws ErrorServicio {
        //Usuario usuario = usuarioRepositorio.findById(idUsuario).get();

        validar(titulo, autor, editorial, ejemplares);

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setAlta(true);
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        Foto foto = fotoServicio.guardar(archivo);
        libro.setPortada(foto);

        libroRepositorio.save(libro);
    }

    @Transactional
    public void modificarLibro(MultipartFile archivo, String libroId, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Boolean alta, String autor, String editorial) throws ErrorServicio {
        validar(titulo, autor, editorial, ejemplares);
        Optional<Libro> respuesta = libroRepositorio.findById(libroId);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (true) {  //Verificar que el usuario sea ADMIN
                libro.setIsbn(isbn);
                libro.setTitulo(titulo);
                libro.setAnio(anio);
                libro.setEjemplares(ejemplares);
                libro.setEjemplaresPrestados(ejemplaresPrestados);
                libro.setAlta(true);
                libro.setAutor(autor);
                libro.setEditorial(editorial);

                String idFoto = null;
                if (libro.getPortada() != null) {
                    idFoto = libro.getPortada().getId();
                }

                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                libro.setPortada(foto);

                libroRepositorio.save(libro);
            } else {
                throw new ErrorServicio("No tienes los permisos para realizar esta operación.");
            }
        } else {
            throw new ErrorServicio("No existe el libro solicitado.");
        }
    }

    @Transactional
    public void eliminarLibro(String libroId) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(libroId);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (true) {  //Verificar que el usuario sea ADMIN
                libro.setAlta(Boolean.FALSE);
            } else {
                throw new ErrorServicio("No tienes los permisos para realizar esta operación.");
            }
        } else {
            throw new ErrorServicio("No existe el libro solicitado.");
        }
    }

    public void validar(String titulo, String autor, String editorial, Integer ejemplares) throws ErrorServicio {
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El título no puede estar vacío.");
        }
        if (autor == null || autor.isEmpty()) {
            throw new ErrorServicio("El libro tiene que tener un autor.");
        }
        if (editorial == null || editorial.isEmpty()) {
            throw new ErrorServicio("El libro tiene que tener una editorial.");
        }
        if (ejemplares == null || ejemplares <= 0) {
            throw new ErrorServicio("Tiene que haber, al menos, un ejemplar.");
        }
    }
}
