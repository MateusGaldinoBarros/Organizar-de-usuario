package com.organizador.demo.usuario.exception;

public class MaturacaoNaoIniciadaException extends RuntimeException {

    public MaturacaoNaoIniciadaException() {
        super("Informe o nome em /maturacao/entrar antes de avançar a maturação");
    }
}
