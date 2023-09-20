package br.com.controlefinanceiro.model;

import br.com.controlefinanceiro.dto.DadosCarteira;
import br.com.controlefinanceiro.enums.TipoCarteira;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.Date;

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
    @Column(nullable = false, length = 20)
    private String titulo;
    @Column(nullable = false, length = 100)
    private String descricao;
    @Column(nullable = false)
    private BigDecimal valor;
    @Enumerated(EnumType.STRING)
    private TipoCarteira tipoCarteira;
    @Column
    private Date dataCriacaoCarteira = new Date();
    @ManyToOne
    private Usuario usuario;

    public Carteira(DadosCarteira dados, Usuario usuarioLogado){
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
        this.valor = dados.valor();
        this.usuario = usuarioLogado;
    }

    @Override
    public String toString() {
        return "Carteira: " +
                "titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", tipoCarteira=" + tipoCarteira +
                ", dataCriacaoCarteira=" + dataCriacaoCarteira +
                ", usuario=" + usuario.getLogin() +
                '.';
    }
}
