package io.github.henriquevital00.minhasfinancas.service;

import io.github.henriquevital00.minhasfinancas.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {
    Usuario autenticar(String email, String senha);
    Usuario salvarUsuario(Usuario usuario);
    void validarEmail(String email);

    Optional<Usuario> obterPorId(Long id);
}
