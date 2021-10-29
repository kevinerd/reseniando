package com.reseniando.grupo4.repositorios;

import com.reseniando.grupo4.entidades.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepositorio extends JpaRepository<Perfil, String> {
    @Query( "SELECT p FROM Perfil p WHERE p.id = :id" )
    public Perfil buscarPorId(@Param("id") String id);
    
    @Query( "SELECT p FROM Perfil p WHERE p.nickname = :nickname" )
    public Perfil findByNickname( @Param("nickname") String nickname );
}
