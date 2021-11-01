package com.reseniando.grupo4.repositorios;

import com.reseniando.grupo4.entidades.Resenia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReseniaRepositorio extends JpaRepository<Resenia, String> {

    /*@Query("SELECT r FROM perfil_resenia r WHERE r.perfil_id = :id")
    List<Resenia> buscarTodosPorIdPerfil(@Param("id") String id);*/
}