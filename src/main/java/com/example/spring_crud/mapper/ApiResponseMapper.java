package com.example.spring_crud.mapper;


import com.example.spring_crud.dto.response.ApiResponseDTO;
import com.example.spring_crud.dto.response.PaginationResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public final class ApiResponseMapper {

    private ApiResponseMapper(){

    }

    public static <E, D> ApiResponseDTO<D> fromPage(
            Page<E> page,
            Function<E, D> mapper
    ) {
        List<D> data = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return new ApiResponseDTO<>(
                data,
                PaginationResponseDTO.fromPage(page)
        );
    }
}

