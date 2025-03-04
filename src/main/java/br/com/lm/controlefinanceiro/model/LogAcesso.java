package br.com.lm.controlefinanceiro.model;

import br.com.lm.controlefinanceiro.enums.TipoLogEvento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logacessos")
public class LogAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String usuario;
    @Column(length = 2000, nullable = false)
    private String informacaoAcessada;
    @Column
    private LocalDateTime dataHoraEvento = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TipoLogEvento tipoLogEvento;

    public LogAcesso() {
    }

    public LogAcesso(String usuario, String informacaoAcessada, TipoLogEvento tipoLogEvento) {
        this.usuario = usuario;
        this.informacaoAcessada = informacaoAcessada;
        this.tipoLogEvento = tipoLogEvento;
    }

    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEvento() {
        return informacaoAcessada;
    }

    public void setEvento(String evento) {
        this.informacaoAcessada = evento;
    }

    public LocalDateTime getDataHoraEvento() {
        return dataHoraEvento;
    }

    public void setDataHoraEvento(LocalDateTime dataHoraEvento) {
        this.dataHoraEvento = dataHoraEvento;
    }

    public TipoLogEvento getTipoLogEvento() {
        return tipoLogEvento;
    }

    public void setTipoLogEvento(TipoLogEvento tipoLogEvento) {
        this.tipoLogEvento = tipoLogEvento;
    }
}
