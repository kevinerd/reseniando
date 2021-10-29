package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Foto;
import com.reseniando.grupo4.entidades.Perfil;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.PerfilRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PerfilServicio {
    @Autowired
    private PerfilRepositorio perfilRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    @Transactional
    public void registrar( MultipartFile archivo, String nickname, String bio ) throws ErrorServicio {
        validar( nickname, bio );
        
        Perfil perfil = new Perfil();
        perfil.setBio(bio);
        perfil.setNickname(nickname);
        
        Foto foto = fotoServicio.guardar(archivo);
        perfil.setFoto(foto);
        
        perfilRepositorio.save(perfil);
    }
    
    @Transactional
    public void modificarPerfil( MultipartFile archivo, String id, String nickname, String bio ) throws ErrorServicio {
        validar(nickname, bio);
        
        Optional<Perfil> respuesta = perfilRepositorio.findById( id );
        if( respuesta.isPresent() ) {
            Perfil perfil = respuesta.get();
            
            perfil.setNickname(nickname);
            perfil.setBio(bio);
            
            String idFoto = null;
            if( perfil.getFoto() != null ) {
                idFoto = perfil.getFoto().getId();
            }
            
            Foto foto = fotoServicio.actualizar( idFoto, archivo );
            perfil.setFoto( foto );
            
            perfilRepositorio.save( perfil );
        } else {
            throw new ErrorServicio( "No se encontró el perfil solicitado." );
        }
    }
    
    public Perfil findById( String id ) throws ErrorServicio {
        Optional<Perfil> respuesta = perfilRepositorio.findById( id );
        
        if( respuesta.isPresent() ) {
            Perfil perfil = respuesta.get();
            return perfil;
        } else {
            throw new ErrorServicio("No se encontró el perfil solicitado.");
        }
    }
    
    private void validar( String nickname, String bio ) throws ErrorServicio {
        if( nickname == null || nickname.isEmpty() ) {
            throw new ErrorServicio( "Debe ingresar un nickname." );
        }
        
        Perfil respuesta = perfilRepositorio.findByNickname( nickname );
        if( respuesta != null ) {
            throw new ErrorServicio( "Este nickname ya está en uso. Pruebe con otro." );
        }
        
        if( bio == null || bio.isEmpty() ) {
            throw new ErrorServicio( "Debe completar la biografía." );
        }
    }
}