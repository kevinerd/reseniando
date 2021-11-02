package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Usuario;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio ur;

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

        return ur.save(usuario);
    }
    
    @Transactional
    public void modificarUsuario( String dni, String nombre, String apellido, String domicilio, String email, String pass1, String pass2 ) throws ErrorServicio {
        validar(dni, nombre, apellido, domicilio, email, pass1, pass2);
        
        Usuario usuario = ur.findByDni( dni );
        if( usuario != null ) {
            usuario.setAlta(Boolean.TRUE);
            usuario.setDni(dni);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDomicilio(domicilio);
            usuario.setEmail(email);
            String encriptada = new BCryptPasswordEncoder().encode(pass1);
            usuario.setPass(encriptada);
            
            ur.save(usuario);
        } else {
            throw new ErrorServicio( "No se encontró el usuario solicitado." );
        }
    }

    public List<Usuario> listarUsuarios() {
        return ur.findAll();
    }
    
    public Usuario encontrarPorDni(String dni) throws ErrorServicio {
        Usuario usuario= null;
        
        Optional<Usuario> op = ur.findById(dni);
        
        if(op.isPresent()){
            usuario = op.get();
        }
        
        return usuario;
    }

    @Transactional
    public void borrarUsuario(Usuario usuario) {
        ur.delete(usuario);
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = ur.buscarPorMail(email);
        
        if( usuario != null ) {
            //Esto es lo que le da los permisos al usuario, a que modulos puede acceder
            List<GrantedAuthority> permisos = new ArrayList<>();
            
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);
            
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
    }

}
