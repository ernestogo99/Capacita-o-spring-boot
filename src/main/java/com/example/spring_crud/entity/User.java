package com.example.spring_crud.entity;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String name;

    private String email;

    private String password;
}
