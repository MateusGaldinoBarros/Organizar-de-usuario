package com.organizador.demo.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.organizador.demo.usuario.controller.MaturacaoController;
import com.organizador.demo.usuario.controller.dto.EntrarMaturacaoRequest;
import com.organizador.demo.usuario.controller.dto.UsuarioResponse;
import com.organizador.demo.usuario.entidade.TipoUsuario;
import com.organizador.demo.usuario.entidade.Usuario;
import com.organizador.demo.usuario.entidade.Whatsapp;
import com.organizador.demo.usuario.exception.MaturacaoNaoIniciadaException;
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
    private MaturacaoController maturacaoController;

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
    void sessaoSoPermiteAvancarAMaturacaoDoUsuarioQueEntrou() {
        MockHttpSession sessaoAna = new MockHttpSession();
        MockHttpSession sessaoBia = new MockHttpSession();
        UsuarioResponse ana = maturacaoController.entrar(new EntrarMaturacaoRequest("Ana"), sessaoAna);
        UsuarioResponse bia = maturacaoController.entrar(new EntrarMaturacaoRequest("Bia"), sessaoBia);

        maturacaoController.avancar(sessaoAna);

        assertEquals(Whatsapp.WPP1, maturacaoController.consultar(sessaoAna).whatsapp());
        assertEquals(bia.id(), maturacaoController.consultar(sessaoBia).id());
        assertEquals(Whatsapp.WPPB, maturacaoController.consultar(sessaoBia).whatsapp());
        assertEquals(ana.id(), maturacaoController.consultar(sessaoAna).id());
    }

    @Test
    void naoAvancaSemEntrarNaMaturacao() {
        assertThrows(MaturacaoNaoIniciadaException.class, () -> maturacaoController.avancar(new MockHttpSession()));
    }

    @Test
    void listaTodasAsMaturacoesSemPermitirAlteraLas() {
        maturacaoController.entrar(new EntrarMaturacaoRequest("Ana"), new MockHttpSession());
        maturacaoController.entrar(new EntrarMaturacaoRequest("Bia"), new MockHttpSession());

        List<UsuarioResponse> maturacoes = maturacaoController.listarTodas();

        assertEquals(2, maturacoes.size());
        assertEquals(Whatsapp.WPPB, maturacoes.getFirst().whatsapp());
        assertEquals(Whatsapp.WPPB, maturacoes.getLast().whatsapp());
    }

    @Test
    void falhaQuandoUsuarioNaoExiste() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.avancar(999L));
    }
}
