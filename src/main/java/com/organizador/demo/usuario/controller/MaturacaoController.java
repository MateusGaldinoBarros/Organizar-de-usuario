package com.organizador.demo.usuario.controller;

import java.util.List;

import com.organizador.demo.usuario.controller.dto.EntrarMaturacaoRequest;
import com.organizador.demo.usuario.controller.dto.UsuarioResponse;
import com.organizador.demo.usuario.entidade.Usuario;
import com.organizador.demo.usuario.exception.MaturacaoNaoIniciadaException;
import com.organizador.demo.usuario.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maturacao")
public class MaturacaoController {

    private static final String USUARIO_ID_DA_MATURACAO = "usuarioIdDaMaturacao";

    private final UsuarioService usuarioService;

    public MaturacaoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/entrar")
    public UsuarioResponse entrar(@RequestBody EntrarMaturacaoRequest request, HttpSession session) {
        Usuario usuario = usuarioService.entrar(request.nome());
        session.setAttribute(USUARIO_ID_DA_MATURACAO, usuario.getId());
        return UsuarioResponse.from(usuario);
    }

    @GetMapping
    public UsuarioResponse consultar(HttpSession session) {
        return UsuarioResponse.from(usuarioService.buscarPorId(usuarioIdDaSessao(session)));
    }

    @GetMapping("/todas")
    public List<UsuarioResponse> listarTodas() {
        return usuarioService.listar().stream().map(UsuarioResponse::from).toList();
    }

    @PostMapping("/avancar")
    public UsuarioResponse avancar(HttpSession session) {
        return UsuarioResponse.from(usuarioService.avancar(usuarioIdDaSessao(session)));
    }

    private Long usuarioIdDaSessao(HttpSession session) {
        Object usuarioId = session.getAttribute(USUARIO_ID_DA_MATURACAO);
        if (!(usuarioId instanceof Long id)) {
            throw new MaturacaoNaoIniciadaException();
        }
        return id;
    }
}
