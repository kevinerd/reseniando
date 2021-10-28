package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.UsuarioRepositorio;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

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
    public Usuario saveUsuario(String dni, String nombre, String apellido, String domicilio, String email, String pass) throws ErrorServicio {
        Usuario usuario = new Usuario();
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

    @Transactional
    public void borrarUsuario(Usuario usuario) {
        ur.delete(usuario);
    }

}