package com.organizador.demo.usuario.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> usuarioNaoEncontrado(UsuarioNaoEncontradoException exception) {
        return Map.of("erro", exception.getMessage());
    }

    @ExceptionHandler(MaturacaoNaoIniciadaException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> maturacaoNaoIniciada(MaturacaoNaoIniciadaException exception) {
        return Map.of("erro", exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> requisicaoInvalida(IllegalArgumentException exception) {
        return Map.of("erro", exception.getMessage());
    }
}
