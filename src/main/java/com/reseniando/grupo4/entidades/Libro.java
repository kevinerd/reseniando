package com.reseniando.grupo4.entidades;

import com.reseniando.grupo4.enumeraciones.Generos;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Libro {

    @Id
    private Long isbn;
    private String autor;
    private String editorial;
    private String titulo;
    private Integer anio;
    private String sinopsis;
    @Enumerated(EnumType.STRING)
    private Generos genero;
    @OneToOne
    private Foto portada;
    private Integer ejemplares;
    private Integer ejemplaresPrestados;
    private Boolean alta;

    public Libro() {
    }

    public Libro(Long isbn, String autor, String editorial, String titulo, Integer anio, String sinopsis, Generos genero, Foto portada, Integer ejemplares, Integer ejemplaresPrestados, Boolean alta) {
        this.isbn = isbn;
        this.autor = autor;
        this.editorial = editorial;
        this.titulo = titulo;
        this.anio = anio;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.portada = portada;
        this.ejemplares = ejemplares;
        this.ejemplaresPrestados = ejemplaresPrestados;
        this.alta = alta;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Generos getGenero() {
        return genero;
    }

    public void setGenero(Generos genero) {
        this.genero = genero;
    }

    public Foto getPortada() {
        return portada;
    }

    public void setPortada(Foto portada) {
        this.portada = portada;
    }

    public Integer getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(Integer ejemplares) {
        this.ejemplares = ejemplares;
    }

    public Integer getEjemplaresPrestados() {
        return ejemplaresPrestados;
    }

    public void setEjemplaresPrestados(Integer ejemplaresPrestados) {
        this.ejemplaresPrestados = ejemplaresPrestados;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }
}
