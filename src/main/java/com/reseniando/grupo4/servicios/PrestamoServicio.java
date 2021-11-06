package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Prestamo;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.PrestamoRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    
    @Transactional
    public Prestamo registrarPrestamo( Libro libro, Usuario usuario ) throws ErrorServicio {
        
        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo( LocalDate.now() );
        prestamo.setFechaEstimativa( LocalDate.now().plusDays(7) );
        
        prestamoRepositorio.save(prestamo);
        
        return prestamo;
    }
    
    @Transactional
    public void modificarPrestamo( String id ) throws ErrorServicio {
        
        Optional<Prestamo> respuesta = prestamoRepositorio.findById( id );
        if( respuesta.isPresent() ) {
            Prestamo prestamo = respuesta.get();
            
            if( !prestamo.getDevuelto().equals(Boolean.TRUE)  ) {
                prestamo.setDevuelto(Boolean.TRUE);
                prestamo.setFechaDevolucion(LocalDate.now());
            }
            prestamoRepositorio.save( prestamo );
        } else {
            throw new ErrorServicio( "No se encontró el prestamo solicitado." );
        }
    }

    @Transactional
    public void modificarPrestamo(String id, LocalDate fechaDevolucion, Boolean devuelto) throws ErrorServicio {

       // validar(fechaPrestamo, fechaEstimativa, fechaDevolucion, devuelto, libro, usuario);

        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            if (true) {  //Verificar que el usuario sea ADMIN
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.setDevuelto(devuelto);
            } else {
                throw new ErrorServicio("No tienes los permisos para realizar esta operación (SERVICIO PRESTAMO - MODIFICARv1).");
            }
        } else {
            throw new ErrorServicio("No se encontro el prestamo (PRESTAMO SERVICIO - MODIFICARv2))");
        }
    }
    
    @Transactional
    public void eliminarPrestamo(String id) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);

        if (respuesta.isPresent() == true) {
            if (true) {  //Verificar que el usuario sea ADMIN
                prestamoRepositorio.deleteById(id);
            } else {
                throw new ErrorServicio("No tienes los permisos para realizar esta operación (PRESTAMO SERVICIOv1).");
            }
        } else {
            throw new ErrorServicio("No existe el prestamo solicitado (PRESTAMO SERVICIOv2).");
        }
    }

//    public void validar(LocalDate fechaPrestamo, LocalDate fechaEstimativa, LocalDate fechaDevolucion, Boolean devuelto, Libro libro, Usuario usuario) throws ErrorServicio {
//
//        if (fechaPrestamo == null || fechaEstimativa == null || fechaDevolucion == null || fechaPrestamo.after(fechaEstimativa) || fechaPrestamo.after(fechaDevolucion)) {
//            throw new ErrorServicio("La fecha del prestamo/estimativa/devolucion no puede ser nula o ser posterior a la fecha estimativa de entrega (SERVICIO PRESTAMO VALIDARv2).");
//        }
//        if (devuelto == null) {
//            throw new ErrorServicio("El estado de devolucion no puede ser nulo (SERVICIO PRESTAMO - VALIDARv3) .");
//        }
//        if (libro == null) {
//            throw new ErrorServicio("El libro prestado no puede ser nulo (SERVICIO PRESTAMO - VALIDARv4).");
//        }
//        if (usuario == null) {
//            throw new ErrorServicio("El usuario alquilante no puede ser nulo (SERVICIO PRESTAMO - VALIDARv5).");
//        }
//
//    }
    
    public Prestamo findById( String id ) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamoRepositorio.findById( id );
        
        if( respuesta.isPresent() ) {
            Prestamo prestamo = respuesta.get();
            return prestamo;
        } else {
            throw new ErrorServicio("No se encontró el prestamo solicitado.");
        }
    }
    
    public List<Prestamo> listarTodo (){
        return prestamoRepositorio.findAll();
    }
    
    public List<Prestamo> listarPorUsuario( Usuario usuario ){
        return prestamoRepositorio.buscarPrestamoPorUsuario( usuario );
    }
}
