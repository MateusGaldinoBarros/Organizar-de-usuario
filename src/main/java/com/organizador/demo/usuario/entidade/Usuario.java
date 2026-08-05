package com.organizador.demo.usuario.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario usuario = TipoUsuario.PROP;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Whatsapp whatsapp = Whatsapp.WPPB;

    protected Usuario() {
    }

    public Usuario(String nome, TipoUsuario usuario, Whatsapp whatsapp) {
        this.nome = nome;
        this.usuario = usuario;
        this.whatsapp = whatsapp;
    }

    public void avancarRoleta() {
        switch (whatsapp) {
            case WPPB -> whatsapp = Whatsapp.WPP1;
            case WPP1 -> whatsapp = Whatsapp.WPP2;
            case WPP2 -> {
                whatsapp = Whatsapp.WPPB;
                usuario = usuario.proximo();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoUsuario getUsuario() {
        return usuario;
    }

    public Whatsapp getWhatsapp() {
        return whatsapp;
    }
}
