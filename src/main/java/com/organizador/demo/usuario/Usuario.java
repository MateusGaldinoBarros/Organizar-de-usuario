package com.organizador.demo.usuario;

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

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Whatsapp whatsapp;

    protected Usuario() {
    }

    public Usuario(String nome, TipoUsuario usuario, Whatsapp whatsapp) {
        this.nome = nome;
        this.usuario = usuario;
        this.whatsapp = whatsapp;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(TipoUsuario usuario) {
        this.usuario = usuario;
    }

    public Whatsapp getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(Whatsapp whatsapp) {
        this.whatsapp = whatsapp;
    }
}
