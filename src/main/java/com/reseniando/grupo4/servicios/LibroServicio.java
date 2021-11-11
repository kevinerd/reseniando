package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Foto;
import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.enumeraciones.Generos;
import com.reseniando.grupo4.repositorios.LibroRepositorio;
import com.reseniando.grupo4.errores.ErrorServicio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public Libro agregarLibro(MultipartFile archivo, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Boolean alta, String autor, String editorial, String sinopsis, Generos genero, Boolean destacado) throws ErrorServicio {
        validar(titulo, autor, editorial, ejemplares);

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setAlta(Boolean.TRUE);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setSinopsis(sinopsis);
        libro.setGenero(genero);
        libro.setDestacado(destacado);

        Foto foto = fotoServicio.guardar(archivo);
        libro.setPortada(foto);

        libroRepositorio.save(libro);
        return libro;
    }

    @Transactional
    public void modificarLibro(
            MultipartFile archivo, 
            Long isbn, 
            String titulo,
            String autor, 
            String editorial,
            Integer anio, 
            Generos genero,
            String sinopsis,
            Integer ejemplares, 
            Integer ejemplaresPrestados,
            Boolean alta,
            Boolean destacado
    ) throws ErrorServicio {
        validar(titulo, autor, editorial, ejemplares);
        Libro libro = libroRepositorio.buscarPorIsbn(isbn);

        if ( libro != null ) {
            if (true) {  //Verificar que el usuario sea ADMIN
                libro.setIsbn(isbn);
                libro.setTitulo(titulo);
                libro.setAnio(anio);
                libro.setEjemplares(ejemplares);
                libro.setEjemplaresPrestados(ejemplaresPrestados);
                libro.setAlta(alta);
                libro.setAutor(autor);
                libro.setEditorial(editorial);
                libro.setDestacado(destacado);
                
                if( libro.getPortada() != null ) {
                    String idFoto = libro.getPortada().getId();
                    Foto foto = fotoServicio.actualizar( idFoto, archivo );
                    libro.setPortada(foto);
                    libroRepositorio.save( libro );
                } else {
                    Foto foto = fotoServicio.guardar(archivo);
                    libro.setPortada(libro.getPortada());
                    libroRepositorio.save( libro );
                }
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
    
    public List<Libro> listarTodo(){
        return libroRepositorio.findAll();
    }
    
    public List<Libro> listarDestacados() {
        List<Libro> librosDest = libroRepositorio.buscarDestacados(Boolean.TRUE);
        return librosDest;
    }
    
    public List<Libro> listarPorGenero( Generos genero ) {
        List<Libro> libros = libroRepositorio.buscarPorGenero(genero);
        return libros;
    }
}
