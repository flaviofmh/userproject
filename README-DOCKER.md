# 🐳 Docker Compose Setup Guide

## Pré-requisitos

- Docker 20.10+
- Docker Compose 2.0+

## 🚀 Como executar

### 1. Subir toda a infraestrutura (PostgreSQL + Aplicação)

```bash
docker-compose up -d
```

### 2. Verificar os logs

```bash
# Ver logs de todos os serviços
docker-compose logs -f

# Ver logs apenas da aplicação
docker-compose logs -f app

# Ver logs apenas do PostgreSQL
docker-compose logs -f postgres
```

### 3. Verificar status dos containers

```bash
docker-compose ps
```

### 4. Parar os serviços

```bash
docker-compose down
```

### 5. Parar e remover volumes (limpar dados)

```bash
docker-compose down -v
```

## 📡 Endpoints disponíveis

### Aplicação
- **API Base**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **Prometheus**: http://localhost:8080/actuator/prometheus
- **Flyway Info**: http://localhost:8080/actuator/flyway

### PostgreSQL
- **Host**: localhost
- **Port**: 5432
- **Database**: userproject_db
- **Username**: userproject
- **Password**: userproject123

## 🗃️ Conectar ao PostgreSQL

### Via psql (linha de comando)

```bash
docker-compose exec postgres psql -U userproject -d userproject_db
```

### Via ferramenta GUI (DBeaver, pgAdmin, etc)

```
Host: localhost
Port: 5432
Database: userproject_db
Username: userproject
Password: userproject123
```

## 📊 Flyway Migrations

As migrations do Flyway são executadas automaticamente na inicialização da aplicação.

### Verificar status das migrations

```bash
curl http://localhost:8080/actuator/flyway
```

### Arquivos de migration

- `V1__create_user_table.sql` - Cria tabela de usuários
- `V2__create_user_external_project_table.sql` - Cria tabela de projetos externos
- `V3__insert_sample_data.sql` - Insere dados de exemplo

## 🔧 Comandos úteis

### Rebuild da aplicação

```bash
docker-compose up -d --build
```

### Executar apenas o PostgreSQL

```bash
docker-compose up -d postgres
```

### Acessar shell do container da aplicação

```bash
docker-compose exec app sh
```

### Ver uso de recursos

```bash
docker stats
```

## 🐛 Troubleshooting

### Aplicação não conecta no banco

1. Verificar se o PostgreSQL está saudável:
```bash
docker-compose ps
```

2. Verificar logs do PostgreSQL:
```bash
docker-compose logs postgres
```

3. Reiniciar os serviços:
```bash
docker-compose restart
```

### Porta já em uso

Se a porta 8080 ou 5432 já estiver em uso, edite o `docker-compose.yml`:

```yaml
# Para aplicação
ports:
  - "8081:8080"

# Para PostgreSQL
ports:
  - "5433:5432"
```

E ajuste a URL no `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/userproject_db
```

### Limpar tudo e recomeçar

```bash
docker-compose down -v
docker-compose up -d --build
```

## 📝 Configuração

O projeto usa **PostgreSQL em todos os ambientes** (dev, prod, test).

Para rodar localmente:

```bash
# Subir apenas o PostgreSQL
docker-compose up -d postgres

# Rodar a aplicação localmente
./mvnw spring-boot:run
```

## 🎯 Testando a API

### Criar usuário

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "name": "Test User"
  }'
```

### Buscar usuário

```bash
curl http://localhost:8080/users/1
```

### Adicionar projeto ao usuário

```bash
curl -X POST http://localhost:8080/users/1/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Awesome Project"
  }'
```

### Listar projetos do usuário

```bash
curl http://localhost:8080/users/1/projects
```
