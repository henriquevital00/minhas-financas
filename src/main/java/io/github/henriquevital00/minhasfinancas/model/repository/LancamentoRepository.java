package io.github.henriquevital00.minhasfinancas.model.repository;

import io.github.henriquevital00.minhasfinancas.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
