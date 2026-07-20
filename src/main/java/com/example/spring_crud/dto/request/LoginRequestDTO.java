package com.example.spring_crud.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "O email não pode ficar em branco")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "A senha não pode ficar em branco")
        String password
) {
}
