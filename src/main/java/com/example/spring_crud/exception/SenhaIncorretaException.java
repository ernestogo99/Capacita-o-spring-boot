package com.example.spring_crud.exception;

public class SenhaIncorretaException extends RuntimeException {
    public SenhaIncorretaException(String message) {
        super(message);
    }

    public SenhaIncorretaException(){
        super("Senha incorreta");
    }
}
