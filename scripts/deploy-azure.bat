@echo off
REM Script de Deploy para Azure App Service com PostgreSQL
REM MotoVision API - Challenge FIAP 2025

set RESOURCE_GROUP=MotoVisionRG
set LOCATION="Brazil South"
set POSTGRES_SERVER=motovision-postgres-server
set POSTGRES_DB=motovisiondb
set POSTGRES_USER=motovisionadmin
set POSTGRES_PASSWORD=Fiap2025!Secure@Database#Pass
set APP_NAME=motovision-api
set APP_PLAN=MotoVisionAppServicePlan

echo === CRIANDO RECURSOS AZURE ===

REM 1. Criar Grupo de Recursos
echo 1. Criando grupo de recursos...
az group create --name %RESOURCE_GROUP% --location %LOCATION%

REM 2. Criar PostgreSQL Server
echo 2. Criando servidor PostgreSQL...
az postgres flexible-server create --resource-group %RESOURCE_GROUP% --name %POSTGRES_SERVER% --location %LOCATION% --admin-user %POSTGRES_USER% --admin-password %POSTGRES_PASSWORD% --sku-name Standard_B1ms --tier Burstable --storage-size 32 --version 13

REM 3. Criar Database
echo 3. Criando database...
az postgres flexible-server db create --resource-group %RESOURCE_GROUP% --server-name %POSTGRES_SERVER% --database-name %POSTGRES_DB%

REM 4. Configurar Firewall
echo 4. Configurando firewall...
az postgres flexible-server firewall-rule create --resource-group %RESOURCE_GROUP% --name %POSTGRES_SERVER% --rule-name AllowAzureServices --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0

REM 5. Criar App Service Plan
echo 5. Criando App Service Plan...
az appservice plan create --name %APP_PLAN% --resource-group %RESOURCE_GROUP% --sku B1 --is-linux

REM 6. Criar Web App
echo 6. Criando Web App...
az webapp create --resource-group %RESOURCE_GROUP% --plan %APP_PLAN% --name %APP_NAME% --runtime "JAVA|21-java21"

REM 7. Configurar Connection String
echo 7. Configurando connection string...
az webapp config connection-string set --resource-group %RESOURCE_GROUP% --name %APP_NAME% --settings POSTGRESQLCONNSTR_DefaultConnection="jdbc:postgresql://%POSTGRES_SERVER%.postgres.database.azure.com:5432/%POSTGRES_DB%?sslmode=require"

REM 8. Configurar App Settings
echo 8. Configurando app settings...
az webapp config appsettings set --resource-group %RESOURCE_GROUP% --name %APP_NAME% --settings DB_USERNAME=%POSTGRES_USER% DB_PASSWORD=%POSTGRES_PASSWORD% SPRING_PROFILES_ACTIVE=cloud

echo === DEPLOY CONCLUÍDO ===
echo URL da aplicação: https://%APP_NAME%.azurewebsites.net
echo Swagger: https://%APP_NAME%.azurewebsites.net/swagger-ui.html
echo.
echo Próximos passos:
echo 1. Fazer build da aplicação: gradlew.bat clean build
echo 2. Fazer deploy do JAR para o App Service
echo 3. Executar testes: scripts\test-api.bat
