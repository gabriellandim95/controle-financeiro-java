package br.com.controlefinanceiro.model;

import br.com.controlefinanceiro.dto.DadosConta;
import br.com.controlefinanceiro.enums.StatusConta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 100)
    private String titulo;
    @Column(length = 1000)
    private String descricao;
    @Column
    private Date dataCadastro = new Date();
    @Column(nullable = false)
    private Date dataVencimento;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column
    @Enumerated(EnumType.STRING)
    private StatusConta statusConta;
    @ManyToOne
    private Usuario usuario;

    public Conta(String titulo, String descricao, Date dataCadastro, Date dataVencimento, BigDecimal valor, StatusConta statusConta, Usuario usuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
        this.dataVencimento = dataVencimento;
        this.valor = valor;
        this.statusConta = statusConta;
        this.usuario = usuario;
    }

    public Conta(DadosConta dados){
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
        this.dataVencimento = dados.dataVencimento();
        this.valor = dados.valor();
        this.statusConta = dados.statusConta();
    }

    @Override
    public String toString() {
        return "Conta: " +
                "id= " + id +
                ", titulo= '" + titulo + '\'' +
                ", descricao= '" + descricao + '\'' +
                ", dataCadastro= " + dataCadastro +
                ", dataVencimento= " + dataVencimento +
                ", valor= " + valor +
                ", statusConta = " + statusConta +
                ", usuario = " + usuario.getLogin();
    }
}