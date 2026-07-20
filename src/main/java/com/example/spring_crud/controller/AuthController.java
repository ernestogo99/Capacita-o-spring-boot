package com.example.spring_crud.controller;


import com.example.spring_crud.dto.request.LoginRequestDTO;
import com.example.spring_crud.dto.request.RegisterRequestDTO;
import com.example.spring_crud.dto.response.LoginResponseDTO;
import com.example.spring_crud.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name="Login")
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> signup(
            @RequestBody @Valid RegisterRequestDTO request
    ) {

        LoginResponseDTO response = authService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO request
    ) {

        LoginResponseDTO response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
