package io.github.henriquevital00.minhasfinancas.exception;

public class ErroAutenticacao extends RuntimeException {
    public ErroAutenticacao(String msg){
        super(msg);
    }
}
