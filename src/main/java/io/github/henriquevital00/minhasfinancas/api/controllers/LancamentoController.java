package io.github.henriquevital00.minhasfinancas.api.controllers;

import io.github.henriquevital00.minhasfinancas.api.dto.AtualizaStatusDTO;
import io.github.henriquevital00.minhasfinancas.api.dto.LancamentoDTO;
import io.github.henriquevital00.minhasfinancas.exception.RegraNegocioException;
import io.github.henriquevital00.minhasfinancas.model.entity.Lancamento;
import io.github.henriquevital00.minhasfinancas.model.entity.Usuario;
import io.github.henriquevital00.minhasfinancas.model.enums.StatusLancamento;
import io.github.henriquevital00.minhasfinancas.model.enums.TipoLancamento;
import io.github.henriquevital00.minhasfinancas.service.LancamentoService;
import io.github.henriquevital00.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService service;
    private final UsuarioService usuarioService;

    @GetMapping("/buscar")
    public ResponseEntity buscar(
            @RequestParam(name = "descricao", required = false) String descricao,
            @RequestParam(name = "mes", required = false) Integer mes,
            @RequestParam(name = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario){
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if(!usuario.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possivel encontrar o usuario   .");
        } else {
            lancamentoFiltro.setUsuario(usuario.get());
        }
        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @PostMapping("/salvar")
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto){
        try{
            Lancamento entidade = converter(dto);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto){
        return service.findById(id).map(entity -> {
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("atualiza-status/{id}")
    public ResponseEntity atualizaStatus(@PathVariable Long id, @RequestBody AtualizaStatusDTO dto){
        return service.findById(id).map(entity -> {
            try {
                StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
                if (statusSelecionado == null){
                    return ResponseEntity.badRequest().body("Não foi possivel atualizar o status do lançamento, envie um status válido");
                }
                entity.setStatus(statusSelecionado);
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            } catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        return service.findById(id).map(entity -> {
            service.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    private Lancamento converter(LancamentoDTO dto) {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado.") );

        lancamento.setUsuario(usuario);

        if(dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }

        if(dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }
}
