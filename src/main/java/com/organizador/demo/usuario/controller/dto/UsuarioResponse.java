package com.organizador.demo.usuario.controller.dto;

import com.organizador.demo.usuario.entidade.TipoUsuario;
import com.organizador.demo.usuario.entidade.Usuario;
import com.organizador.demo.usuario.entidade.Whatsapp;

public record UsuarioResponse(Long id, String nome, TipoUsuario tipoUsuario, Whatsapp whatsapp) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getUsuario(),
                usuario.getWhatsapp());
    }
}
