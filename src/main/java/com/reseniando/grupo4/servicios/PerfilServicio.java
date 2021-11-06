package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Foto;
import com.reseniando.grupo4.entidades.Perfil;
import com.reseniando.grupo4.entidades.Resenia;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.PerfilRepositorio;
import java.util.ArrayList;
import java.util.List;
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
    public Perfil registrar( MultipartFile archivo, String nickname, String bio ) throws ErrorServicio {
        validar( nickname, bio );
        
        Perfil perfil = new Perfil();
        perfil.setBio(bio);
        perfil.setNickname(nickname);
        
        Foto foto = fotoServicio.guardar(archivo);
        perfil.setFoto(foto);
        
        perfilRepositorio.save(perfil);
        
        return perfil;
    }
    
    @Transactional
    public void modificarPerfil( MultipartFile archivo, String id, String nickname, String bio ) throws ErrorServicio {
        Optional<Perfil> respuesta = perfilRepositorio.findById( id );
        if( respuesta.isPresent() ) {
            Perfil perfil = respuesta.get();
            
            if( !nickname.equals(perfil.getNickname()) ) {
                validar(nickname, bio);
                perfil.setNickname(nickname);
                if( !bio.equals(perfil.getBio()) ) {
                    perfil.setBio(bio);
                }
            }
            
            if( perfil.getFoto() != null ) {
                String idFoto = perfil.getFoto().getId();
                Foto foto = fotoServicio.actualizar( idFoto, archivo );
                perfil.setFoto(foto);
                perfilRepositorio.save( perfil );
            } else {
                Foto foto = fotoServicio.guardar(archivo);
                perfil.setFoto(foto);
                perfilRepositorio.save( perfil );
            }
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
    
    public List<Resenia> listarResenias( Perfil perfil ){
        List<Resenia> reseniasPerfil = new ArrayList<>();
        reseniasPerfil = perfil.getResenias();
        return reseniasPerfil;
    }
}