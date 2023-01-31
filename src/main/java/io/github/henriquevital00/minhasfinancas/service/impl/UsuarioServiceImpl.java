package io.github.henriquevital00.minhasfinancas.service.impl;

import io.github.henriquevital00.minhasfinancas.exception.ErroAutenticacao;
import io.github.henriquevital00.minhasfinancas.exception.RegraNegocioException;
import io.github.henriquevital00.minhasfinancas.model.entity.Usuario;
import io.github.henriquevital00.minhasfinancas.model.repository.UsuarioRepository;
import io.github.henriquevital00.minhasfinancas.service.UsuarioService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if(!usuario.isPresent()){
            throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
        }
        if(!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha inválida.");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        this.validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean exist = this.repository.existsByEmail(email);
        if(exist){
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }
}
