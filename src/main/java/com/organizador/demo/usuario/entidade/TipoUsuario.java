package com.organizador.demo.usuario.entidade;

public enum TipoUsuario {
    PROP,
    USER2,
    USER3,
    USER4,
    VISITANTE;

    public TipoUsuario proximo() {
        TipoUsuario[] tipos = values();
        return tipos[(ordinal() + 1) % tipos.length];
    }
}
