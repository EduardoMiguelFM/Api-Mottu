# 🚀 Guia de Deploy no Azure - MotoVision API

Este guia explica como fazer o deploy completo da aplicação MotoVision API no Azure usando Azure Cloud Shell.

## 📋 Pré-requisitos

1. **Conta Azure ativa** com permissões para criar recursos
2. **Azure CLI instalado e configurado** (já disponível no Cloud Shell)
3. **Acesso ao Azure Portal** ou Cloud Shell

## 🎯 Opções de Deploy

### Opção 1: Deploy Completo Automatizado (Recomendado)

Use o script completo que automatiza todo o processo:

```bash
# 1. Acessar Azure Cloud Shell (portal.azure.com → Cloud Shell)
# 2. Fazer upload do projeto ou clonar do repositório
git clone <seu-repositorio>
cd Api-Mottu

# 3. Tornar script executável
chmod +x scripts/deploy-azure-cloud-shell.sh

# 4. Executar deploy completo
./scripts/deploy-azure-cloud-shell.sh
```

O script faz automaticamente:

- ✅ Criação do Resource Group
- ✅ Criação do PostgreSQL Flexible Server
- ✅ Criação do Database
- ✅ Configuração do Firewall
- ✅ Criação do App Service Plan
- ✅ Criação do Web App (Java 21)
- ✅ Configuração de Connection Strings
- ✅ Configuração de Variáveis de Ambiente
- ✅ Build da aplicação
- ✅ Deploy do JAR

### Opção 2: Deploy Manual por Etapas

Se preferir fazer manualmente:

#### 1. Criar Recursos Azure

```bash
# Fazer login no Azure
az login

# Executar script de criação de recursos
./scripts/deploy-azure.sh
```

#### 2. Build da Aplicação

```bash
# Build do projeto
./gradlew clean build -x test
```

#### 3. Deploy do JAR

```bash
# Deploy para App Service
./scripts/deploy-jar.sh
```

## 🔧 Configuração Manual

Se precisar configurar manualmente:

### 1. Criar Resource Group

```bash
az group create --name MotoVisionRG --location eastus
```

### 2. Criar PostgreSQL Flexible Server

```bash
az postgres flexible-server create \
  --resource-group MotoVisionRG \
  --name motovision-postgres-server \
  --location eastus \
  --admin-user motovisionadmin \
  --admin-password "MotoVisionPass123!Secure@2025" \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --storage-size 32 \
  --version 14 \
  --public-access 0.0.0.0
```

### 3. Criar Database

```bash
az postgres flexible-server db create \
  --resource-group MotoVisionRG \
  --server-name motovision-postgres-server \
  --database-name motovisiondb
```

### 4. Configurar Firewall

```bash
az postgres flexible-server firewall-rule create \
  --resource-group MotoVisionRG \
  --name motovision-postgres-server \
  --rule-name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

### 5. Criar App Service Plan

```bash
az appservice plan create \
  --name MotoVisionAppServicePlan \
  --resource-group MotoVisionRG \
  --location eastus \
  --sku B1 \
  --is-linux
```

### 6. Criar Web App

```bash
az webapp create \
  --resource-group MotoVisionRG \
  --plan MotoVisionAppServicePlan \
  --name motovision-api \
  --runtime "JAVA:21-java21"

# Configurar Java 21 explicitamente
az webapp config set \
  --resource-group MotoVisionRG \
  --name motovision-api \
  --java-version "21" \
  --java-container "JAVA" \
  --java-container-version "21"
```

### 7. Configurar Connection String

```bash
az webapp config connection-string set \
  --resource-group MotoVisionRG \
  --name motovision-api \
  --connection-string-type PostgreSQL \
  --settings POSTGRESQLCONNSTR_DefaultConnection="jdbc:postgresql://motovision-postgres-server.postgres.database.azure.com:5432/motovisiondb?sslmode=require"
```

### 8. Configurar Variáveis de Ambiente

```bash
az webapp config appsettings set \
  --resource-group MotoVisionRG \
  --name motovision-api \
  --settings \
    DB_USERNAME="motovisionadmin" \
    DB_PASSWORD="MotoVisionPass123!Secure@2025" \
    SPRING_PROFILES_ACTIVE="cloud" \
    JAVA_OPTS="-Xms512m -Xmx1024m"
```

### 9. Deploy do JAR

```bash
az webapp deploy \
  --resource-group MotoVisionRG \
  --name motovision-api \
  --src-path build/libs/mottu-api-0.0.1-SNAPSHOT.jar \
  --type jar
```

## ✅ Verificação Pós-Deploy

### 1. Verificar Status da Aplicação

```bash
az webapp show \
  --name motovision-api \
  --resource-group MotoVisionRG \
  --query state
```

### 2. Ver Logs

```bash
az webapp log tail \
  --name motovision-api \
  --resource-group MotoVisionRG
```

### 3. Testar Endpoints

Após o deploy, aguarde 2-3 minutos e teste:

- **API Principal**: `https://motovision-api.azurewebsites.net`
- **Swagger UI**: `https://motovision-api.azurewebsites.net/swagger-ui.html`
- **Login**: `https://motovision-api.azurewebsites.net/login`

### 4. Verificar Conexão com Banco

```bash
# Verificar logs para confirmar conexão com PostgreSQL
az webapp log tail --name motovision-api --resource-group MotoVisionRG | grep -i postgresql
```

## 🔍 Troubleshooting

### Problema: Aplicação não inicia

**Solução:**

1. Verificar logs: `az webapp log tail --name motovision-api --resource-group MotoVisionRG`
2. Verificar se o Java 21 está configurado: `az webapp config show --name motovision-api --resource-group MotoVisionRG`
3. Verificar variáveis de ambiente: `az webapp config appsettings list --name motovision-api --resource-group MotoVisionRG`

### Problema: Erro de conexão com PostgreSQL

**Solução:**

1. Verificar firewall do PostgreSQL: `az postgres flexible-server firewall-rule list --resource-group MotoVisionRG --name motovision-postgres-server`
2. Verificar connection string: `az webapp config connection-string list --name motovision-api --resource-group MotoVisionRG`
3. Testar conexão do banco: `az postgres flexible-server db show --resource-group MotoVisionRG --server-name motovision-postgres-server --database-name motovisiondb`

### Problema: Erro 500 ou aplicação não responde

**Solução:**

1. Verificar se o profile "cloud" está ativo
2. Verificar logs de erro do Spring Boot
3. Verificar se as migrações Flyway foram executadas
4. Reiniciar o App Service: `az webapp restart --name motovision-api --resource-group MotoVisionRG`

## 📊 Recursos Criados

Após o deploy, os seguintes recursos serão criados no Azure:

- **Resource Group**: `MotoVisionRG`
- **PostgreSQL Flexible Server**: `motovision-postgres-server`
- **Database**: `motovisiondb`
- **App Service Plan**: `MotoVisionAppServicePlan` (B1 - Linux)
- **Web App**: `motovision-api`

## 💰 Custos Estimados

- **PostgreSQL Flexible Server (B1ms)**: ~$15-20/mês
- **App Service Plan (B1)**: ~$13-15/mês
- **Total estimado**: ~$28-35/mês

> **Nota**: Valores podem variar conforme região e uso. Recomenda-se usar o Azure Pricing Calculator para estimativas precisas.

## 🔐 Segurança

### Boas Práticas Implementadas

1. ✅ PostgreSQL com SSL obrigatório (`sslmode=require`)
2. ✅ Firewall configurado para permitir apenas Azure Services
3. ✅ Senhas fortes configuradas
4. ✅ Variáveis sensíveis em App Settings (não no código)
5. ✅ Spring Security configurado

### Recomendações Adicionais

- Use Azure Key Vault para armazenar senhas em produção
- Configure Application Insights para monitoramento
- Configure backup automático do PostgreSQL
- Use HTTPS obrigatório no App Service

## 📝 Notas Importantes

1. **Java 21**: A aplicação requer Java 21. Certifique-se de que o App Service está configurado corretamente.

2. **PostgreSQL**: A aplicação usa PostgreSQL Flexible Server. Não há suporte para H2 em produção.

3. **Profile Cloud**: O profile `cloud` deve estar ativo no App Service (`SPRING_PROFILES_ACTIVE=cloud`).

4. **Flyway**: As migrações são executadas automaticamente na primeira inicialização.

5. **Tempo de Inicialização**: A aplicação pode levar 2-3 minutos para inicializar completamente após o deploy.

## 🆘 Suporte

Em caso de problemas:

1. Verifique os logs do App Service
2. Verifique a documentação do Azure
3. Consulte o README.md do projeto
4. Verifique os scripts de deploy em `scripts/`

## 📚 Referências

- [Azure App Service Documentation](https://docs.microsoft.com/azure/app-service/)
- [Azure Database for PostgreSQL](https://docs.microsoft.com/azure/postgresql/)
- [Spring Boot on Azure](https://docs.microsoft.com/azure/developer/java/spring-framework/)
- [Azure CLI Reference](https://docs.microsoft.com/cli/azure/)

---

**Última atualização**: 2025 - Entrega Final FIAP
