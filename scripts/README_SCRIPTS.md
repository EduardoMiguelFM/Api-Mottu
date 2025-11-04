# 📜 Documentação dos Scripts - Mottu API

Este documento explica a função de cada script disponível no projeto.

---

## 🚀 Scripts de Deploy para Azure

### 1. `deploy-azure-cloud-shell.sh` ⭐ **PRINCIPAL**

**O que faz:**

- Script completo e automatizado para fazer deploy no Azure Cloud Shell
- Executa TODOS os passos necessários de uma vez só

**Quando usar:**

- Quando você quer fazer deploy completo pela primeira vez
- Quando quer automatizar tudo sem precisar executar múltiplos comandos
- **Recomendado para Azure Cloud Shell**

**O que ele faz:**

1. ✅ Cria Resource Group no Azure
2. ✅ Cria PostgreSQL Flexible Server
3. ✅ Cria Database no PostgreSQL
4. ✅ Configura Firewall do PostgreSQL
5. ✅ Cria App Service Plan
6. ✅ Cria Web App (App Service) com Java 21
7. ✅ Configura Connection String do PostgreSQL
8. ✅ Configura variáveis de ambiente (DB_USERNAME, DB_PASSWORD, etc.)
9. ✅ Faz build da aplicação (compila o código)
10. ✅ Faz deploy do JAR para o Azure

**Como usar:**

```bash
chmod +x scripts/deploy-azure-cloud-shell.sh
./scripts/deploy-azure-cloud-shell.sh
```

---

### 2. `deploy-azure.sh` / `deploy-azure.bat`

**O que faz:**

- Cria apenas os recursos Azure (não faz build nem deploy do código)
- Versão `.sh` para Linux/Mac/Cloud Shell
- Versão `.bat` para Windows

**Quando usar:**

- Quando você quer criar os recursos Azure manualmente
- Quando você prefere fazer build e deploy em etapas separadas
- Para criar recursos em diferentes ambientes

**O que ele faz:**

1. ✅ Cria Resource Group
2. ✅ Cria PostgreSQL Flexible Server
3. ✅ Cria Database
4. ✅ Configura Firewall
5. ✅ Cria App Service Plan
6. ✅ Cria Web App
7. ✅ Configura Connection String
8. ✅ Configura variáveis de ambiente

**O que ele NÃO faz:**

- ❌ Não compila o código
- ❌ Não faz deploy do JAR

**Como usar:**

```bash
# Linux/Mac/Cloud Shell
./scripts/deploy-azure.sh

# Windows
scripts\deploy-azure.bat
```

---

### 3. `deploy-jar.sh` / `deploy-jar.bat`

**O que faz:**

- Faz deploy apenas do JAR já compilado para o Azure App Service
- Versão `.sh` para Linux/Mac/Cloud Shell
- Versão `.bat` para Windows

**Quando usar:**

- Quando você já compilou a aplicação (`build/libs/mottu-api-0.0.1-SNAPSHOT.jar` existe)
- Quando você quer fazer deploy de uma nova versão sem recriar os recursos
- Após executar `build.sh` ou `build.bat`

**O que ele faz:**

1. ✅ Verifica se o JAR existe
2. ✅ Faz upload do JAR para o Azure App Service
3. ✅ Inicia a aplicação

**Pré-requisitos:**

- JAR já compilado em `build/libs/mottu-api-0.0.1-SNAPSHOT.jar`
- Recursos Azure já criados (via `deploy-azure.sh`)

**Como usar:**

```bash
# Linux/Mac/Cloud Shell
./scripts/deploy-jar.sh

# Windows
scripts\deploy-jar.bat
```

---

## 🔨 Scripts de Build

### 4. `build.sh` / `build.bat`

**O que faz:**

- Compila o código Java usando Gradle
- Gera o arquivo JAR executável
- Versão `.sh` para Linux/Mac/Cloud Shell
- Versão `.bat` para Windows

**Quando usar:**

- Quando você fez alterações no código e quer compilar
- Antes de fazer deploy (se não usar `deploy-azure-cloud-shell.sh`)
- Para testar se o código compila sem erros

**O que ele faz:**

1. ✅ Limpa build anterior (`clean`)
2. ✅ Compila o código (`build`)
3. ✅ Gera o JAR em `build/libs/mottu-api-0.0.1-SNAPSHOT.jar`

**Como usar:**

```bash
# Linux/Mac/Cloud Shell
./scripts/build.sh

# Windows
scripts\build.bat
```

---

## 🔍 Scripts de Verificação

### 5. `check-deploy-status.sh`

**O que faz:**

- Verifica o status do deploy no Azure
- Testa se a aplicação está respondendo
- Mostra informações sobre os recursos criados

**Quando usar:**

- Após fazer deploy para verificar se tudo está funcionando
- Quando você quer diagnosticar problemas
- Para verificar configurações do App Service

**O que ele faz:**

1. ✅ Verifica se Resource Group existe
2. ✅ Verifica status do App Service
3. ✅ Verifica servidores PostgreSQL
4. ✅ Mostra configurações (Java version, variáveis de ambiente)
5. ✅ Testa se a API está respondendo
6. ✅ Testa se o Swagger está acessível

**Como usar:**

```bash
chmod +x scripts/check-deploy-status.sh
./scripts/check-deploy-status.sh
```

---

## 🧪 Scripts de Teste

### 6. `test-api.sh` / `test-api.bat`

**O que faz:**

- Executa testes automatizados da API
- Testa endpoints principais
- Verifica se a aplicação está funcionando corretamente

**Quando usar:**

- Após fazer deploy para validar que tudo funciona
- Para testar mudanças na API
- Antes de considerar o deploy como concluído

**Pré-requisitos:**

- Aplicação rodando (localmente ou no Azure)
- URL da aplicação configurada

**Como usar:**

```bash
# Linux/Mac/Cloud Shell
./scripts/test-api.sh

# Windows
scripts\test-api.bat
```

---

## 📊 Resumo por Fluxo de Trabalho

### 🎯 Fluxo 1: Deploy Completo Automatizado (Recomendado)

```bash
# 1. Execute o script completo (faz tudo)
./scripts/deploy-azure-cloud-shell.sh

# 2. Verifique o status
./scripts/check-deploy-status.sh
```

**Resultado:** Aplicação rodando no Azure com PostgreSQL

---

### 🎯 Fluxo 2: Deploy Manual em Etapas

```bash
# 1. Criar recursos Azure
./scripts/deploy-azure.sh

# 2. Compilar aplicação
./scripts/build.sh

# 3. Fazer deploy do JAR
./scripts/deploy-jar.sh

# 4. Verificar status
./scripts/check-deploy-status.sh
```

**Resultado:** Mesmo resultado, mas com mais controle sobre cada etapa

---

### 🎯 Fluxo 3: Atualizar Aplicação (Re-deploy)

```bash
# 1. Fazer alterações no código
# 2. Compilar novamente
./scripts/build.sh

# 3. Fazer deploy da nova versão
./scripts/deploy-jar.sh

# 4. Verificar se está funcionando
./scripts/check-deploy-status.sh
```

**Resultado:** Nova versão da aplicação no Azure

---

## 📋 Tabela Comparativa

| Script                        | Cria Recursos Azure | Compila Código | Faz Deploy | Verifica Status |
| ----------------------------- | ------------------- | -------------- | ---------- | --------------- |
| `deploy-azure-cloud-shell.sh` | ✅                  | ✅             | ✅         | ❌              |
| `deploy-azure.sh`             | ✅                  | ❌             | ❌         | ❌              |
| `build.sh`                    | ❌                  | ✅             | ❌         | ❌              |
| `deploy-jar.sh`               | ❌                  | ❌             | ✅         | ❌              |
| `check-deploy-status.sh`      | ❌                  | ❌             | ❌         | ✅              |

---

## 💡 Dicas de Uso

### Para Iniciantes (Primeira vez):

```bash
# Use o script completo - é mais fácil!
./scripts/deploy-azure-cloud-shell.sh
```

### Para Desenvolvedores (Atualizações frequentes):

```bash
# Faça mudanças no código, depois:
./scripts/build.sh
./scripts/deploy-jar.sh
```

### Para Troubleshooting:

```bash
# Verifique o que está acontecendo:
./scripts/check-deploy-status.sh

# Veja os logs:
az webapp log tail --name mottu-api-fiap --resource-group MottuRG
```

---

## ⚠️ Observações Importantes

1. **Scripts `.sh`**: Funcionam no Linux, Mac e Azure Cloud Shell
2. **Scripts `.bat`**: Funcionam apenas no Windows
3. **Azure Cloud Shell**: Use sempre os scripts `.sh`
4. **Permissões**: No Linux/Mac, execute `chmod +x scripts/*.sh` antes
5. **Autenticação**: Execute `az login` antes de usar scripts de deploy

---

## 🆘 Problemas Comuns

### Script não executa (permissão negada)

```bash
chmod +x scripts/nome-do-script.sh
```

### Erro "az: command not found"

- Instale Azure CLI ou use Azure Cloud Shell

### Erro "JAR não encontrado"

- Execute `./scripts/build.sh` primeiro

### Erro "Resource Group não encontrado"

- Execute `./scripts/deploy-azure.sh` primeiro

---

**Última atualização**: 2025 - Entrega Final FIAP
