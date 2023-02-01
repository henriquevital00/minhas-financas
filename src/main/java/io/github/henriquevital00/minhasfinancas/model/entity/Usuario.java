package io.github.henriquevital00.minhasfinancas.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "usuario", schema = "financas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nome;
    @Column
    private String email;
    @Column
    @JsonIgnore
    private String senha;
}
