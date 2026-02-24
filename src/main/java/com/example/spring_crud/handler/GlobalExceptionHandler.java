package com.example.spring_crud.handler;


import com.example.spring_crud.dto.response.ExceptionResponseDTO;
import com.example.spring_crud.dto.response.FieldError;
import com.example.spring_crud.exception.CpfJaCadastradoException;
import com.example.spring_crud.exception.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> threatGeneralExceptions(Exception exception){
        ExceptionResponseDTO exceptionDTO=new ExceptionResponseDTO(null, HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<FieldError> errors=new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error->{
            FieldError fieldError=new FieldError(error.getField(),error.getDefaultMessage());
            errors.add(fieldError);
        });

        ExceptionResponseDTO response = new ExceptionResponseDTO(errors, HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> threatSystemNotFound(PersonNotFoundException exception){
        ExceptionResponseDTO exceptionDTO=new ExceptionResponseDTO(
                null,
                HttpStatus.NOT_FOUND.toString(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);

    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<ExceptionResponseDTO> threatSystemNotFound(CpfJaCadastradoException exception){
        ExceptionResponseDTO exceptionDTO=new ExceptionResponseDTO(
                null,
                HttpStatus.CONFLICT.toString(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionDTO);

    }
}
