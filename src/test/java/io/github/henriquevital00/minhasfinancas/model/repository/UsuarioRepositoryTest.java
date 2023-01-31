package io.github.henriquevital00.minhasfinancas.model.repository;

import io.github.henriquevital00.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;



    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenario
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);
        //acao
        boolean result = repository.existsByEmail("usuario@email.com");
        //verificacap
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail(){
        //cenario
        //acao
        boolean result = repository.existsByEmail("usuario@email.com");
        //verificacao
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public  void devePersistirUmUsuarioNaBaseDeDados(){
        //cenario
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com")
                .senha("senha").build();
        //acao
        Usuario usuarioSalvo = repository.save(usuario);
        //verificacao
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){
        //cenario
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //verificacao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");

        Assertions.assertThat( result.isPresent() ).isTrue();
    }

    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {

        //verificacao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");

        Assertions.assertThat( result.isPresent() ).isFalse();

    }

    public static Usuario criarUsuario() {
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
    }
}
