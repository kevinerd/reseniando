package com.reseniando.grupo4.enumeraciones;

public enum Generos {
    TERROR("Terror"), COMEDIA("Comedia"), CIENCIA_FICCION("Ciencia Ficción"), ROMANCE("Romance"), 
    INFANTIL("Infantil"), MISTERIO("Misterio"), POLICIAL("Policial"), AUTOAYUDA("Autoayuda"), 
    FANTASÍA("Fantasía"), ADOLESCENTES("Adolescentes"), FILOSOFÍA("Filosofía"), NOVELA("Novela"),
    FICCION("Ficción");

    private final String gen;

    private Generos(String gen) {
        this.gen = gen;
    }

    public String getGen() {
        return gen;
    }
}
