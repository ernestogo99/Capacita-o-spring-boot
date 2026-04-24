package com.example.spring_crud.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginationResponseDTO(
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        List<SortFieldDTO> sort) {

    public static PaginationResponseDTO fromPage(Page<?> page){
        List<SortFieldDTO> sortFields = page.getSort()
                .stream()
                .map(order -> new SortFieldDTO(
                        order.getProperty(),
                        order.getDirection().name()
                ))
                .toList();
        return  new PaginationResponseDTO(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                sortFields
        );
    }
}
