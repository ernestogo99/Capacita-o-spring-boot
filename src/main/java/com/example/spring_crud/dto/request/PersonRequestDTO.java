package com.example.spring_crud.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record PersonRequestDTO(
        @NotBlank(message = "O nome não pode ficar em branco")
        String name,

        @NotBlank(message = "O CPF não pode ficar em branco")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Min(value = 0,message = "A idade não pode ser menor que 0")
        Integer age
) {
}
