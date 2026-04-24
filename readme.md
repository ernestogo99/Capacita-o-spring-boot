# Capacitação Spring boot 

O objetivo dessa capacitação é ensinar como criar uma api em spring boot utilizando boas práticas

![Swagger](https://img.shields.io/badge/Swagger-%2383B93E?style=for-the-badge&logo=swagger&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%234D6A9C.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white)
## Seção 1 (Crud básico)

- [O que é o Spring Boot?](#o-que-é-o-spring-boot)
- [Como criar um projeto em Java com Spring Boot](#como-criar-um-projeto-em-java-com-spring-boot)
- [Estrutura de Pastas](#estrutura-de-pastas)
- [Passo 1: Configurar o banco de dados (Postgres)](#passo-1-configurar-o-banco-de-dados-postgres)
- [Passo 2: Criação da Entity](#passo-2-criação-da-entity)
- [Passo 3: Criação dos DTOs](#passo-3-criação-dos-dtos)
- [Passo 4: Criação do Mapper](#passo-4-criação-do-mapper)
- [Passo 5: Exceções Personalizadas](#passo-5-exceções-personalizadas)
- [Passo 6: Criação do Repository](#passo-6-criação-do-repository)
- [Passo 7: Criação do Service](#passo-7-criação-do-service)
- [Passo 8: Documentação com Swagger](#passo-8-documentação-com-swagger)
- [Passo 9: Controller](#passo-9-controller)
- [Passo 10: Tratamento de Exceções](#passo-10-tratamento-de-exceções)
- [Boas Práticas](#boas-práticas)
- [Resumo](#resumo)


## O que é o spring Boot?
Spring Boot é um framework que facilita a criação de aplicações Java baseadas no Spring, principalmente APIs e aplicações web, com o mínimo de configuração manual.


## Como criar um projeto em java com spring boot

Basta ir no site spring initializer e escolher as configurações e dependências

````sh 
https://start.spring.io/
````

- dependências: são bibliotecas que ajudam no desenvolvimento e ficam no arquivo pom.xml (MAVEN)

nessa capacitação utilizamos as seguintes dependências:

- Spring Boot DevTools: ferramenta que melhora a produtividade no desenvolvimento, permitindo reinicialização automática da aplicação a cada alteração no código.

- Lombok: biblioteca que reduz código repetitivo, gerando automaticamente getters, setters, construtores e outros métodos através de anotações.

- Spring Web: dependência responsável por criar aplicações web e APIs REST, fornecendo recursos como @RestController, @RequestMapping e servidor embutido.

- PostgreSQL Driver: driver de conexão que permite que a aplicação se comunique com o banco de dados PostgreSQL.

- Spring Data JPA: módulo que facilita o acesso ao banco de dados utilizando JPA e Hibernate, permitindo criar repositórios com menos código.

- Validation: fornece suporte à validação de dados usando anotações como @NotNull, @NotBlank e @Email.

## Estrutura de pastas

Em um projeto back-end, precisamos ter uma estrutura de projeto definida, com o objetivo
de deixar o projeto escalável, fácil de manter e de entender, onde cada camada teria uma única função, separando assim
as responsabilidades.

nessa capacitação iremos usar a estrutura de pastas em camadas

```
com.example.spring_crud/
├── controller/       ← Endpoints REST da aplicação (camada HTTP)
├── dto/              ← Objetos de transferência de dados (entrada e saída da API)
├── entity/           ← Entidades JPA que representam as tabelas do banco
├── exception/        ← Exceções personalizadas de regra de negócio
├── handler/          ← Tratamento global de exceções (@ControllerAdvice)
├── mapper/           ← Conversão entre Entity e DTO (camada de mapeamento)
├── repository/       ← Interfaces de acesso a dados (Spring Data JPA)
├── service/          ← Regras de negócio e orquestração da aplicação
└── SpringCrudApplication.java  ← Classe principal (ponto de entrada do Spring Boot)
```



## Passo 1: Configurar o banco de dados (Postgres)

Vá para o arquivo application.properties na pasta resources e cole isso:

```
# Informações do banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/db_name
spring.datasource.username:postgres
spring.datasource.password:password

# Driver JDBC
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# Logs SQL
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

assim, o banco de dados estará conectado com o seu back-end, basta passar a url,o nome do banco,
o nome do usuário e a senha que você criou no seu postgres ou seja, no lugar de db_name você coloca o nome do banco, no lugar de postgres você coloca o seu usuário 
e no lugar de password você coloca sua senha.


## Passo 2: Criação da entity

Na pasta entity você irá criar as entidades que irão representar as tabelas no banco
de dados, ela irá conter anotações jpa como @id @entity e etc, contudo ela não deve ser
retornada diretamente na api, pois nem sempre você vai querer mostrar todos os dados da entidade 
para o usuário, por isso utilizamos o DTO.

```java
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_people")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "age", nullable = false)
    private Integer age;

}
``` 

as tags getter,setter, allArgsConstructor e noArgsConstructor são do lombok,
elas nos permitem escrever menos código e facilitar o retrabalho, caso alguma propriedade
da entidade mude, elas já geram automaticamente getters, setters e os construtores


## Passo 3: Criação dos dtos (data-transfer-objects)

O dto define o contrato da api e protege a entidade de exposição direta, faremos dtos de requisição e de resposta,
exibindo apenas o necessário.É nessa camada que faremos a validação dos campos utilizando as tags da dependência validation
Nos dtos, utilizaremos record ao invés da classe java comum, pois o dto é apenas um objeto de transporte, que representa o contrato da api, logo ele não precisa
de setter, lógica complexa e mutabilidade.

Um record é um tipo especial de classe introduzido no Java 14 (estável no 16) que serve para representar objetos imutáveis de forma mais simples e concisa.
assim, o java gera automaticamente um construtor,getters,equals(), hashcode(),tostring()


DTO de requisição(nele que irá ficar as validações)

```java 
public record PersonRequestDTO(
        @NotBlank(message = "O nome não pode ficar em branco")

        String name,
        
        @NotBlank(message = "O CPF não pode ficar em branco")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Min(value = 0,message = "A idade não pode ser menor que 0")
        Integer age
) {
}
```

DTO de resposta


```java
public record PersonResponseDTO(
        Long id,
        String name,
        String cpf,
        Integer age
) {
}
```

## Passo 4: Criação do mapper

Converte Entity ↔ DTO
Evita código repetido no  service

Utilizaremos o mapper no service, lá ficará as regras de negócios, o controller
lidará apenas com as requisições http

Nesse passo utilizaremos a biblioteca mapStruct, para isso vamos precisar adicionar 
as seguintes dependências no pom.xml

essa nos colocaremos dentro da parte dependencies
```
<dependency>
			<groupId>org.mapstruct</groupId>
			<artifactId>mapstruct</artifactId>
			<version>1.5.5.Final</version>
</dependency>
```

e essa dentro de annotationProcessorpath

```
	<path>
		<groupId>org.mapstruct</groupId>
		<artifactId>mapstruct-processor</artifactId>
		<version>1.5.5.Final</version>
	</path>
```

após isso, vamos na pasta mapper criar o personmapper

```java
@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person toEntity(PersonRequestDTO personRequestDTO);

    PersonRequestDTO toRequestDTO(Person person);

    PersonResponseDTO toResponseDTO(Person person);

    List<PersonResponseDTO> toListResponseDTO(List<Person> personList);
}

```

Perceba que é apenas uma interface, pois precisamos apenas definir os métodos,
o mapstruct faz a implementação automática do método


## Passo 5: Exceções personalizadas

Devemos criar exceções personalizadas, de acordo com as regras de negócio,
como por exemplo, quando uma pessoa não é encontrado e quando já existe uma pessoa 
com um cpf cadastrado, já que cpf é unico.

O objetivo disso é tornar os erros mais claros, tanto para o usuário, quanto para o desenvolvedor.

```java


public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String message) {
        super(message);
    }

   public CpfJaCadastradoException(){
        super("Cpf já cadastrado");
   }
}


```


```java
public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(String message) {
        super(message);
    }

    public PersonNotFoundException(){
      super("Pessoa não encontrada");
    }
}
```


## Passo 6: Criação do Repository

Aqui é a camada que irá se comunicar com o banco de dados,
nela utilizaremos o jpaRepository, onde já vem métodos nativos,assim,não precisando escrever sql diretamente.

Também podemos criar métodos personalizados, para métodos mais simples, basta colocar o nome 
do método, já para métodos mais complexos, poderemos utilizar jpqa.


```java
public interface PersonRepository extends JpaRepository<Person,Long> {

    boolean existsByCpf(String cpf);



    @Query("""
            SELECT p FROM Person p
            WHERE p.cpf = :cpf
            """)
    Optional<Person> findByCpf(@Param("cpf") String cpf);
}

```

Perceba que criamos uma interface e extendemos o jparepository, Onde o primeiro parametro é a entidade e o segundo é o tipo do id.


## Passo 7: Criação do Service

Aqui é onde ficará as regras de negócios e a lógica da aplicação, também é aqui que iremos chamar o mapper e o repository

Para indicar que é um service, precisamos colocar a tag @Service na classe, perceba que 
estamos utilizando  injeção via construtor ao invés de @autowired do lombok, pois a injeção via construtor facilita
testes, garante imutabilidade(Final) e segue as boas práticas do spring moderno.

Cada método aqui representa uma operação do crud(CREATE-READ-UPDATE-DELETE)

Perceba que estamos usando as exceções personalizadas, pois elas foram criadas de acordo
com as regras de negócio.

```java

@Service
public class PersonService {

    private final PersonMapper personMapper;


    private final PersonRepository personRepository;

    public PersonService(PersonMapper personMapper,PersonRepository personRepository){
        this.personMapper=personMapper;
        this.personRepository=personRepository;
    }

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

```

## Passo 8: Documentação com swagger

A documentação é uma das etapas mais importantes no desenvolvimento de uma API.
Ela permite que desenvolvedores entendam claramente:

- Quais endpoints existem

- Quais métodos HTTP são utilizados

- Quais dados devem ser enviados

- Quais respostas podem ser retornadas

- Quais possíveis erros podem ocorrer

Sem documentação, uma API se torna difícil de usar, manter e evoluir.

O Swagger é uma ferramenta baseada no padrão OpenAPI que permite gerar automaticamente a documentação interativa da API a partir do próprio código.

No ecossistema do Spring Boot, normalmente utilizamos o Springdoc OpenAPI, que integra o Swagger de forma simples.

Ele gera uma interface web onde é possível:

- Visualizar todos os endpoints

- Testar requisições diretamente pelo navegador

- Ver modelos de requisição e resposta

- Ver códigos de status HTTP possíveis

Para adicionar o swagger na sua api, basta adicionar essa dependência no seu pom.xml

```
	<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.8.12</version>
		</dependency>
```

Utilizaremos as tags do swagger no controller para documentar os endpoints,elas também
podem ser utilizadas no dto.

para acessar o swagger, vá para o link:

```
http://localhost:8080/swagger-ui/index.html
```



## Passo 9: Controller

O controller é responsável pela criação das rotas e ele lida com as requisições http,
as regras de negócio devem ficar no service e não no controller, nele também documentamos o endpoint com o swagger,
mas como a documentação com o swagger pega muito espaço do código, vamos separar ela em uma interface,assim deixando o controller mais limpo


exemplo:

```java

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

```



```java

@RestController
@RequestMapping("/pessoas")
@Tag(name="Pessoas")
public class PersonController implements PersonControllerDocs {



    private final PersonService personService;

    public PersonController(PersonService personService){
        this.personService=personService;
    }


    @PostMapping
    @Override
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody @Valid PersonRequestDTO personRequestDTO){
        PersonResponseDTO person=this.personService.createPerson(personRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }


    @GetMapping
    @Override
    public ResponseEntity<List<PersonResponseDTO>> getAllPersons(){
        return ResponseEntity.ok(this.personService.getAllPersons());
    }


    @GetMapping("/{id}")
    @Override
    public ResponseEntity<PersonResponseDTO> getPersonById(     @Parameter(description = "ID da pessoa") @PathVariable Long id){
        PersonResponseDTO personResponseDTO=this.personService.getPersonById(id);
        return ResponseEntity.ok(personResponseDTO);
    }


    @GetMapping("/cpf/{cpf}")
    @Override
    public ResponseEntity<PersonResponseDTO> getPersonByCPF(     @Parameter(description = "CPF da pessoa") @PathVariable String cpf){
        PersonResponseDTO personResponseDTO=this.personService.getPersonByCPF(cpf);
        return ResponseEntity.ok(personResponseDTO);
    }


    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deletePerson(
            @Parameter(description = "ID da pessoa") @PathVariable Long id
    ){
        this.personService.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<PersonResponseDTO> updatePersonById(  @Parameter(description = "ID da pessoa") @PathVariable Long id, @RequestBody @Valid PersonRequestDTO personRequestDTO){
        PersonResponseDTO response=this.personService.updatePerson(id,personRequestDTO);
        return ResponseEntity.ok(response);
    }
}

```


Perceba que há várias tags nele, vamos entender cada uma:

🏷️ @RestController

Indica que a classe é um controller REST.

Ele:

- Combina @Controller + @ResponseBody

- Retorna dados em JSON automaticamente

- Indica que os métodos respondem requisições HTTP

🗺️ @RequestMapping("/pessoas")

Define a rota base do controller.

Isso significa que:

- Todos os endpoints começarão com /pessoas

- Organiza as rotas relacionadas à entidade Pessoa

- Evita repetição de caminho em cada método


📌 @Tag(name="Pessoas")

Anotação do Swagger (OpenAPI).

Ela:

- Agrupa os endpoints na documentação

- Organiza visualmente as rotas por categoria

- Facilita a navegação na interface do Swagger

Na interface, aparecerá uma seção chamada "Pessoas" contendo todos os endpoints desse controller.

📝 @Operation(summary = "...")

Também é uma anotação do Swagger.

Ela:

- Define uma descrição resumida do endpoint

- Explica o que aquele método faz

- Melhora a clareza da documentação


📥 @RequestBody

Indica que os dados vêm do corpo da requisição (JSON).

Ela:

- Converte automaticamente JSON em objeto Java

- Permite enviar dados complexos

- É usada geralmente em POST e PUT


✅ @Valid

Ativa a validação do DTO.

Ela:

- Executa as validações definidas no DTO (@NotNull, @NotBlank, etc.)

- Garante integridade dos dados

- Lança exceção automaticamente se os dados forem inválidos



🔢 @PathVariable

Captura valores diretamente da URL.

Ela:

- Permite receber parâmetros da rota

- Mapeia {id} ou {cpf} para variáveis do método



📘 @Parameter(description = "...")

Anotação do Swagger.

Ela:

- Adiciona descrição ao parâmetro na documentação

- Torna mais claro o que aquele parâmetro representa

- Melhora a experiência de quem consome a API


📤 ResponseEntity

Permite controlar completamente a resposta HTTP.

Ela possibilita:

- Definir código de status (200, 201, 204, etc.)

- Retornar corpo da resposta

- Configurar headers se necessário



## Passo 10: Tratamento de exceções

Vamos criar um handler de exceções na pasta handler, com o objetivo de padronizar
o formato de resposta das exceções, para que elas fiquem mais claras e melhores de visualizar.
Para isso vamo criar dois dtos


DTO para obter os campos inválidos

```java
public record FieldError(String field,String message) {
}

```


Dto de resposta(Perceba que estamos utilizando tags do swagger para documentar o dto)
```java
@Schema(
        description = "DTO that encapsulates detailed information about errors or exceptions."
)
@JsonPropertyOrder(alphabetic = true)
public record ExceptionResponseDTO(



        @Schema(description = "List of field errors detailing the specific issues with the request.")
        List<FieldError> errors,

        @Schema(description = "HTTP status code associated with the error.", example = "400")
        String status,

        @Schema(description = "A detailed description of the error or exception that occurred.", example = "Missing or invalid required fields.")
        String message

) {
}
```

Após isso, vamos criar um globalexception handler na pasta handler e tratar as exceções nele:
Nele usaremos as tags RestControllerAdvice e ExceptionHandler, assim o spring irá entender que ele irá lidar com as exceções

```java

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> threatGeneralExceptions(Exception exception){
        ExceptionResponseDTO exceptionDTO=new ExceptionResponseDTO(null, HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<FieldError> errors=new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error->{
            FieldError fieldError=new FieldError(error.getField(),error.getDefaultMessage());
            errors.add(fieldError);
        });

        ExceptionResponseDTO response = new ExceptionResponseDTO(errors, HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> threatSystemNotFound(PersonNotFoundException exception){
        ExceptionResponseDTO exceptionDTO=new ExceptionResponseDTO(
                null,
                HttpStatus.NOT_FOUND.toString(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);

    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<ExceptionResponseDTO> threatSystemNotFound(CpfJaCadastradoException exception){
        ExceptionResponseDTO exceptionDTO=new ExceptionResponseDTO(
                null,
                HttpStatus.CONFLICT.toString(),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionDTO);

    }
}


```


## Boas práticas

- Criar Exceções personalizadas
- Tratar Exceções
- Escolher uma boa estrutura de projeto
- Métodos com nomes claros
- Respeitar o princípio único de responsabilidade(cada camada tem sua função)
- Documentar a api
- Manter Métodos pequenos e com responsabilidade única


## Resumo

Finalizando a seção 1, vemos que o fluxo que seguimos foi

entity -> dto -> mapper -> repository -> service -> controller


além das exceções personalizadas e do tratamento de exceções
Perceba que cada camada tem sua responsabilidade e que ela se conecta
com as outras camadas, isso deve ser respeitado para garantir que a api 
esteja escalável e fácil de manter.


# Seção 2 (Paginação,Ordenação,Filtros e Busca avançada)

- [Paginação](#paginação)
- [Ordenação](#ordenação)
- [Filtros](#filtros)
- [Busca avançada](#busca-avançada)


# Paginação

- Paginação é uma técnica usada para dividir grandes volumes de dados em partes menores (páginas),ao invés de retornar tudo de uma vez.

A paginação do java possui 3 parâmetros principais:
- page: número de páginas
- size: quantidade de registros por página
- sort: ordenação dos dados


O spring resolve a paginação utilizando o seguinte:
- Pageable: representa os parâmetros da página
- Page<T>: representa o resultado paginado

Vamos criar dtos para padronizar as respostas da api com a paginação


```java
public record SortFieldDTO(
        String field,
        String direction
) {}
```

```java

public record PaginationResponseDTO(
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        List<SortFieldDTO> sort) {

    public static PaginationResponseDTO fromPage(Page<?> page){
        List<SortFieldDTO> sortFields = page.getSort()
                .stream()
                .map(order -> new SortFieldDTO(
                        order.getProperty(),
                        order.getDirection().name()
                ))
                .toList();
        return  new PaginationResponseDTO(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),   
                sortFields
        );
    }
}

```


Perceba que esse T é um tipo genérico, ou seja, podemos deixar essa resposta como padrão, para diversos tipos
```java
public record ApiResponseDTO<T>(
        List<T> data,
        PaginationResponseDTO pagination
) {
}
```

Devemos também fazer um mapper
```java

public final class ApiResponseMapper {

    private ApiResponseMapper(){

    }

    public static <E, D> ApiResponseDTO<D> fromPage(
            Page<E> page,
            Function<E, D> mapper
    ) {
        List<D> data = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return new ApiResponseDTO<>(
                data,
                PaginationResponseDTO.fromPage(page)
        );
    }
}
```



Assim iremos para o service e criaremos um método que retorna uma lista de pessoas com paginação

```java
 public ApiResponseDTO<PersonResponseDTO> getAllPersonWithPagination(Pageable pageable) {
        Page<Person> page = this.personRepository.findAll(pageable);

        return ApiResponseMapper.fromPage(
                page,
                this.personMapper::toResponseDTO
        );
    }
```


Agora no controller:

- na interface controllerdocs
```java
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

```

- no controller
```java
  @GetMapping(
            value = "/with-pagination",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> getAllSystemsWithResult(
            @PageableDefault(
                    size = 10,
                    sort ="age",
                    page = 0
            ) Pageable pageable
    ) {
        ApiResponseDTO<PersonResponseDTO> response =
                this.personService.getAllPersonWithPagination(pageable);
        return ResponseEntity.ok(response);
    }
```



