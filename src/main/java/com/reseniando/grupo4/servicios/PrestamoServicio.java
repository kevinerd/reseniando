package com.reseniando.grupo4.servicios;

import com.reseniando.grupo4.entidades.Libro;
import com.reseniando.grupo4.entidades.Prestamo;
import com.reseniando.grupo4.entidades.Usuario;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamorepositorio;

    @Transactional
    public void agregarPrestamo(String id, Date fechaPrestamo, Date fechaEstimativa, Date fechaDevolucion, Boolean devuelto, Libro libro, Usuario usuario) throws ErrorServicio {

        validar(id, fechaPrestamo, fechaEstimativa, fechaDevolucion, devuelto);

        Prestamo prestamo = new Prestamo();
        prestamo.setId(id);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaEstimativa(fechaEstimativa);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setDevuelto(devuelto);
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);

        prestamorepositorio.save(prestamo);

    }

    @Transactional
    public void eliminarPrestamo(String id) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamorepositorio.findById(id);

        if (respuesta.isPresent()) {
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

        validar(id, fechaPrestamo, fechaEstimativa, fechaDevolucion, devuelto);

        Optional<Prestamo> respuesta = prestamorepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            if (true) {  //Verificar que el usuario sea ADMIN

                prestamo.setId(id);
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

    public void validar(String id, Date fechaPrestamo, Date fechaEstimativa, Date fechaDevolucion, Boolean devuelto) throws ErrorServicio {

        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id en Prestamos no puede estar vacio o ser nulo (SERVICIO PRESTAMO VALIDAR v1).");

        }
        if (fechaPrestamo == null || fechaEstimativa == null || fechaDevolucion == null || fechaPrestamo.after(fechaEstimativa) || fechaPrestamo.after(fechaDevolucion)) {
            throw new ErrorServicio("La fecha del prestamo/estimativa/devolucion no puede ser nula o ser posterior a la fecha estimativa de entrega (SERVICIO PRESTAMO VALIDARv2).");
        }
        if (devuelto == null) {
            throw new ErrorServicio("El estado de devolucion no puede ser nulo (SERVICIO PRESTAMO - VALIDARv3) .");
        }

    }
}
