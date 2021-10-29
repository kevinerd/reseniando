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
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio ur;

    @Transactional
    public Usuario crearUsuario(Usuario usuario) throws ErrorServicio {
        if (usuario.getDni() == null || usuario.getDni().isEmpty() || usuario.getDni().length() < 6) {
            throw new ErrorServicio("El DNI ingresado no es posible o se encuentra vacio.");
        }
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio.");
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new ErrorServicio("El apellido no puede estar vacio.");
        }
        if (usuario.getDomicilio() == null || usuario.getDomicilio().trim().isEmpty()) {
            throw new ErrorServicio("El domicilio no puede estar vacio.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().contains("@") == false) {
            throw new ErrorServicio("El email ingresado no es correcto o se encuentra vacio.");
        }
        if (usuario.getPass() == null || usuario.getPass().length() < 4 || usuario.getPass().trim().isEmpty()) {
            throw new ErrorServicio("La contraseña ingresada tiene menos de cuatro caracteres o se encuentra vacia.");
        }
        return ur.save(usuario);
    }

    @Transactional
    public Usuario crearUsuario(String dni, String nombre, String apellido, String domicilio, String email, String pass) throws ErrorServicio {
        Usuario usuario = new Usuario();
        usuario.setAlta(Boolean.TRUE);
        usuario.setDni(dni);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDomicilio(domicilio);
        usuario.setEmail(email);
        usuario.setPass(pass);

        return ur.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return ur.findAll();
    }
    
    public Usuario findByDni(String dni) throws ErrorServicio {
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
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        Usuario usuario = ur.findByDni(dni);
        
        if( usuario != null ) {
            //Esto es lo que le da los permisos al usuario, a que modulos puede acceder
            List<GrantedAuthority> permisos = new ArrayList<>();
            
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);
            
            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_FOTOS");
            permisos.add(p2);
            
            GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_MASCOTAS");
            permisos.add(p3);
            
            GrantedAuthority p4 = new SimpleGrantedAuthority("MODULO_VOTOS");
            permisos.add(p4);
            
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

}