package com.organizador.demo.usuario.exception;

public class RoletaNaoIniciadaException extends RuntimeException {

    public RoletaNaoIniciadaException() {
        super("Informe o nome em /roleta/entrar antes de avançar a roleta");
    }
}
