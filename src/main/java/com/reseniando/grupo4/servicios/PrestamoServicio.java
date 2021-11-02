package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Prestamo;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamorepositorio;

    @Transactional
    public void agregarPrestamo(Date fechaPrestamo, Date fechaEstimativa, Date fechaDevolucion, Boolean devuelto, Libro libro, Usuario usuario) throws ErrorServicio {

//        validar(fechaPrestamo, fechaEstimativa, fechaDevolucion, devuelto, libro, usuario);

        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaEstimativa(fechaEstimativa);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setDevuelto(devuelto);
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);

        prestamorepositorio.save(prestamo);

    }
    
    @Transactional
    public void agregarPrestamo(Prestamo prestamo){
        prestamorepositorio.save(prestamo);
    }

    @Transactional
    public void eliminarPrestamo(String id) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamorepositorio.findById(id);

        if (respuesta.isPresent() == true) {
            if (true) {  //Verificar que el usuario sea ADMIN
                prestamorepositorio.deleteById(id);
            } else {
                throw new ErrorServicio("No tienes los permisos para realizar esta operación (PRESTAMO SERVICIOv1).");
            }
        } else {
            throw new ErrorServicio("No existe el prestamo solicitado (PRESTAMO SERVICIOv2).");
        }
    }

    @Transactional
    public void modificarPrestamo(String id, Date fechaPrestamo, Date fechaEstimativa, Date fechaDevolucion, Boolean devuelto, Libro libro, Usuario usuario) throws ErrorServicio {

        validar(fechaPrestamo, fechaEstimativa, fechaDevolucion, devuelto, libro, usuario);

        Optional<Prestamo> respuesta = prestamorepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            if (true) {  //Verificar que el usuario sea ADMIN

                prestamo.setFechaPrestamo(fechaPrestamo);
                prestamo.setFechaEstimativa(fechaEstimativa);
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.setDevuelto(devuelto);
                prestamo.setLibro(libro);
                prestamo.setUsuario(usuario);
            } else {
                throw new ErrorServicio("No tienes los permisos para realizar esta operación (SERVICIO PRESTAMO - MODIFICARv1).");
            }
        } else {
            throw new ErrorServicio("No se encontro el prestamo (PRESTAMO SERVICIO - MODIFICARv2))");
        }
    }

    public void validar(Date fechaPrestamo, Date fechaEstimativa, Date fechaDevolucion, Boolean devuelto, Libro libro, Usuario usuario) throws ErrorServicio {

        if (fechaPrestamo == null || fechaEstimativa == null || fechaDevolucion == null || fechaPrestamo.after(fechaEstimativa) || fechaPrestamo.after(fechaDevolucion)) {
            throw new ErrorServicio("La fecha del prestamo/estimativa/devolucion no puede ser nula o ser posterior a la fecha estimativa de entrega (SERVICIO PRESTAMO VALIDARv2).");
        }
        if (devuelto == null) {
            throw new ErrorServicio("El estado de devolucion no puede ser nulo (SERVICIO PRESTAMO - VALIDARv3) .");
        }
        if (libro == null) {
            throw new ErrorServicio("El libro prestado no puede ser nulo (SERVICIO PRESTAMO - VALIDARv4).");
        }
        if (usuario == null) {
            throw new ErrorServicio("El usuario alquilante no puede ser nulo (SERVICIO PRESTAMO - VALIDARv5).");
        }

    }
    
    public Optional <Prestamo> buscarPorid(String id){
       
        
        return  prestamorepositorio.findById(id);  
    }
    
    public List<Prestamo> listarTodo (){
        return prestamorepositorio.findAll();
    }
}
