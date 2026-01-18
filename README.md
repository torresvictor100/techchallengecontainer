
# TechChallenge – Backend (Spring Boot + Docker)

Backend em Spring Boot 3.5 (Java 21) com MySQL 8 e Docker Compose. Este guia traz o passo a passo para subir o projeto, configurar variáveis e acessar a API.

---

## Stack principal
- Java 21 + Spring Boot 3.5
- MySQL 8.2 (exposto na porta local `3307`)
- Gradle, Docker e Docker Compose
- Swagger UI (OpenAPI) para documentação

## Pré-requisitos
- Docker + Docker Compose
- Git

## Como rodar rapidamente
1. Clonar o repositório
   ```bash
   git clone git@github.com:torresvictor100/techchallengecontainer.git
   cd techchallengecontainer
   ```
2. Criar o arquivo `.env`
   ```bash
   cp .env.exemplo .env
   ```
   Preencha (ou mantenha) os valores:
   ```env
   SERVER_PORT=8080

   # Banco de Dados
   MYSQL_ROOT_PASSWORD=root
   MYSQL_DATABASE=techchallenge
   MYSQL_USER=user
   MYSQL_PASSWORD=user123

   # Autenticação / JWT
   APP_AUTH_EMAIL=admin@tech.com
   APP_AUTH_PASSWORD=123456
   APP_AUTH_JWT_SECRET=MinhaChaveSuperSecreta1234567890
   APP_AUTH_JWT_EXPIRATION_MS=86400000

   # Datasource
   SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/techchallenge?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   SPRING_DATASOURCE_USERNAME=user
   SPRING_DATASOURCE_PASSWORD=user123

   # JPA / Hibernate
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_JPA_SHOW_SQL=true
   SPRING_JPA_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
   ```
   ⚠️ Não faça commit do `.env`.
3. Subir tudo com Docker Compose
   ```bash
   docker compose up -d --build
   ```
   - `tech_db`: MySQL (porta externa `3307`, interna `3306`)
   - `tech_app`: aplicação Spring Boot (porta `8080`, debug remoto `5005`)
4. Conferir os containers
   ```bash
   docker compose ps
   ```

## Acessos úteis
- API base: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- MySQL local: `localhost:3307` (usuário e senha do `.env`)
- Logs: `docker logs -f tech_app` e `docker logs -f tech_db`

## Credenciais padrão
- Admin: `admin@tech.com` / `123456` (criado no startup)

## Resetar ambiente
```bash
docker compose down --rmi all --volumes --remove-orphans
docker compose up -d --build
```

## Estrutura do projeto
```
techchallengecontainer/
├─ src/
├─ Dockerfile
├─ docker-compose.yml
├─ .env / .env.exemplo
├─ build.gradle
├─ settings.gradle
└─ README.md
```

## Extras
- Coleção Postman: `colletion.json` (importe para testar os endpoints).
- Debug remoto: porta `5005` exposta pelo container `tech_app`.

---

## ✅ 9. Tecnologias usadas

* Java 21
* Spring Boot 3.5
* Gradle
* MySQL 8
* Docker
* Docker Compose

---

## ✅ 10. Fim

Agora sua aplicação Spring Boot pode ser levantada com apenas **um comando**:

```
docker compose up -d --build
``` 
