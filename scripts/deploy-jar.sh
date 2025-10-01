#!/bin/bash

# Script para Deploy do JAR no Azure App Service
# Mottu API - Challenge FIAP 2025

APP_NAME="mottu-api-fiap"
RESOURCE_GROUP="MottuRG"
JAR_FILE="build/libs/mottu-api-0.0.1-SNAPSHOT.jar"

echo "=== DEPLOY DO JAR PARA AZURE APP SERVICE ==="
echo ""

# Verificar se o JAR existe
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR não encontrado: $JAR_FILE"
    echo "Execute primeiro: ./scripts/build.sh"
    exit 1
fi

echo "✅ JAR encontrado: $JAR_FILE"
echo ""

# Fazer deploy usando Azure CLI
echo "1. Fazendo deploy do JAR..."
az webapp deploy \
  --resource-group $RESOURCE_GROUP \
  --name $APP_NAME \
  --src-path $JAR_FILE \
  --type jar

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ DEPLOY CONCLUÍDO COM SUCESSO!"
    echo ""
    echo "URLs da aplicação:"
    echo "- API: https://$APP_NAME.azurewebsites.net"
    echo "- Swagger: https://$APP_NAME.azurewebsites.net/swagger-ui.html"
    echo "- Interface Web: https://$APP_NAME.azurewebsites.net/login"
    echo ""
    echo "Aguarde alguns minutos para a aplicação inicializar..."
    echo "Execute os testes: ./scripts/test-api.sh"
else
    echo ""
    echo "❌ ERRO NO DEPLOY!"
    echo "Verifique se os recursos Azure foram criados corretamente."
    exit 1
fi
