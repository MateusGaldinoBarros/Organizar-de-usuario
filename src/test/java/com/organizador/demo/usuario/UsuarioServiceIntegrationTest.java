package com.organizador.demo.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.organizador.demo.usuario.controller.RoletaController;
import com.organizador.demo.usuario.controller.dto.EntrarRoletaRequest;
import com.organizador.demo.usuario.controller.dto.UsuarioResponse;
import com.organizador.demo.usuario.entidade.TipoUsuario;
import com.organizador.demo.usuario.entidade.Usuario;
import com.organizador.demo.usuario.entidade.Whatsapp;
import com.organizador.demo.usuario.exception.RoletaNaoIniciadaException;
import com.organizador.demo.usuario.exception.UsuarioNaoEncontradoException;
import com.organizador.demo.usuario.repository.UsuarioRepository;
import com.organizador.demo.usuario.service.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

@SpringBootTest
class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoletaController roletaController;

    @AfterEach
    void limparBanco() {
        usuarioRepository.deleteAll();
    }

    @Test
    void entraNoEstadoInicialEAvancaSomenteOUsuarioSolicitado() {
        Usuario ana = usuarioService.entrar("Ana");
        Usuario bia = usuarioService.entrar("Bia");

        assertEquals(TipoUsuario.PROP, ana.getUsuario());
        assertEquals(Whatsapp.WPPB, ana.getWhatsapp());
        usuarioService.avancar(ana.getId());

        assertEquals(Whatsapp.WPP1, usuarioRepository.findById(ana.getId()).orElseThrow().getWhatsapp());
        assertEquals(TipoUsuario.PROP, usuarioRepository.findById(bia.getId()).orElseThrow().getUsuario());
        assertEquals(Whatsapp.WPPB, usuarioRepository.findById(bia.getId()).orElseThrow().getWhatsapp());
    }

    @Test
    void sessaoSoPermiteAvancarARoletaDoUsuarioQueEntrou() {
        MockHttpSession sessaoAna = new MockHttpSession();
        MockHttpSession sessaoBia = new MockHttpSession();
        UsuarioResponse ana = roletaController.entrar(new EntrarRoletaRequest("Ana"), sessaoAna);
        UsuarioResponse bia = roletaController.entrar(new EntrarRoletaRequest("Bia"), sessaoBia);

        roletaController.avancar(sessaoAna);

        assertEquals(Whatsapp.WPP1, roletaController.consultar(sessaoAna).whatsapp());
        assertEquals(bia.id(), roletaController.consultar(sessaoBia).id());
        assertEquals(Whatsapp.WPPB, roletaController.consultar(sessaoBia).whatsapp());
        assertEquals(ana.id(), roletaController.consultar(sessaoAna).id());
    }

    @Test
    void naoAvancaSemEntrarNaRoleta() {
        assertThrows(RoletaNaoIniciadaException.class, () -> roletaController.avancar(new MockHttpSession()));
    }

    @Test
    void listaTodasAsRoletasSemPermitirAlteraLas() {
        roletaController.entrar(new EntrarRoletaRequest("Ana"), new MockHttpSession());
        roletaController.entrar(new EntrarRoletaRequest("Bia"), new MockHttpSession());

        List<UsuarioResponse> roletas = roletaController.listarTodas();

        assertEquals(2, roletas.size());
        assertEquals(Whatsapp.WPPB, roletas.getFirst().whatsapp());
        assertEquals(Whatsapp.WPPB, roletas.getLast().whatsapp());
    }

    @Test
    void falhaQuandoUsuarioNaoExiste() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.avancar(999L));
    }
}
