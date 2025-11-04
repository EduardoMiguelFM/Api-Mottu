# 📋 Análise de Scripts - Quais Manter?

## ✅ Scripts Essenciais (MANTER)

### Deploy Principal

- `deploy-azure-cloud-shell.sh` ⭐ **PRINCIPAL** - Deploy completo automatizado
- `deploy-azure.sh` / `deploy-azure.bat` - Criar recursos Azure (usado no README)
- `build.sh` / `build.bat` - Build da aplicação (usado no README)
- `deploy-jar.sh` / `deploy-jar.bat` - Deploy do JAR (usado no README)

### Utilitários

- `delete-azure-resources.sh` / `delete-azure-resources.bat` - Limpar recursos (útil)

---

## ❌ Scripts Duplicados/Redundantes (PODE EXCLUIR)

### Duplicados

- `deploy-jar-only.sh` ❌ - **DUPLICADO** de `deploy-jar.sh` (mesma função)
- `deploy-only-build.sh` ❌ - **REDUNDANTE** - função já está no `deploy-azure-cloud-shell.sh`

**Motivo:** O `deploy-azure-cloud-shell.sh` já detecta recursos existentes e faz build+deploy.

---

## ⚠️ Scripts Opcionais (Decidir se mantém)

### Testes

- `test-all-entities.sh` / `test-all-entities.bat` ⚠️ - Testes completos (útil mas não essencial)
- `test-api.sh` / `test-api.bat` ⚠️ - Testes básicos (útil mas não essencial)

### Verificação

- `check-deploy-status.sh` ⚠️ - Verificar status do deploy (útil mas não essencial)

### Outros

- `script_bd.sql` - Script SQL manual (pode manter como referência)
- `README_SCRIPTS.md` - Documentação (útil manter)

---

## 🗑️ Recomendação de Exclusão

### Excluir com certeza:

1. ✅ `deploy-jar-only.sh` - Duplicado
2. ✅ `deploy-only-build.sh` - Redundante

### Manter por enquanto (podem ser úteis):

- Scripts de teste (podem ser úteis para validação)
- Scripts de verificação (podem ajudar no troubleshooting)

---

## 📊 Resumo

| Script                          | Status        | Ação    |
| ------------------------------- | ------------- | ------- |
| `deploy-azure-cloud-shell.sh`   | ✅ Essencial  | MANTER  |
| `deploy-azure.sh/bat`           | ✅ Essencial  | MANTER  |
| `build.sh/bat`                  | ✅ Essencial  | MANTER  |
| `deploy-jar.sh/bat`             | ✅ Essencial  | MANTER  |
| `delete-azure-resources.sh/bat` | ✅ Útil       | MANTER  |
| `deploy-jar-only.sh`            | ❌ Duplicado  | EXCLUIR |
| `deploy-only-build.sh`          | ❌ Redundante | EXCLUIR |
| `test-all-entities.sh/bat`      | ⚠️ Opcional   | DECIDIR |
| `test-api.sh/bat`               | ⚠️ Opcional   | DECIDIR |
| `check-deploy-status.sh`        | ⚠️ Opcional   | DECIDIR |

---

**Última atualização**: 2025 - Entrega Final FIAP
