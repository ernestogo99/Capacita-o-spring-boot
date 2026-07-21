package com.example.spring_crud.controller;
import com.example.spring_crud.dto.request.LoginRequestDTO;
import com.example.spring_crud.dto.request.RegisterRequestDTO;
import com.example.spring_crud.dto.response.LoginResponseDTO;
import com.example.spring_crud.dto.response.ExceptionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface AuthControllerDocs {

    @Operation(
            summary = "Cadastrar novo usuário",
            description = """
                    Realiza o cadastro de um novo usuário na aplicação.
                    
                    ### Campos obrigatórios
                    - Nome
                    - Email
                    - Senha
                    
                    Após o cadastro, um token JWT é gerado automaticamente para autenticação.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            schema = @Schema(implementation = ExceptionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email já cadastrado",
                    content = @Content(
                            schema = @Schema(implementation = ExceptionResponseDTO.class)
                    )
            )
    })
    ResponseEntity<LoginResponseDTO> signup(

            @Parameter(
                    description = "Dados necessários para cadastro",
                    required = true
            )
            @RequestBody @Valid RegisterRequestDTO request

    );

    @Operation(
            summary = "Realizar login",
            description = """
                    Autentica um usuário utilizando email e senha.
                    
                    Caso as credenciais sejam válidas,
                    um token JWT será retornado para autenticação das próximas requisições.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(
                            schema = @Schema(implementation = ExceptionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            schema = @Schema(implementation = ExceptionResponseDTO.class)
                    )
            )
    })
    ResponseEntity<LoginResponseDTO> login(

            @Parameter(
                    description = "Credenciais do usuário",
                    required = true
            )
            @RequestBody @Valid LoginRequestDTO request

    );

}