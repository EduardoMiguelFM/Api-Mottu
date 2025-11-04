#!/bin/bash

# Script para verificar status do deploy no Azure
# MotoVision API - Challenge FIAP 2025

# Variáveis
RESOURCE_GROUP="MotoVisionRG"
APP_NAME="motovision-api"

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  VERIFICAÇÃO DE STATUS DO DEPLOY${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Verificar se está logado no Azure
if ! az account show &> /dev/null; then
    echo -e "${RED}❌ Não autenticado no Azure. Execute: az login${NC}"
    exit 1
fi

# Verificar Resource Group
echo -e "${YELLOW}[1] Verificando Resource Group...${NC}"
if az group show --name $RESOURCE_GROUP &> /dev/null; then
    echo -e "${GREEN}✅ Resource Group existe: $RESOURCE_GROUP${NC}"
else
    echo -e "${RED}❌ Resource Group não encontrado: $RESOURCE_GROUP${NC}"
    exit 1
fi
echo ""

# Verificar App Service
echo -e "${YELLOW}[2] Verificando App Service...${NC}"
if az webapp show --name $APP_NAME --resource-group $RESOURCE_GROUP &> /dev/null; then
    STATE=$(az webapp show --name $APP_NAME --resource-group $RESOURCE_GROUP --query state -o tsv)
    URL=$(az webapp show --name $APP_NAME --resource-group $RESOURCE_GROUP --query defaultHostName -o tsv)
    
    echo -e "${GREEN}✅ App Service encontrado: $APP_NAME${NC}"
    echo "   Estado: $STATE"
    echo "   URL: https://$URL"
else
    echo -e "${RED}❌ App Service não encontrado: $APP_NAME${NC}"
    exit 1
fi
echo ""

# Verificar PostgreSQL
echo -e "${YELLOW}[3] Verificando PostgreSQL...${NC}"
POSTGRES_SERVERS=$(az postgres flexible-server list --resource-group $RESOURCE_GROUP --query "[].name" -o tsv)
if [ -z "$POSTGRES_SERVERS" ]; then
    echo -e "${RED}❌ Nenhum servidor PostgreSQL encontrado${NC}"
else
    echo -e "${GREEN}✅ Servidores PostgreSQL encontrados:${NC}"
    for server in $POSTGRES_SERVERS; do
        echo "   - $server"
    done
fi
echo ""

# Verificar Configurações do App Service
echo -e "${YELLOW}[4] Verificando configurações do App Service...${NC}"
echo -e "${BLUE}Java Version:${NC}"
az webapp config show --name $APP_NAME --resource-group $RESOURCE_GROUP --query "javaVersion" -o tsv
echo ""

echo -e "${BLUE}Variáveis de Ambiente:${NC}"
az webapp config appsettings list --name $APP_NAME --resource-group $RESOURCE_GROUP --query "[?name=='SPRING_PROFILES_ACTIVE' || name=='DB_USERNAME'].{Name:name, Value:value}" -o table
echo ""

echo -e "${BLUE}Connection Strings:${NC}"
az webapp config connection-string list --name $APP_NAME --resource-group $RESOURCE_GROUP --query "[].{Name:name, Type:type}" -o table
echo ""

# Verificar Logs Recentes
echo -e "${YELLOW}[5] Últimas linhas de log (últimas 20)...${NC}"
echo -e "${BLUE}Nota: Pode demorar alguns segundos...${NC}"
az webapp log tail --name $APP_NAME --resource-group $RESOURCE_GROUP --output none &
TAIL_PID=$!
sleep 5
kill $TAIL_PID 2>/dev/null || true
echo ""

# Testar Endpoints
echo -e "${YELLOW}[6] Testando endpoints...${NC}"
APP_URL="https://$URL"

echo -e "${BLUE}Testando API principal...${NC}"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" --max-time 10 "$APP_URL" || echo "000")
if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "302" ] || [ "$HTTP_CODE" = "401" ]; then
    echo -e "${GREEN}✅ API respondendo (HTTP $HTTP_CODE)${NC}"
else
    echo -e "${RED}❌ API não está respondendo (HTTP $HTTP_CODE)${NC}"
    echo "   Aguarde mais alguns minutos para a aplicação inicializar completamente."
fi

echo ""
echo -e "${BLUE}Testando Swagger...${NC}"
SWAGGER_CODE=$(curl -s -o /dev/null -w "%{http_code}" --max-time 10 "$APP_URL/swagger-ui.html" || echo "000")
if [ "$SWAGGER_CODE" = "200" ] || [ "$SWAGGER_CODE" = "302" ]; then
    echo -e "${GREEN}✅ Swagger acessível (HTTP $SWAGGER_CODE)${NC}"
else
    echo -e "${YELLOW}⚠️  Swagger ainda não disponível (HTTP $SWAGGER_CODE)${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  VERIFICAÇÃO CONCLUÍDA${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${BLUE}URLs da Aplicação:${NC}"
echo "  🏠 API: $APP_URL"
echo "  📚 Swagger: $APP_URL/swagger-ui.html"
echo "  🔐 Login: $APP_URL/login"
echo ""
echo -e "${YELLOW}Comandos Úteis:${NC}"
echo "  Ver logs: az webapp log tail --name $APP_NAME --resource-group $RESOURCE_GROUP"
echo "  Reiniciar: az webapp restart --name $APP_NAME --resource-group $RESOURCE_GROUP"
echo "  Parar: az webapp stop --name $APP_NAME --resource-group $RESOURCE_GROUP"
echo "  Iniciar: az webapp start --name $APP_NAME --resource-group $RESOURCE_GROUP"
echo ""

