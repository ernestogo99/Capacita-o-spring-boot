package com.example.spring_crud.service;

import com.example.spring_crud.dto.request.LoginRequestDTO;
import com.example.spring_crud.dto.request.RegisterRequestDTO;
import com.example.spring_crud.dto.response.LoginResponseDTO;
import com.example.spring_crud.entity.User;
import com.example.spring_crud.exception.DuplicateException;
import com.example.spring_crud.exception.PersonNotFoundException;
import com.example.spring_crud.exception.SenhaIncorretaException;
import com.example.spring_crud.mapper.AuthMapper;
import com.example.spring_crud.repository.UserRepository;
import com.example.spring_crud.security.jwt.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthMapper authMapper;

    public AuthService(AuthMapper authMapper,UserRepository userRepository,PasswordEncoder passwordEncoder,JWTService jwtService){
        this.jwtService=jwtService;
        this.authMapper=authMapper;
        this.passwordEncoder=passwordEncoder;
        this.userRepository=userRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        User user=this.getUserByEmail(loginRequestDTO.email());
        if(this.passwordEncoder.matches(loginRequestDTO.password(),user.getPassword())){
            String token=this.jwtService.generateToken(user);
            return new LoginResponseDTO(user.getName(),token );
        }
        throw  new SenhaIncorretaException();
    }


    public LoginResponseDTO signup(RegisterRequestDTO request){
        String email = request.email();
        Optional<User> existingUser = this.userRepository.findByEmail(email);

        if(existingUser.isPresent()){
            throw new DuplicateException(String.format("Usuário com o email '%s' já existe", email));
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = this.authMapper.toEntity(request);
        user.setPassword(hashedPassword);
        this.userRepository.save(user);

        String token=this.jwtService.generateToken(user);
        return new LoginResponseDTO(user.getName(),token );
    }

    private User getUserByEmail(String email){
        return this.userRepository.findByEmail(email).orElseThrow(()->new PersonNotFoundException("Usuário não encontrado"));
    }
}
