#!/bin/bash

# ===============================================
# Script Completo de Deploy para Azure Cloud Shell
# MotoVision API - Challenge FIAP 2025 - Entrega Final
# ===============================================
# Este script realiza o deploy completo da aplicação:
# 1. Cria todos os recursos Azure necessários
# 2. Configura PostgreSQL Flexible Server
# 3. Configura App Service com Java 21
# 4. Faz build da aplicação
# 5. Faz deploy do JAR
# ===============================================

set -e  # Parar em caso de erro

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Variáveis de configuração
RESOURCE_GROUP="MotoVisionRG"
LOCATION="eastus"  # Mudado para formato correto (sem espaço)
POSTGRES_SERVER="motovision-postgres-$(date +%s | tail -c 5)"  # Nome único com timestamp
POSTGRES_DB="motovisiondb"
POSTGRES_USER="motovisionadmin"
POSTGRES_PASSWORD="MotoVisionPass123!Secure@2025"  # Senha mais segura
APP_NAME="motovision-api-$(date +%s | tail -c 5)"  # Nome único
APP_PLAN="MotoVisionAppServicePlan"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  DEPLOY COMPLETO - MOTOVISION API${NC}"
echo -e "${BLUE}  Azure Cloud Shell${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Verificar se Azure CLI está instalado e logado
echo -e "${YELLOW}Verificando autenticação Azure...${NC}"
if ! az account show &> /dev/null; then
    echo -e "${RED}❌ Não autenticado no Azure. Execute: az login${NC}"
    exit 1
fi

ACCOUNT=$(az account show --query name -o tsv)
echo -e "${GREEN}✅ Conectado ao Azure: ${ACCOUNT}${NC}"
echo ""

# Confirmar criação de recursos
echo -e "${YELLOW}Configurações do Deploy:${NC}"
echo "  Resource Group: $RESOURCE_GROUP"
echo "  Location: $LOCATION"
echo "  PostgreSQL Server: $POSTGRES_SERVER"
echo "  Database: $POSTGRES_DB"
echo "  App Service: $APP_NAME"
echo ""
read -p "Deseja continuar? (s/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Ss]$ ]]; then
    echo "Deploy cancelado."
    exit 1
fi

echo ""
echo -e "${BLUE}=== INICIANDO DEPLOY ===${NC}"
echo ""

# ==========================================
# 1. CRIAR GRUPO DE RECURSOS
# ==========================================
echo -e "${YELLOW}[1/9] Criando grupo de recursos...${NC}"
if az group show --name $RESOURCE_GROUP &> /dev/null; then
    echo -e "${GREEN}✅ Grupo de recursos já existe.${NC}"
else
    az group create --name $RESOURCE_GROUP --location "$LOCATION" --output none
    echo -e "${GREEN}✅ Grupo de recursos criado.${NC}"
fi
echo ""

# ==========================================
# 2. CRIAR POSTGRESQL FLEXIBLE SERVER
# ==========================================
echo -e "${YELLOW}[2/9] Criando servidor PostgreSQL Flexible Server...${NC}"
az postgres flexible-server create \
  --resource-group $RESOURCE_GROUP \
  --name $POSTGRES_SERVER \
  --location "$LOCATION" \
  --admin-user $POSTGRES_USER \
  --admin-password $POSTGRES_PASSWORD \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --storage-size 32 \
  --version 14 \
  --public-access 0.0.0.0 \
  --output none

echo -e "${GREEN}✅ Servidor PostgreSQL criado: $POSTGRES_SERVER.postgres.database.azure.com${NC}"
echo ""

# ==========================================
# 3. CRIAR DATABASE
# ==========================================
echo -e "${YELLOW}[3/9] Criando database...${NC}"
az postgres flexible-server db create \
  --resource-group $RESOURCE_GROUP \
  --server-name $POSTGRES_SERVER \
  --database-name $POSTGRES_DB \
  --output none

echo -e "${GREEN}✅ Database '$POSTGRES_DB' criado.${NC}"
echo ""

# ==========================================
# 4. CONFIGURAR FIREWALL (Permitir Azure Services)
# ==========================================
echo -e "${YELLOW}[4/9] Configurando firewall para permitir Azure Services...${NC}"
az postgres flexible-server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --name $POSTGRES_SERVER \
  --rule-name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0 \
  --output none

echo -e "${GREEN}✅ Firewall configurado.${NC}"
echo ""

# ==========================================
# 5. CRIAR APP SERVICE PLAN
# ==========================================
echo -e "${YELLOW}[5/9] Criando App Service Plan...${NC}"
if az appservice plan show --name $APP_PLAN --resource-group $RESOURCE_GROUP &> /dev/null; then
    echo -e "${GREEN}✅ App Service Plan já existe.${NC}"
else
    az appservice plan create \
      --name $APP_PLAN \
      --resource-group $RESOURCE_GROUP \
      --location "$LOCATION" \
      --sku B1 \
      --is-linux \
      --output none
    echo -e "${GREEN}✅ App Service Plan criado.${NC}"
fi
echo ""

# ==========================================
# 6. CRIAR WEB APP
# ==========================================
echo -e "${YELLOW}[6/9] Criando Web App (Java 21)...${NC}"
if az webapp show --name $APP_NAME --resource-group $RESOURCE_GROUP &> /dev/null; then
    echo -e "${GREEN}✅ Web App já existe.${NC}"
else
    az webapp create \
      --resource-group $RESOURCE_GROUP \
      --plan $APP_PLAN \
      --name $APP_NAME \
      --runtime "JAVA:21-java21" \
      --output none
    
    # Configurar Java 21 explicitamente
    az webapp config set \
      --resource-group $RESOURCE_GROUP \
      --name $APP_NAME \
      --java-version "21" \
      --java-container "JAVA" \
      --java-container-version "21" \
      --output none
    
    echo -e "${GREEN}✅ Web App criada: $APP_NAME${NC}"
fi
echo ""

# ==========================================
# 7. CONFIGURAR CONNECTION STRING
# ==========================================
echo -e "${YELLOW}[7/9] Configurando connection string do PostgreSQL...${NC}"
CONNECTION_STRING="jdbc:postgresql://${POSTGRES_SERVER}.postgres.database.azure.com:5432/${POSTGRES_DB}?sslmode=require"

# Configurar connection string no formato Azure
az webapp config connection-string set \
  --resource-group $RESOURCE_GROUP \
  --name $APP_NAME \
  --connection-string-type PostgreSQL \
  --settings POSTGRESQLCONNSTR_DefaultConnection="$CONNECTION_STRING" \
  --output none

echo -e "${GREEN}✅ Connection string configurada.${NC}"
echo ""

# ==========================================
# 8. CONFIGURAR APP SETTINGS
# ==========================================
echo -e "${YELLOW}[8/9] Configurando variáveis de ambiente...${NC}"
az webapp config appsettings set \
  --resource-group $RESOURCE_GROUP \
  --name $APP_NAME \
  --settings \
    DB_USERNAME="$POSTGRES_USER" \
    DB_PASSWORD="$POSTGRES_PASSWORD" \
    SPRING_PROFILES_ACTIVE="cloud" \
    JAVA_OPTS="-Xms512m -Xmx1024m" \
    WEBSITES_ENABLE_APP_SERVICE_STORAGE=false \
  --output none

echo -e "${GREEN}✅ Variáveis de ambiente configuradas.${NC}"
echo ""

# ==========================================
# 9. BUILD E DEPLOY DA APLICAÇÃO
# ==========================================
echo -e "${YELLOW}[9/9] Fazendo build e deploy da aplicação...${NC}"

# Verificar se estamos no diretório correto
if [ ! -f "build.gradle" ]; then
    echo -e "${RED}❌ Erro: build.gradle não encontrado. Execute este script na raiz do projeto.${NC}"
    exit 1
fi

# Build da aplicação
echo "  Compilando aplicação..."
chmod +x gradlew 2>/dev/null || true
./gradlew clean build -x test --no-daemon

# Verificar se o JAR foi gerado
JAR_FILE="build/libs/mottu-api-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo -e "${RED}❌ Erro: JAR não encontrado após build: $JAR_FILE${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Build concluído.${NC}"
echo "  Fazendo deploy do JAR..."

# Deploy do JAR
az webapp deploy \
  --resource-group $RESOURCE_GROUP \
  --name $APP_NAME \
  --src-path $JAR_FILE \
  --type jar \
  --output none

echo -e "${GREEN}✅ Deploy concluído!${NC}"
echo ""

# ==========================================
# RESUMO FINAL
# ==========================================
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  ✅ DEPLOY CONCLUÍDO COM SUCESSO!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${BLUE}📋 Informações do Deploy:${NC}"
echo "  Resource Group: $RESOURCE_GROUP"
echo "  PostgreSQL Server: $POSTGRES_SERVER.postgres.database.azure.com"
echo "  Database: $POSTGRES_DB"
echo "  Usuário DB: $POSTGRES_USER"
echo ""
echo -e "${BLUE}🌐 URLs da Aplicação:${NC}"
APP_URL="https://${APP_NAME}.azurewebsites.net"
echo "  🏠 API Principal: $APP_URL"
echo "  📚 Swagger UI: $APP_URL/swagger-ui.html"
echo "  📖 API Docs: $APP_URL/v3/api-docs"
echo "  🔐 Login: $APP_URL/login"
echo ""
echo -e "${YELLOW}⏳ Aguarde 2-3 minutos para a aplicação inicializar completamente...${NC}"
echo ""
echo -e "${BLUE}🔧 Comandos Úteis:${NC}"
echo "  Ver logs: az webapp log tail --name $APP_NAME --resource-group $RESOURCE_GROUP"
echo "  Ver status: az webapp show --name $APP_NAME --resource-group $RESOURCE_GROUP --query state"
echo "  Restart: az webapp restart --name $APP_NAME --resource-group $RESOURCE_GROUP"
echo ""
echo -e "${GREEN}✅ Deploy finalizado!${NC}"
echo ""

