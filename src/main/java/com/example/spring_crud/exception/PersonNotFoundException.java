package com.example.spring_crud.exception;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(String message) {
        super(message);
    }

    public PersonNotFoundException(){
      super("Pessoa não encontrada");
    }
}
