# 🔧 Como Resetar o Banco de Dados

## ⚠️ Problema: Flyway já executou V3__insert_sample_data.sql

Se você já executou a aplicação antes de remover o `V3__insert_sample_data.sql`, o Flyway registrou essa migration na tabela `flyway_schema_history`.

Ao remover o arquivo, o Flyway irá reclamar que a migration está faltando.

---

## ✅ Solução: Resetar o Volume do Docker

### Opção 1: Reset Completo (Recomendado)

```bash
# Parar containers
docker-compose down

# Remover volumes (APAGA TODOS OS DADOS)
docker-compose down -v

# Subir novamente
docker-compose up -d
```

O Flyway irá executar apenas V1 e V2, e o banco ficará limpo.

---

### Opção 2: Remover manualmente o registro do Flyway

Se você não quer perder os dados, pode remover apenas o registro da V3:

```bash
# Conectar ao PostgreSQL
docker-compose exec postgres psql -U userproject -d userproject_db

# Remover o registro da V3
DELETE FROM flyway_schema_history WHERE version = '3';

# Sair
\q
```

---

### Opção 3: Criar Migration de Limpeza (Não Recomendado)

Se você quiser manter o histórico, pode criar uma V4 que remove os dados:

```sql
-- V4__remove_sample_data.sql
DELETE FROM tb_user_external_project WHERE user_id IN (1, 2, 3);
DELETE FROM tb_user WHERE id IN (1, 2, 3);
```

**⚠️ Não recomendamos essa abordagem.** É melhor resetar.

---

## 🎯 Qual escolher?

| Situação | Solução |
|----------|---------|
| **Desenvolvimento local** | Opção 1 (Reset completo) ✅ |
| **Já tem dados importantes** | Opção 2 (Remover registro) |
| **Produção com dados** | ⚠️ CUIDADO! Planejar migration |

---

## ✅ Verificar Status do Flyway

Após o reset, verifique que apenas V1 e V2 foram executadas:

```bash
# Via API
curl http://localhost:8080/actuator/flyway

# Via SQL
docker-compose exec postgres psql -U userproject -d userproject_db -c "SELECT * FROM flyway_schema_history ORDER BY installed_rank;"
```

Você deve ver apenas:
```
version | description
--------|------------------------------------
1       | create user table
2       | create user external project table
```

---

## 🚀 Testar que está funcionando

```bash
# Criar um usuário via API
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"pass123","name":"Test User"}'

# Verificar que foi criado
curl http://localhost:8080/users/1
```

---

**✅ Pronto! Banco resetado e funcionando apenas com as migrations estruturais.**
