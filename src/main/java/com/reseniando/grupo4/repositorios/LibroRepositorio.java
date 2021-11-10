package com.reseniando.grupo4.repositorios;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.enumeraciones.Generos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT l FROM Libro l WHERE l.id = :id")
    List<Libro> buscarLibroPorId(@Param("id") Long id);
    
    @Query( "SELECT l FROM Libro l WHERE l.isbn = :isbn" )
    public Libro buscarPorIsbn(@Param("isbn") Long isbn);
    
    @Query("SELECT l FROM Libro l WHERE l.destacado = :destacado")
    List<Libro> buscarDestacados(@Param("destacado") Boolean destacado);
    
    @Query("SELECT l FROM Libro l WHERE l.genero = :genero")
    List<Libro> buscarPorGenero(@Param("genero") Generos genero);
}
