package com.organizador.demo.usuario.service;

import java.util.List;

import com.organizador.demo.usuario.entidade.TipoUsuario;
import com.organizador.demo.usuario.entidade.Usuario;
import com.organizador.demo.usuario.entidade.Whatsapp;
import com.organizador.demo.usuario.exception.UsuarioNaoEncontradoException;
import com.organizador.demo.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario entrar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        String nomeNormalizado = nome.strip();
        return usuarioRepository.findByNome(nomeNormalizado)
                .orElseGet(() -> usuarioRepository.save(new Usuario(nomeNormalizado, TipoUsuario.PROP, Whatsapp.WPPB)));
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    @Transactional
    public Usuario avancar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        usuario.avancarMaturacao();
        return usuario;
    }
}
