package com.reseniando.grupo4.repositorios;

import com.reseniando.grupo4.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    
    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE :query OR u.apellido LIKE :query")
    public List<Usuario> findAllByQuery(@Param("query") String query);

    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    public Usuario findByDni(@Param("dni") String dni);
    
    @Query("SELECT c FROM Usuario c WHERE c.email = :email")
    public Usuario buscarPorMail( @Param("email") String email );
}
