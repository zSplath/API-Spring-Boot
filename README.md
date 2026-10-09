# Clift Med API (Spring Boot)

API REST de uma clínica médica fictícia, desenvolvida nos cursos de Spring Boot da Alura. Permite cadastrar **médicos** e **pacientes** e **agendar ou cancelar consultas**, com autenticação via JWT. O código está na pasta [`api`](api).

Principais pontos trabalhados:

- CRUD com Spring Web + Spring Data JPA, usando DTOs (`record`) e Bean Validation
- Paginação e ordenação nas listagens
- Exclusão lógica (campo `ativo`)
- Migrations de banco com **Flyway**
- **Spring Security** com autenticação stateless via **JWT** (auth0 java-jwt) e senhas com BCrypt
- Tratamento de erros centralizado (`@RestControllerAdvice`)
- Regras de negócio no agendamento de consultas, com validadores plugáveis:
  - funcionamento de segunda a sábado, das 07h às 19h
  - antecedência mínima de 30 minutos
  - escolha automática de médico livre pela especialidade, quando nenhum médico é informado
- Documentação automática com **SpringDoc / Swagger UI**
- Testes de controller (MockMvc + JacksonTester) e de repository (`@DataJpaTest`)
- Perfis `dev`, `test` e `prod`

## Tecnologias

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Validation, Security, DevTools)
- MySQL 8 + Flyway
- Lombok
- java-jwt (auth0)
- SpringDoc OpenAPI
- JUnit 5, Mockito, MockMvc

## Pré-requisitos

- JDK 17+
- MySQL 8 rodando localmente (Maven não precisa ser instalado: o projeto usa o Maven Wrapper `mvnw`)

## Como rodar

1. Crie o banco de dados no MySQL:

   ```sql
   CREATE DATABASE cliftmed_api;
   ```

2. Ajuste usuário e senha do MySQL em `api/src/main/resources/application.properties`:

   ```properties
   spring.datasource.username=user
   spring.datasource.password=pass
   ```

3. (Opcional) Defina a variável de ambiente `JWT_SECRET` com a chave de assinatura dos tokens. Sem ela, é usado um valor padrão de desenvolvimento.

4. Rode a aplicação:

   ```bash
   cd api
   ./mvnw spring-boot:run      # Linux/macOS
   mvnw.cmd spring-boot:run    # Windows
   ```

As tabelas são criadas automaticamente pelo Flyway. A API sobe em `http://localhost:8080`.

### Criando um usuário para login

Não há rota de cadastro de usuário. Insira um diretamente no banco, com a senha em **BCrypt** (gere o hash em qualquer gerador BCrypt, por exemplo [bcrypt-generator.com](https://bcrypt-generator.com/)):

```sql
INSERT INTO usuarios (login, senha) VALUES ('admin@clift.med', '<hash-bcrypt-da-senha>');
```

## Testando

### Pelo Swagger

Acesse `http://localhost:8080/swagger-ui.html`, faça o `POST /login`, copie o token retornado e clique em **Authorize** para testar as demais rotas.

### Por requisições

```bash
# 1. Login (retorna o token JWT)
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login": "admin@clift.med", "senha": "<sua-senha>"}'

# 2. Cadastrar um médico (use o token nas demais requisições)
curl -X POST http://localhost:8080/medicos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Ana Souza", "email": "ana@clift.med", "telefone": "11999999999",
    "crm": "123456", "especialidade": "CARDIOLOGIA",
    "endereco": {"logradouro": "Rua A", "bairro": "Centro", "cep": "01001000",
                 "cidade": "São Paulo", "uf": "SP", "numero": "10"}
  }'

# 3. Listar médicos (paginado: ?page=0&size=5&sort=nome)
curl http://localhost:8080/medicos -H "Authorization: Bearer <token>"

# 4. Agendar uma consulta (sem idMedico, escolhe um médico livre da especialidade)
curl -X POST http://localhost:8080/consultas \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"idPaciente": 1, "especialidade": "CARDIOLOGIA", "data": "2030-01-10T10:00"}'
```

### Rotas

| Recurso | Rotas |
|---|---|
| Autenticação | `POST /login` |
| Médicos | `POST /medicos`, `GET /medicos`, `GET /medicos/{id}`, `PUT /medicos`, `DELETE /medicos/{id}` |
| Pacientes | `POST /pacientes`, `GET /pacientes`, `GET /pacientes/{id}`, `PUT /pacientes`, `DELETE /pacientes/{id}` |
| Consultas | `POST /consultas` (agendar), `DELETE /consultas` (cancelar com `idConsulta` e `motivo`: `PACIENTE_DESISTIU`, `MEDICO_CANCELOU` ou `OUTROS`) |

Especialidades disponíveis: `ORTOPEDIA`, `CARDIOLOGIA`, `GINECOLOGIA`, `DERMATOLOGIA`.

### Testes automatizados

Os testes de repository usam o perfil `test` e um banco MySQL separado. Crie o banco antes de rodar:

```sql
CREATE DATABASE cliftmed_api_test;
```

```bash
cd api
./mvnw test
```

## Produção

O perfil `prod` lê a conexão do banco de variáveis de ambiente:

```bash
DATASOURCE_URL=jdbc:mysql://<host>/<banco> \
DATASOURCE_USER=<usuario> \
DATASOURCE_PASSWORD=<senha> \
JWT_SECRET=<chave> \
java -jar -Dspring.profiles.active=prod target/api-0.0.1-SNAPSHOT.jar
```
