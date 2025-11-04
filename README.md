# 🚀 User Project API - Guia Completo

API REST para gerenciamento de usuários e projetos externos, desenvolvida com Spring Boot 3.5.7, PostgreSQL e Flyway.

## 📋 Índice

- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Endpoints da API](#-endpoints-da-api)
- [Docker Compose](#-docker-compose)
- [Flyway Migrations](#-flyway-migrations)
- [Testes](#-testes)
- [Monitoramento](#-monitoramento)

## 🛠 Tecnologias

- **Java 21**
- **Spring Boot 3.5.7**
- **PostgreSQL 16**
- **Flyway** (Versionamento de BD)
- **MapStruct** (Mapeamento de objetos)
- **Lombok** (Redução de boilerplate)
- **Docker & Docker Compose**
- **Maven**

## 📦 Pré-requisitos

### Para rodar localmente
- Java 21+
- Maven 3.9+
- PostgreSQL 16+ (local ou via Docker)

### Para rodar com Docker
- Docker 20.10+
- Docker Compose 2.0+

## 🚀 Instalação e Execução

### Opção 1: Docker Compose (Recomendado)

```bash
# Build e start de todos os serviços (PostgreSQL + Aplicação)
docker-compose up -d

# Verificar logs
docker-compose logs -f app

# Parar serviços
docker-compose down
```

**A aplicação estará disponível em:** http://localhost:8080

**PostgreSQL:**
- Host: localhost:5432
- Database: userproject_db
- User: userproject
- Password: userproject123

### Opção 2: Executar Localmente

#### Passo 1: Subir PostgreSQL
```bash
# Via Docker (Recomendado)
docker-compose up -d postgres

# OU use PostgreSQL instalado localmente
# Certifique-se de ter criado o banco: userproject_db
```

#### Passo 2: Rodar a aplicação
```bash
# Compilar o projeto
./mvnw clean package

# Rodar aplicação
./mvnw spring-boot:run
```

**Nota:** Para mais opções de configuração, consulte `POSTGRESQL-SETUP.md`

## 📡 Endpoints da API

### 👤 Usuários

#### Criar Usuário
```bash
POST /users
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

**Resposta 201 Created:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

#### Buscar Usuário
```bash
GET /users/{id}
```

**Resposta 200 OK:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

#### Atualizar Usuário
```bash
PUT /users/{id}
Content-Type: application/json

{
  "email": "updated@example.com",
  "password": "newpassword",
  "name": "Updated Name"
}
```

#### Deletar Usuário
```bash
DELETE /users/{id}
```

**Resposta: 204 No Content**

---

### 📂 Projetos Externos

#### Adicionar Projeto ao Usuário
```bash
POST /users/{user-id}/projects
Content-Type: application/json

{
  "name": "My Awesome Project"
}
```

**Resposta 201 Created:**
```json
{
  "id": 1,
  "name": "My Awesome Project",
  "userId": 1
}
```

#### Listar Projetos do Usuário (com paginação)
```bash
GET /users/{user-id}/projects?page=0&size=10&sort=name,asc
```

**Resposta 200 OK:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Project Alpha",
      "userId": 1
    },
    {
      "id": 2,
      "name": "Project Beta",
      "userId": 1
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 2,
  "totalPages": 1
}
```

---

## 🐳 Docker Compose

### Arquitetura

```
┌─────────────────┐         ┌──────────────────┐
│   PostgreSQL    │◄────────│   Spring Boot    │
│   (port 5432)   │         │    (port 8080)   │
└─────────────────┘         └──────────────────┘
```

### Comandos Úteis

```bash
# Subir serviços
docker-compose up -d

# Rebuild da aplicação
docker-compose up -d --build app

# Ver logs em tempo real
docker-compose logs -f

# Parar e remover tudo (incluindo volumes)
docker-compose down -v

# Acessar PostgreSQL
docker-compose exec postgres psql -U userproject -d userproject_db

# Verificar status
docker-compose ps
```

### Configuração de Ambiente

Você pode customizar as variáveis de ambiente no `docker-compose.yml`:

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/userproject_db
  SPRING_DATASOURCE_USERNAME: userproject
  SPRING_DATASOURCE_PASSWORD: userproject123
  JAVA_OPTS: "-Xms512m -Xmx1024m"
```

---

## 📊 Flyway Migrations

### Estrutura de Migrations

```
src/main/resources/db/migration/
├── V1__create_user_table.sql
└── V2__create_user_external_project_table.sql
```

### Convenções de Nomenclatura

- **V** = Version (obrigatório)
- **1** = Número da versão (sequencial)
- **__** = Dois underscores separando versão da descrição
- **create_user_table** = Descrição (snake_case)
- **.sql** = Extensão

### Verificar Status das Migrations

```bash
curl http://localhost:8080/actuator/flyway
```

**Nota:** O banco inicia vazio. Use os endpoints da API para criar dados.

---

## 🧪 Testes

### Executar Todos os Testes

```bash
./mvnw test
```

### Cobertura de Testes

O projeto possui testes para:

- ✅ **Controllers** (UserControllerTest, UserProjectControllerTest)
- ✅ **Use Cases** (5 classes de teste)
- ✅ **Validações** de entrada
- ✅ **Exception Handling**

### Estrutura de Testes

```
src/test/java/
├── presentation/
│   ├── UserControllerTest.java
│   └── UserProjectControllerTest.java
└── domain/usecase/
    ├── UserUpsertUseCaseTest.java
    ├── RetrieveUserInformationUseCaseTest.java
    ├── UserDeletionUseCaseTest.java
    ├── UserProjectCreationUseCaseTest.java
    └── UserProjectRetrievalUseCaseTest.java
```

---

## 📈 Monitoramento

### Spring Boot Actuator Endpoints

#### Health Check
```bash
curl http://localhost:8080/actuator/health
```

**Resposta:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    }
  }
}
```

#### Métricas
```bash
curl http://localhost:8080/actuator/metrics
```

#### Prometheus (para integração com Grafana)
```bash
curl http://localhost:8080/actuator/prometheus
```

#### Flyway Info
```bash
curl http://localhost:8080/actuator/flyway
```

---

## 🏗 Arquitetura

O projeto segue **Clean Architecture** com as seguintes camadas:

```
src/main/java/com/user/project/
├── domain/              # Regras de negócio
│   ├── model/          # Entidades de domínio
│   ├── repository/     # Interfaces de repositório
│   ├── usecase/        # Casos de uso
│   └── exception/      # Exceções de negócio
├── infrastructure/      # Implementações técnicas
│   ├── entity/         # Entidades JPA
│   ├── repository/     # Repositórios JPA
│   ├── gateways/       # Implementação dos gateways
│   ├── mapper/         # Mappers de infraestrutura
│   └── exception/      # Exception handlers
└── presentation/        # Camada de apresentação
    ├── UserController.java
    ├── UserProjectController.java
    ├── request/        # DTOs de entrada
    ├── response/       # DTOs de saída
    └── mapper/         # Mappers de apresentação
```

---

## 🔒 Validações

### UserRequest
- **email**: Obrigatório, formato válido, max 200 caracteres
- **password**: Obrigatório, min 6 caracteres, max 129 caracteres
- **name**: Opcional, max 120 caracteres

### ProjectRequest
- **name**: Obrigatório, min 3 caracteres, max 120 caracteres

---

## ❗ Tratamento de Erros

### Formato de Resposta de Erro

```json
{
  "status": 400,
  "timestamp": "2025-11-04T08:45:00Z",
  "type": "https://userproject.com/validation-error",
  "title": "Validation Error",
  "detail": "Invalid request content.",
  "userMessage": "Um ou mais campos estão inválidos",
  "fields": [
    {
      "name": "email",
      "userMessage": "deve ser um endereço de e-mail bem formado"
    }
  ]
}
```

### Códigos de Status HTTP

- **200** OK - Sucesso
- **201** Created - Recurso criado
- **204** No Content - Sucesso sem corpo de resposta
- **400** Bad Request - Erro de validação
- **404** Not Found - Recurso não encontrado
- **500** Internal Server Error - Erro do servidor

---

## 🔧 Configuração de Profiles

O projeto usa apenas PostgreSQL em todos os ambientes.

### dev (padrão)
- PostgreSQL local (localhost:5432)
- Connection pooling configurado
- Logs detalhados

### test
- PostgreSQL de teste (localhost:5433) ou mesma instância
- Flyway clean habilitado para testes
- Logs de debug

### Alterar configurações

```bash
# Via linha de comando
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Via variável de ambiente
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/userproject_db"
./mvnw spring-boot:run

# Via Docker Compose
# Já configurado no docker-compose.yml
```

---

## 📝 Logs

### Configuração de Logs (logback-spring.xml)

- **Console**: Logs em tempo real
- **File**: Arquivo rotativo `application.log`
- **Rotação**: Diária, mantém 30 dias

### Localização dos Logs

```
application.log              # Log atual
application.log.2025-11-04.gz  # Logs comprimidos por dia
```

### Níveis de Log

```properties
com.user.project=DEBUG    # Aplicação
org.springframework=INFO  # Spring Framework
org.flywaydb=INFO        # Flyway migrations
```

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto é um projeto de demonstração para fins educacionais e de entrevista.

---

## 👨‍💻 Autor

Desenvolvido como projeto de entrevista técnica.

---

## 📞 Suporte

Para dúvidas ou problemas:
1. Verifique a seção de [Troubleshooting](#-troubleshooting) no README-DOCKER.md
2. Consulte os logs: `docker-compose logs -f`
3. Verifique o health check: `curl http://localhost:8080/actuator/health`

---

## ✅ Checklist de Requisitos Implementados

### Requisitos Mínimos
- [x] Criar usuário (POST /users)
- [x] Buscar informações do usuário (GET /users/{id})
- [x] Deletar usuário (DELETE /users/{id})
- [x] Adicionar projeto externo ao usuário (POST /users/{user-id}/projects)
- [x] Buscar projetos externos do usuário (GET /users/{user-id}/projects)
- [x] Testes unitários (Controllers e Use Cases)
- [x] Containerização com Docker

### Requisitos Opcionais
- [x] Atualizar informações do usuário (PUT /users/{id})
- [x] Configurar logs (Logback com rotação)
- [x] Configurar métricas (Actuator + Prometheus)
- [x] Docker Compose com banco de dados e serviço
- [x] Flyway para versionamento de scripts
- [x] Dados de exemplo pré-carregados
- [x] Documentação completa

---

**🎯 Status do Projeto: Pronto para Produção!**
