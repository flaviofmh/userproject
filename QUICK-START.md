# 🚀 Quick Start Guide

## Executar com Docker Compose (Recomendado)

```bash
# 1. Build e start
docker-compose up -d

# 2. Verificar se está rodando
docker-compose ps

# 3. Testar a API
curl http://localhost:8080/actuator/health
```

## Testar a API

### Criar um usuário
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","name":"Test User"}'
```

### Buscar usuário
```bash
curl http://localhost:8080/users/1
```

### Adicionar projeto
```bash
curl -X POST http://localhost:8080/users/1/projects \
  -H "Content-Type: application/json" \
  -d '{"name":"My Project"}'
```

### Listar projetos
```bash
curl http://localhost:8080/users/1/projects
```

## Conectar ao PostgreSQL

```bash
docker-compose exec postgres psql -U userproject -d userproject_db
```

## Parar tudo

```bash
docker-compose down
```

## Mais informações

Consulte `README.md` para documentação completa.
