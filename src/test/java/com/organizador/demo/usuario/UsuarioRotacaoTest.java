package com.organizador.demo.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.organizador.demo.usuario.entidade.TipoUsuario;
import com.organizador.demo.usuario.entidade.Usuario;
import com.organizador.demo.usuario.entidade.Whatsapp;
import org.junit.jupiter.api.Test;

class UsuarioRotacaoTest {

    @Test
    void avancaWhatsappEAposWpp2AvancaTipoUsuario() {
        Usuario usuario = new Usuario("Ana", TipoUsuario.PROP, Whatsapp.WPPB);

        usuario.avancarMaturacao();
        assertEquals(Whatsapp.WPP1, usuario.getWhatsapp());
        assertEquals(TipoUsuario.PROP, usuario.getUsuario());

        usuario.avancarMaturacao();
        assertEquals(Whatsapp.WPP2, usuario.getWhatsapp());

        usuario.avancarMaturacao();
        assertEquals(Whatsapp.WPPB, usuario.getWhatsapp());
        assertEquals(TipoUsuario.USER2, usuario.getUsuario());
    }

    @Test
    void reiniciaAposUltimoTipoUsuario() {
        Usuario usuario = new Usuario("Ana", TipoUsuario.VISITANTE, Whatsapp.WPP2);

        usuario.avancarMaturacao();

        assertEquals(TipoUsuario.PROP, usuario.getUsuario());
        assertEquals(Whatsapp.WPPB, usuario.getWhatsapp());
    }
}
