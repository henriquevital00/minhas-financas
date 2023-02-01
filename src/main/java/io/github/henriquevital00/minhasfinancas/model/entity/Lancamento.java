package io.github.henriquevital00.minhasfinancas.model.entity;

import io.github.henriquevital00.minhasfinancas.model.enums.StatusLancamento;
import io.github.henriquevital00.minhasfinancas.model.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.convert.Jsr310Converters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lancamento", schema = "financas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String descricao;
    @Column
    private Integer mes;
    @Column
    private Integer ano;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @Column
    private BigDecimal valor;
    @Column(name = "data_cadastro")
    //@Convert(converter = Jsr310Converters.LocalDateToDateConverter.class)
    private LocalDate dataCadastro;
    @Column
    @Enumerated(value = EnumType.STRING)
    private TipoLancamento tipo;
    @Column
    @Enumerated(value = EnumType.STRING)
    private StatusLancamento status;
}
