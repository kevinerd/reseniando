package com.reseniando.grupo4.repositorios;

import com.reseniando.grupo4.entidades.Foto;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FotoRepositorio extends JpaRepository <Foto, String>{
       
}
