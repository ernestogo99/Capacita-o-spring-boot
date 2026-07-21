package com.example.spring_crud.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank(message = "O nome não pode ficar em branco")
        String name,

        @NotBlank(message = "O email não pode ficar em branco")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "A senha não pode ficar em branco")
        String password
) {
}
