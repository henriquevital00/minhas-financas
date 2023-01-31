package io.github.henriquevital00.minhasfinancas.api.controllers;

import io.github.henriquevital00.minhasfinancas.api.dto.UsuarioDTO;
import io.github.henriquevital00.minhasfinancas.exception.ErroAutenticacao;
import io.github.henriquevital00.minhasfinancas.exception.RegraNegocioException;
import io.github.henriquevital00.minhasfinancas.model.entity.Usuario;
import io.github.henriquevital00.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto){
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao e){
            return ResponseEntity.badRequest().body(e);
        }

    }

    @PostMapping("/salvar")
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto){
        Usuario usuario = Usuario.builder().nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha()).build();

        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
