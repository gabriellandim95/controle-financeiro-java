package br.com.controlefinanceiro.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "historicoPagamento")
public class HistoricoPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Carteira carteira;
    @ManyToOne
    private Conta conta;
    @ManyToOne
    private Usuario usuario;

    public HistoricoPagamento(Carteira carteira, Conta conta, Usuario usuario) {
        this.carteira = carteira;
        this.conta = conta;
        this.usuario = usuario;
    }
}
