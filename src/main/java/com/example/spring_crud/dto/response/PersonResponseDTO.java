package com.example.spring_crud.dto.response;

public record PersonResponseDTO(
        Long id,
        String name,
        String cpf,
        Integer age
) {
}
