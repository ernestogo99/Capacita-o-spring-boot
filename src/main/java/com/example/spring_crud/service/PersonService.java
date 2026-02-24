package com.example.spring_crud.service;

import com.example.spring_crud.dto.request.PersonRequestDTO;
import com.example.spring_crud.dto.response.PersonResponseDTO;
import com.example.spring_crud.entity.Person;
import com.example.spring_crud.exception.CpfJaCadastradoException;
import com.example.spring_crud.exception.PersonNotFoundException;
import com.example.spring_crud.mapper.PersonMapper;
import com.example.spring_crud.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PersonRepository personRepository;


    public PersonResponseDTO createPerson(PersonRequestDTO personRequestDTO){
        Person person=this.personMapper.toEntity(personRequestDTO);
        if(this.personRepository.existsByCpf(person.getCpf())){
            throw  new CpfJaCadastradoException();
        }
        Person savedPerson=this.personRepository.save(person);
        return  this.personMapper.toResponseDTO(savedPerson);
    }


    public Person getPerson(Long id){
        return this.personRepository.findById(id).orElseThrow(()->new PersonNotFoundException());
    }

    public PersonResponseDTO getPersonById(Long id){
        Person person=this.getPerson(id);
        return this.personMapper.toResponseDTO(person);
    }

    public PersonResponseDTO getPersonByCPF(String cpf){
        Person person=this.personRepository.findByCpf(cpf).orElseThrow(()-> new PersonNotFoundException());
        return this.personMapper.toResponseDTO(person);
    }

    public void deletePersonById(Long id){
        Person person=this.getPerson(id);
        this.personRepository.delete(person);
    }

    public List<PersonResponseDTO> getAllPersons(){
        return this.personMapper.toListResponseDTO(this.personRepository.findAll());
    }

    public PersonResponseDTO updatePerson(Long id,PersonRequestDTO personRequestDTO){
        Person person=this.getPerson(id);
        person.setAge(personRequestDTO.age());
        person.setCpf(personRequestDTO.cpf());
        person.setName(personRequestDTO.name());

        Person save=this.personRepository.save(person);
        return this.personMapper.toResponseDTO(save);
    }
}
