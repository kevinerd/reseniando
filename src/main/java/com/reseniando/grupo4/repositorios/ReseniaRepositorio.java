package com.reseniando.grupo4.repositorios;

import com.reseniando.grupo4.entidades.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReseniaRepositorio extends JpaRepository<Resenia, String> {

    /* -- CON EL MISMO ID DE LA RESENIA, BUSCO EN LA TABLA PERFIL_RESENIAS Y OBTENGO EL ID DEL PERFIL -- */
    @Query(value = "SELECT pr.perfil_id "
            + "FROM perfil_resenias pr "
            + "WHERE pr.resenias_id = :id", nativeQuery = true )
            @Transactional(readOnly = true)
    String buscarPerfilResenia( @Param("id") String id );
    
    /* -- CON EL MISMO ID DE LA RESENIA, BUSCO EN LA TABLA LIBRO_RESENIAS Y OBTENGO EL ID DEL LIBRO -- */
    @Query(value = "SELECT lr.libro_isbn "
            + "FROM libro_resenias lr "
            + "WHERE lr.resenias_id = :id", nativeQuery = true )
    Long buscarLibroResenia( @Param("id") String id );
}