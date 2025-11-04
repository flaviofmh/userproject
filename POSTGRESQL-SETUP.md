# 🐘 PostgreSQL Local Setup Guide

## Opções para Desenvolvimento Local

Existem 3 formas de rodar o PostgreSQL localmente:

---

## 🎯 Opção 1: Docker Compose (Recomendado)

### Subir apenas o PostgreSQL
```bash
docker-compose up -d postgres
```

### Rodar a aplicação localmente apontando para o PostgreSQL do Docker
```bash
./mvnw spring-boot:run
```

A aplicação irá conectar automaticamente em `localhost:5432`.

---

## 🎯 Opção 2: PostgreSQL Instalado Localmente

Se você tem PostgreSQL instalado no seu computador:

### 1. Criar o banco de dados
```sql
CREATE DATABASE userproject_db;
CREATE USER userproject WITH PASSWORD 'userproject123';
GRANT ALL PRIVILEGES ON DATABASE userproject_db TO userproject;
```

### 2. Rodar a aplicação
```bash
./mvnw spring-boot:run
```

---

## 🎯 Opção 3: Ambiente Completo com Docker Compose

```bash
# Subir PostgreSQL + Aplicação
docker-compose up -d

# Ver logs
docker-compose logs -f app
```

---

## 🧪 Para Testes

Os testes usam a mesma instância PostgreSQL que o desenvolvimento (porta 5432).

```bash
# Subir PostgreSQL
docker-compose up -d postgres

# Rodar testes
./mvnw test
```

**Nota:** Os testes compartilham o mesmo banco de dados. O Flyway gerencia o schema automaticamente.

---

## 🔧 Variáveis de Ambiente

Você pode customizar a conexão com variáveis de ambiente:

```bash
# Windows (PowerShell)
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/userproject_db"
$env:SPRING_DATASOURCE_USERNAME="userproject"
$env:SPRING_DATASOURCE_PASSWORD="userproject123"
./mvnw spring-boot:run

# Linux/Mac
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/userproject_db"
export SPRING_DATASOURCE_USERNAME="userproject"
export SPRING_DATASOURCE_PASSWORD="userproject123"
./mvnw spring-boot:run
```

---

## 🗃️ Conectar ao PostgreSQL

### Via Docker Compose
```bash
docker-compose exec postgres psql -U userproject -d userproject_db
```

### Via psql Local
```bash
psql -h localhost -p 5432 -U userproject -d userproject_db
```

### Via DBeaver, pgAdmin, etc
```
Host: localhost
Port: 5432
Database: userproject_db
Username: userproject
Password: userproject123
```

---

## 📊 Verificar Migrations

```bash
# Via API
curl http://localhost:8080/actuator/flyway

# Via SQL
docker-compose exec postgres psql -U userproject -d userproject_db -c "SELECT * FROM flyway_schema_history;"
```

---

## 🔄 Reset do Banco de Dados

### Com Docker Compose
```bash
# Parar e remover volumes (apaga todos os dados)
docker-compose down -v

# Subir novamente (banco limpo)
docker-compose up -d
```

O Flyway irá criar as tabelas automaticamente. O banco ficará vazio, pronto para uso.

---

## 🚨 Troubleshooting

### PostgreSQL não está acessível
```bash
# Verificar se está rodando
docker-compose ps

# Verificar logs
docker-compose logs postgres

# Testar conexão
docker-compose exec postgres pg_isready -U userproject
```

### Erro "database does not exist"
```bash
# Acessar PostgreSQL e criar manualmente
docker-compose exec postgres psql -U userproject -d postgres
postgres=# CREATE DATABASE userproject_db;
```

### Erro de senha
Verifique as credenciais em `application.properties` ou variáveis de ambiente.

### Porta 5432 já em uso
Se você tem PostgreSQL instalado localmente, pode:
1. Parar o PostgreSQL local
2. Ou mudar a porta no `docker-compose.yml`:
```yaml
ports:
  - "5433:5432"  # Mapeia porta 5433 do host para 5432 do container
```

E ajustar a URL de conexão:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/userproject_db
```

---

## ⚡ Quick Commands

```bash
# Iniciar PostgreSQL
docker-compose up -d postgres

# Parar PostgreSQL
docker-compose stop postgres

# Ver logs do PostgreSQL
docker-compose logs -f postgres

# Backup do banco
docker-compose exec postgres pg_dump -U userproject userproject_db > backup.sql

# Restaurar backup
cat backup.sql | docker-compose exec -T postgres psql -U userproject -d userproject_db
```
