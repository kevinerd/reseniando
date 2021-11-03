package com.reseniando.grupo4.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Perfil {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nickname;
    private String bio;
    @OneToOne
    private Foto foto;
    @OneToMany
    private List<Resenia> resenias;
    /*@OneToOne
    private Favorito favoritos;*/
    /*@OneToOne
    private Leido leidos;*/
    @OneToMany
    private List<Libro> leidos;
    @OneToMany
    private List<Libro> favoritos;

    public Perfil() {
    }

    public Perfil(String nickname, String bio, Foto foto) {
        this.nickname = nickname;
        this.bio = bio;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public List<Resenia> getResenias() {
        return resenias;
    }

    public void setResenias(List<Resenia> resenias) {
        this.resenias = resenias;
    }

    /*public Favorito getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Favorito favoritos) {
        this.favoritos = favoritos;
    }*/

    /*public Leido getLeidos() {
        return leidos;
    }

    public void setLeidos(Leido leidos) {
        this.leidos = leidos;
    }*/

    public List<Libro> getLeidos() {
        return leidos;
    }

    public void setLeidos(List<Libro> leidos) {
        this.leidos = leidos;
    }

    public List<Libro> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Libro> favoritos) {
        this.favoritos = favoritos;
    }
}
