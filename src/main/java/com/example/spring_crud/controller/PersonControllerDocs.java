package com.example.spring_crud.controller;

import com.example.spring_crud.dto.request.PersonRequestDTO;
import com.example.spring_crud.dto.response.ApiResponseDTO;
import com.example.spring_crud.dto.response.ExceptionResponseDTO;
import com.example.spring_crud.dto.response.PersonResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PersonControllerDocs {
    @Operation(
            summary = "Criar pessoa",
            description = """
                    Cria uma nova pessoa no sistema.
                    
                    ### Campos obrigatórios:
                    - Nome
                    - CPF (único)
                    - Idade
                    
                    ### Regras:
                    - CPF deve ser único
                    - CPF deve conter 11 dígitos numéricos
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pessoa criada com sucesso",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "CPF já cadastrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))
            )
    })
    ResponseEntity<PersonResponseDTO> createPerson(PersonRequestDTO personRequestDTO);


    @Operation(
            summary = "Listar pessoas",
            description = "Retorna todas as pessoas cadastradas no sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso"
    )
    ResponseEntity<List<PersonResponseDTO>> getAllPersons();


    @Operation(
            summary = "Listar pessoas com paginação e ordenação por campos",
            description = "Retorna todas as pessoas cadastradas no sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso"
    )
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> getAllPersonWithPagination(
            @ParameterObject
             Pageable pageable
    );


    @Operation(
            summary = "Buscar pessoa por ID",
            description = "Retorna uma pessoa com base no ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pessoa encontrada",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pessoa não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))
            )
    })
    ResponseEntity<PersonResponseDTO> getPersonById(
            @Parameter(description = "ID da pessoa", required = true)
            Long id
    );


    @Operation(
            summary = "Buscar pessoa por CPF",
            description = "Retorna uma pessoa com base no CPF informado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pessoa encontrada",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pessoa não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))
            )
    })
    ResponseEntity<PersonResponseDTO> getPersonByCPF(
            @Parameter(description = "CPF da pessoa (apenas números)", required = true)
            String cpf
    );


    @Operation(
            summary = "Atualizar pessoa",
            description = """
                    Atualiza os dados de uma pessoa existente.
                    
                    ### Regras:
                    - A pessoa deve existir
                    - CPF deve continuar único
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pessoa atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pessoa não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "CPF já cadastrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))
            )
    })
    ResponseEntity<PersonResponseDTO> updatePersonById(
            @Parameter(description = "ID da pessoa", required = true)
            Long id,
            PersonRequestDTO personRequestDTO
    );


    @Operation(
            summary = "Excluir pessoa",
            description = "Remove uma pessoa do sistema a partir do ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pessoa removida com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pessoa não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))
            )
    })
    ResponseEntity<Void> deletePerson(
            @Parameter(description = "ID da pessoa", required = true)
            Long id
    );
}
