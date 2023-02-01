package io.github.henriquevital00.minhasfinancas.api.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.henriquevital00.minhasfinancas.api.dto.UsuarioDTO;
import io.github.henriquevital00.minhasfinancas.exception.ErroAutenticacao;
import io.github.henriquevital00.minhasfinancas.exception.RegraNegocioException;
import io.github.henriquevital00.minhasfinancas.model.entity.Usuario;
import io.github.henriquevital00.minhasfinancas.service.LancamentoService;
import io.github.henriquevital00.minhasfinancas.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest( controllers = UsuarioController.class )
@AutoConfigureMockMvc
public class UsuarioControllerTest {
    static final String API = "/api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService service;

    @MockBean
    LancamentoService lancamentoService;

    @Test
    public void deveAutenticarUmUsuario() throws Exception {
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
        Mockito.when( service.autenticar(email, senha) ).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API.concat("/autenticar") )
                .accept( JSON )
                .contentType( JSON )
                .content(json);


        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isOk()  )
                .andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId())  )
                .andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome())  )
                .andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail())  )

        ;

    }

    @Test
    public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
        Mockito.when( service.autenticar(email, senha) ).thenThrow(ErroAutenticacao.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API.concat("/autenticar") )
                .accept( JSON )
                .contentType( JSON )
                .content(json);


        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isBadRequest()  );

        ;

    }

    @Test
    public void deveCriarUmNovoUsuario() throws Exception {
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

        Mockito.when( service.salvarUsuario(Mockito.any(Usuario.class)) ).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API.concat("/salvar")  )
                .accept( JSON )
                .contentType( JSON )
                .content(json);


        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isCreated()  )
                .andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId())  )
                .andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome())  )
                .andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail())  )

        ;

    }

    @Test
    public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();

        Mockito.when( service.salvarUsuario(Mockito.any(Usuario.class)) ).thenThrow(RegraNegocioException.class);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API.concat("/salvar")  )
                .accept( JSON )
                .contentType( JSON )
                .content(json);


        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isBadRequest()  );

        ;

    }

    @Test
    public void deveObterOSaldoDoUsuario() throws Exception {

        //cenário

        BigDecimal saldo = BigDecimal.valueOf(10);
        Usuario usuario = Usuario.builder().id(1l).email("usuario@email.com").senha( "123").build();
        Mockito.when(service.obterPorId(1l)).thenReturn(Optional.of(usuario));
        Mockito.when(lancamentoService.obterSaldoPorUsuario(1l)).thenReturn(saldo);


        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( API.concat("/saldo/1")  )
                .accept( JSON )
                .contentType( JSON );
        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string("10") );

    }

    @Test
    public void deveRetornarResourceNotFoundQuandoUsuarioNaoExisteParaObterOSaldo() throws Exception {

        //cenário
        Mockito.when(service.obterPorId(1l)).thenReturn(Optional.empty());


        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( API.concat("/1/saldo")  )
                .accept( JSON )
                .contentType( JSON );
        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isNotFound() );

    }
}
