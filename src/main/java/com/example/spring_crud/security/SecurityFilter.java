package com.example.spring_crud.security;


import com.example.spring_crud.entity.User;
import com.example.spring_crud.exception.PersonNotFoundException;
import com.example.spring_crud.repository.UserRepository;
import com.example.spring_crud.security.jwt.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {


    private JWTService jwtService;

    private UserRepository userRepository;

    public SecurityFilter(JWTService jwtService, UserRepository userRepository){
        this.jwtService=jwtService;
        this.userRepository=userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=this.recoverToken(request);
        String login= jwtService.validateToken(token);

        if(login !=null){
            User user= this.userRepository.findByEmail(login).orElseThrow(()->new PersonNotFoundException("Usuario não encontrado"));
            List<SimpleGrantedAuthority> authorities= Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            var authentication= new UsernamePasswordAuthenticationToken(user,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }


    private String recoverToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader==null) return  null;
        return authHeader.replace("Bearer ", "");
    }
}
