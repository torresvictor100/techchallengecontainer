---

# ✅ **Como subir a aplicação Spring Boot com Docker**

````markdown
# TechChallenge – Backend (Spring Boot + Docker)

Este projeto é um backend Spring Boot (Java 21) configurado para rodar totalmente via Docker utilizando Dockerfile e Docker Compose

O objetivo deste README é explicar exatamente o que você precisa instalar e como subir a aplicação usando contêineres, de forma simples e rápida.

---

## ✅ 1. Requisitos necessários

Antes de rodar o projeto, instale:

### **Docker**
```bash
sudo apt update
sudo apt install docker.io -y
````

Ou instalação oficial:

```bash
curl -fsSL https://get.docker.com | sudo sh
```

### **Docker Compose**

Verifique se está instalado:

```bash
docker compose version
```

### **Git**

```bash
sudo apt install git -y
```

Pronto. Isso é tudo o que precisa.

---

## ✅ 2. Clonar o repositório

```bash
git clone git@github.com:torresvictor100/techchallengecontainer.git
cd techchallengecontainer
```

---

## ✅ 3. Criar arquivo `.env`

O Docker Compose utiliza variáveis de ambiente para configurar o MySQL e o Spring Boot.

Crie o arquivo `.env` na raiz do projeto com as variáveis igualarquivo `.env.exemplo` a baixo esta um exemplo de variaveis:

```env

MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=techchallenge
MYSQL_USER=user
MYSQL_PASSWORD=user123

SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/techchallenge?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=user123

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
SPRING_JPA_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
```

⚠ **Nunca faça commit do `.env`.**

---

## ✅ 4. Subindo a aplicação usando Docker Compose

Execute:

```bash
docker compose up -d --build
```

Isso irá:

* Criar o contêiner do **MySQL**
* Criar o contêiner da aplicação **Spring Boot**
* Iniciar tudo automaticamente

Ver os containers:

```bash
docker compose ps
```

---

## ✅ 5. Ver logs da aplicação

Logs do backend:

```bash
docker logs -f tech_app
```

Logs do MySQL:

```bash
docker logs -f tech_db
```

---

## ✅ 6. Acessar a API

A API estará disponível em:

```
http://localhost:8080
```

---

## ✅ 7. Resetar tudo (limpar e subir de novo)

Se quiser limpar completamente:

```bash
docker compose down --rmi all --volumes --remove-orphans
```

Depois subir novamente:

```bash
docker compose build --no-cache
docker compose up -d
```

---

## ✅ 8. Estrutura do projeto

```
techchallengecontainer/
│── src/
│── Dockerfile
│── docker-compose.yml
│── .env
│── .gitignore
│── build.gradle
│── settings.gradle
└── README.md
```

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
