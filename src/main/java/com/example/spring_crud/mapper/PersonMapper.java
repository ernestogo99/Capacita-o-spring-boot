package com.example.spring_crud.mapper;


import com.example.spring_crud.dto.request.PersonRequestDTO;
import com.example.spring_crud.dto.response.PersonResponseDTO;
import com.example.spring_crud.entity.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person toEntity(PersonRequestDTO personRequestDTO);

    PersonRequestDTO toRequestDTO(Person person);

    PersonResponseDTO toResponseDTO(Person person);

    List<PersonResponseDTO> toListResponseDTO(List<Person> personList);
}
