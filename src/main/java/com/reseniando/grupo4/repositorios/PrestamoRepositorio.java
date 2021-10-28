/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reseniando.grupo4.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reseniando.grupo4.entidades.Prestamo;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{
    
}
