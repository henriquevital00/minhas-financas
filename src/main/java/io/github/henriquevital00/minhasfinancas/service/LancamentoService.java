package io.github.henriquevital00.minhasfinancas.service;

import io.github.henriquevital00.minhasfinancas.model.entity.Lancamento;
import io.github.henriquevital00.minhasfinancas.model.enums.StatusLancamento;

import java.util.List;

public interface LancamentoService {
    Lancamento salvar(Lancamento lancamento);
    Lancamento atualizar(Lancamento lancamento);
    void deletar(Lancamento lancamento);
    List<Lancamento> buscar(Lancamento lancamentoFiltro);
    void atualziarStatus(Lancamento lancamento, StatusLancamento status);
    void validar(Lancamento lancamento);
}
