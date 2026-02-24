package com.example.spring_crud.repository;

import com.example.spring_crud.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,Long> {

    boolean existsByCpf(String cpf);



    @Query("""
       SELECT p FROM Person p
       WHERE p.cpf = :cpf
       """)
    Optional<Person> findByCpf(@Param("cpf") String cpf);
}
