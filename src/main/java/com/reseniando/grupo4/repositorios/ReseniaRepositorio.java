package com.reseniando.grupo4.repositorios;

import com.reseniando.grupo4.entidades.Resenia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReseniaRepositorio extends JpaRepository<Resenia, String> {

    @Query("SELECT r FROM Resenia r WHERE r.id = :id")
    public List<Resenia> buscarReseniaPorId(@Param("id") String id); 
}