/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reseniando.grupo4.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reseniando.grupo4.entidades.Prestamo;
import com.reseniando.grupo4.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{
    
     @Query("SELECT p FROM Prestamo p WHERE p.id = :id")
     List<Prestamo> buscarPrestamoPorId(@Param("id") String id); 
     
     @Query("SELECT p FROM Prestamo p WHERE p.usuario = :usuario")
     List<Prestamo> buscarPrestamoPorUsuario(@Param("usuario") Usuario usuario); 
}
