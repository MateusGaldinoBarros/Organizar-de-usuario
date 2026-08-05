package com.organizador.demo.usuario.controller;

import com.organizador.demo.usuario.controller.dto.EntrarRoletaRequest;
import com.organizador.demo.usuario.controller.dto.UsuarioResponse;
import com.organizador.demo.usuario.entidade.Usuario;
import com.organizador.demo.usuario.exception.RoletaNaoIniciadaException;
import com.organizador.demo.usuario.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roleta")
public class RoletaController {

    private static final String USUARIO_ID_DA_ROLETA = "usuarioIdDaRoleta";

    private final UsuarioService usuarioService;

    public RoletaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/entrar")
    public UsuarioResponse entrar(@RequestBody EntrarRoletaRequest request, HttpSession session) {
        Usuario usuario = usuarioService.entrar(request.nome());
        session.setAttribute(USUARIO_ID_DA_ROLETA, usuario.getId());
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
        Object usuarioId = session.getAttribute(USUARIO_ID_DA_ROLETA);
        if (!(usuarioId instanceof Long id)) {
            throw new RoletaNaoIniciadaException();
        }
        return id;
    }
}
