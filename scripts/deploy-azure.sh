#!/bin/bash

# Script de Deploy para Azure App Service com PostgreSQL
# MotoVision API - Challenge FIAP 2025

# Variáveis
RESOURCE_GROUP="MotoVisionRG"
LOCATION="Brazil South"
POSTGRES_SERVER="motovision-postgres-server"
POSTGRES_DB="motovisiondb"
POSTGRES_USER="motovisionadmin"
POSTGRES_PASSWORD="MotoVisionPass123!"
APP_NAME="motovision-api"
APP_PLAN="MotoVisionAppServicePlan"

echo "=== CRIANDO RECURSOS AZURE ==="

# 1. Criar Grupo de Recursos
echo "1. Criando grupo de recursos..."
az group create --name $RESOURCE_GROUP --location "$LOCATION"

# 2. Criar PostgreSQL Server
echo "2. Criando servidor PostgreSQL..."
az postgres flexible-server create \
  --resource-group $RESOURCE_GROUP \
  --name $POSTGRES_SERVER \
  --location "$LOCATION" \
  --admin-user $POSTGRES_USER \
  --admin-password $POSTGRES_PASSWORD \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --storage-size 32 \
  --version 13

# 3. Criar Database
echo "3. Criando database..."
az postgres flexible-server db create \
  --resource-group $RESOURCE_GROUP \
  --server-name $POSTGRES_SERVER \
  --database-name $POSTGRES_DB

# 4. Configurar Firewall
echo "4. Configurando firewall..."
az postgres flexible-server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --name $POSTGRES_SERVER \
  --rule-name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0

# 5. Criar App Service Plan
echo "5. Criando App Service Plan..."
az appservice plan create \
  --name $APP_PLAN \
  --resource-group $RESOURCE_GROUP \
  --sku B1 \
  --is-linux

# 6. Criar Web App
echo "6. Criando Web App..."
az webapp create \
  --resource-group $RESOURCE_GROUP \
  --plan $APP_PLAN \
  --name $APP_NAME \
  --runtime "JAVA|21-java21"

# 7. Configurar Connection String
echo "7. Configurando connection string..."
az webapp config connection-string set \
  --resource-group $RESOURCE_GROUP \
  --name $APP_NAME \
  --settings POSTGRESQLCONNSTR_DefaultConnection="jdbc:postgresql://$POSTGRES_SERVER.postgres.database.azure.com:5432/$POSTGRES_DB?sslmode=require"

# 8. Configurar App Settings
echo "8. Configurando app settings..."
az webapp config appsettings set \
  --resource-group $RESOURCE_GROUP \
  --name $APP_NAME \
  --settings \
    DB_USERNAME="$POSTGRES_USER" \
    DB_PASSWORD="$POSTGRES_PASSWORD" \
    SPRING_PROFILES_ACTIVE="cloud"

echo "=== DEPLOY CONCLUÍDO ==="
echo "URL da aplicação: https://$APP_NAME.azurewebsites.net"
echo "Swagger: https://$APP_NAME.azurewebsites.net/swagger-ui.html"
echo ""
echo "Próximos passos:"
echo "1. Fazer build da aplicação: ./gradlew clean build"
echo "2. Fazer deploy do JAR para o App Service"
echo "3. Executar testes: ./scripts/test-api.sh"
