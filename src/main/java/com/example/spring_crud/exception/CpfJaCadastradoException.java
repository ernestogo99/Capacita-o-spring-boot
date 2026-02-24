package com.example.spring_crud.exception;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String message) {
        super(message);
    }

   public CpfJaCadastradoException(){
        super("Cpf já cadastrado");
   }
}
