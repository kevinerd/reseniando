package com.reseniando.grupo4.enumeraciones;

public enum Generos {
    TERROR("Terror"), COMEDIA("Comedia"), CIENCIA_FICCION("Ciencia_Ficción"), ROMANCE("Romance"), INFANTIL("Infantil"), MISTERIO("Misterio"), POLICIAL("Policial"), AUTOAYUDA("Autoayuda"), FANTASÍA("Fantasía"), ADOLESCENTES("Adolescentes"), FILOSOFÍA("Filosofía");

    private final String gen;

    private Generos(String gen) {
        this.gen = gen;
    }

    public String getGen() {
        return gen;
    }
}
