package com.example.spring_crud.mapper;

import com.example.spring_crud.dto.request.RegisterRequestDTO;
import com.example.spring_crud.dto.response.LoginResponseDTO;
import com.example.spring_crud.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    User toEntity(RegisterRequestDTO registerRequestDTO);

    @Mapping(target = "token", ignore = true)
    LoginResponseDTO toLoginResponseDTO(User user);
}