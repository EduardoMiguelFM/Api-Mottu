#!/bin/bash

# Script de Teste da API no Azure
# Mottu API - Challenge FIAP 2025

APP_URL="https://mottu-api-fiap.azurewebsites.net/api"

echo "=== TESTANDO API NO AZURE ==="
echo "URL Base: $APP_URL"
echo ""

# Aguardar a aplicação inicializar
echo "Aguardando aplicação inicializar..."
sleep 30

# 1. Testar health check - Listar pátios
echo "1. Testando listagem de pátios..."
curl -X GET "$APP_URL/patios" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

echo ""

# 2. Testar CRUD Motos
echo "2. Testando CRUD de Motos..."

# CREATE - Criar nova moto
echo "2.1. Criando nova moto..."
CREATE_RESPONSE=$(curl -X POST "$APP_URL/motos" \
  -H "Content-Type: application/json" \
  -d '{
    "modelo": "Honda PCX 160",
    "placa": "JKL-3456",
    "status": "DISPONIVEL",
    "patioId": 1
  }' \
  -w "\nStatus: %{http_code}\n" \
  -s)

echo "$CREATE_RESPONSE"
echo ""

# READ - Listar todas as motos
echo "2.2. Listando todas as motos..."
curl -X GET "$APP_URL/motos/todos" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

echo ""

# READ - Buscar moto por ID
echo "2.3. Buscando moto por ID (1)..."
curl -X GET "$APP_URL/motos/id/1" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

echo ""

# UPDATE - Atualizar moto
echo "2.4. Atualizando moto (ID 1)..."
curl -X PUT "$APP_URL/motos/id/1" \
  -H "Content-Type: application/json" \
  -d '{
    "modelo": "Honda Biz 125",
    "placa": "ABC-1234",
    "status": "MANUTENCAO",
    "patioId": 1
  }' \
  -w "\nStatus: %{http_code}\n" \
  -s

echo ""

# Testar endpoints especiais
echo "3. Testando endpoints especiais..."

# Status por setor
echo "3.1. Contagem de motos por setor A..."
curl -X GET "$APP_URL/motos/patio/setor/A/contagem" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

echo ""

# Status individual da moto
echo "3.2. Status individual da moto ABC-1234..."
curl -X GET "$APP_URL/motos/patio/moto/ABC-1234/status" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

echo ""

# Status geral do pátio
echo "3.3. Status geral do pátio..."
curl -X GET "$APP_URL/patios/status" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n" \
  -s

echo ""

# DELETE - Deletar moto (se existir ID 4)
echo "2.5. Tentando deletar moto (ID 4)..."
curl -X DELETE "$APP_URL/motos/id/4" \
  -w "\nStatus: %{http_code}\n" \
  -s

echo ""

echo "=== TESTES CONCLUÍDOS ==="
echo ""
echo "URLs importantes:"
echo "- API: https://mottu-api-fiap.azurewebsites.net"
echo "- Swagger: https://mottu-api-fiap.azurewebsites.net/swagger-ui.html"
echo "- Interface Web: https://mottu-api-fiap.azurewebsites.net/login"
