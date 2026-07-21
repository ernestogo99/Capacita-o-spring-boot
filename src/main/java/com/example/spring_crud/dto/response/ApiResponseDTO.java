package com.example.spring_crud.dto.response;

import java.util.List;

public record ApiResponseDTO<T>(
        List<T> data,
        PaginationResponseDTO pagination
) {
}
