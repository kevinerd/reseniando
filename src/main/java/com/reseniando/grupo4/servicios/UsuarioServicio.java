package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.enumeraciones.Role;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public Usuario crearUsuario(String dni, String nombre, String apellido, String domicilio, String email, String pass1, String pass2) throws ErrorServicio {
        validar(dni, nombre, apellido, domicilio, email, pass1, pass2);
        
        Usuario usuario = new Usuario();
        usuario.setAlta(Boolean.TRUE);
        usuario.setDni(dni);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDomicilio(domicilio);
        usuario.setEmail(email);
        String encriptada = new BCryptPasswordEncoder().encode(pass1);
        usuario.setPass(encriptada);
        usuario.setRol(Role.USER);

        return usuarioRepositorio.save(usuario);
    }
    
    @Transactional
    public void modificarUsuario( String dni, String nombre, String apellido, String domicilio, String email, String pass1, String pass2 ) throws ErrorServicio {       
        Usuario usuario = usuarioRepositorio.findByDni( dni );
        if( usuario != null ) {
            if( !nombre.equals( usuario.getNombre() )) {
                if ( nombre.trim().isEmpty() ) {
                    throw new ErrorServicio("El nombre no puede estar vacio.");
                }
                usuario.setNombre(nombre);
            }
            if( !apellido.equals( usuario.getApellido() )) {
                if ( apellido.trim().isEmpty() ) {
                    throw new ErrorServicio("El apellido no puede estar vacio.");
                }
                usuario.setApellido(apellido);
            }
            if( !domicilio.equals( usuario.getDomicilio() )) {
                if ( domicilio.trim().isEmpty() ) {
                    throw new ErrorServicio("La dirección no puede estar vacia.");
                }
                usuario.setDomicilio(domicilio);
            }
            if( !email.equals( usuario.getEmail() )) {
                if ( email.trim().isEmpty() ) {
                    throw new ErrorServicio("El email no puede estar vacio.");
                }
                usuario.setEmail(email);
            }
            if ( !pass1.equals(pass2) ){
                throw new ErrorServicio("Las contraseñas tienen que ser iguales.");
            }
            if (pass1 == null || pass1.length() < 4 || pass1.trim().isEmpty()) {
                throw new ErrorServicio("La contraseña ingresada tiene menos de cuatro caracteres o se encuentra vacia.");
            }
            if (pass2 == null || pass2.length() < 4 || pass2.trim().isEmpty()) {
                throw new ErrorServicio("La contraseña ingresada tiene menos de cuatro caracteres o se encuentra vacia.");
            }
            
            String encriptada = new BCryptPasswordEncoder().encode(pass1);
            usuario.setPass(encriptada);
            
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio( "No se encontró el usuario solicitado." );
        }
    }
    
    public Usuario findById( String id ) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById( id );
        
        if( respuesta.isPresent() ) {
            Usuario usuario = respuesta.get();
            return usuario;
        } else {
            throw new ErrorServicio("No se encontró el perfil solicitado.");
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }
    
    public Usuario encontrarPorDni(String dni) throws ErrorServicio {
        Usuario usuario= null;
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(dni);
        
        if(respuesta.isPresent()){
            usuario = respuesta.get();
        }
        
        return usuario;
    }

    @Transactional
    public void borrarUsuario(Usuario usuario) {
        usuarioRepositorio.delete(usuario);
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(email);
        
        if( usuario != null ) {
            //Esto es lo que le da los permisos al usuario, a que modulos puede acceder
            List<GrantedAuthority> permisos = new ArrayList<>();
            
            permisos.add(new SimpleGrantedAuthority("ROLE_"+usuario.getRol()));
            
            //GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO"");
            //permisos.add(p1);
            
            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            
            User user = new User( usuario.getEmail(), usuario.getPass(), permisos);
            
            return user;
        } else {
            return null;
        }
    }
    
    private void validar(String dni, String nombre, String apellido, String domicilio, String email, String pass1, String pass2) throws ErrorServicio {
        if (dni == null || dni.isEmpty() || dni.length() < 6) {
            throw new ErrorServicio("El DNI ingresado no es posible o se encuentra vacio.");
        }
        Usuario respuesta = usuarioRepositorio.findByDni( dni );
        if( respuesta != null ) {
            throw new ErrorServicio( "Este documento ya está en uso." );
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorServicio("El apellido no puede estar vacio.");
        }
        if (domicilio == null || domicilio.trim().isEmpty()) {
            throw new ErrorServicio("El domicilio no puede estar vacio.");
        }
        if (email == null || email.contains("@") == false) {
            throw new ErrorServicio("El email ingresado no es correcto o se encuentra vacio.");
        }
        if (pass1 == null || pass1.length() < 4 || pass1.trim().isEmpty()) {
            throw new ErrorServicio("La contraseña ingresada tiene menos de cuatro caracteres o se encuentra vacia.");
        }
        if (pass2 == null || pass2.length() < 4 || pass2.trim().isEmpty()) {
            throw new ErrorServicio("La contraseña ingresada tiene menos de cuatro caracteres o se encuentra vacia.");
        }
        if ( !pass1.equals(pass2) ){
            throw new ErrorServicio("Las contraseñas tienen que ser iguales.");
        }
    }

}
