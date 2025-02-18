package br.com.lm.controlefinanceiro.model;

import br.com.lm.controlefinanceiro.dto.DadosCarteira;
import br.com.lm.controlefinanceiro.enums.TipoCarteira;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Audited
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carteira")
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String uuid = UUID.randomUUID().toString();
    @Column(nullable = false, length = 20)
    private String titulo;
    @Column(nullable = false, length = 100)
    private String descricao;
    @Column(nullable = false)
    private BigDecimal saldo;
    @Enumerated(EnumType.STRING)
    private TipoCarteira tipoCarteira;
    @Column
    private Date dataCriacaoCarteira = new Date();
    @ManyToOne
    private Usuario usuario;

    public Carteira(DadosCarteira dados, Usuario usuarioLogado){
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
        this.saldo = dados.saldo();
        this.tipoCarteira = dados.tipoCarteira();
        this.usuario = usuarioLogado;
    }

    @Override
    public String toString() {
        return "Carteira: " +
                "titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", saldo=" + saldo +
                ", tipoCarteira=" + tipoCarteira +
                ", dataCriacaoCarteira=" + dataCriacaoCarteira +
                ", usuario=" + usuario.getLogin() +
                '.';
    }
}
