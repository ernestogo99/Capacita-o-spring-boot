package com.example.spring_crud.controller;


import com.example.spring_crud.dto.request.PersonRequestDTO;
import com.example.spring_crud.dto.response.PersonResponseDTO;
import com.example.spring_crud.entity.Person;
import com.example.spring_crud.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
@Tag(name="Pessoas")
public class PersonController {


    @Autowired
    private PersonService personService;


    @PostMapping
    @Operation(summary = "Cria uma nova pessoa com as informações fornecidas")
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody @Valid PersonRequestDTO personRequestDTO){
        PersonResponseDTO person=this.personService.createPerson(personRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }


    @GetMapping
    @Operation(summary = "lista todas as pessoas")
    public ResponseEntity<List<PersonResponseDTO>> getAllPersons(){
        return ResponseEntity.ok(this.personService.getAllPersons());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Busca uma pessoa por id")
    public ResponseEntity<PersonResponseDTO> getPersonById(     @Parameter(description = "ID da pessoa") @PathVariable Long id){
        PersonResponseDTO personResponseDTO=this.personService.getPersonById(id);
        return ResponseEntity.ok(personResponseDTO);
    }


    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Busca uma pessoa por cpf")
    public ResponseEntity<PersonResponseDTO> getPersonByCPF(     @Parameter(description = "CPF da pessoa") @PathVariable String cpf){
        PersonResponseDTO personResponseDTO=this.personService.getPersonByCPF(cpf);
        return ResponseEntity.ok(personResponseDTO);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma pessoa pelo ID")
    public ResponseEntity<Void> deletePerson(
            @Parameter(description = "ID da pessoa") @PathVariable Long id
    ){
        this.personService.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }
}
