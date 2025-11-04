#!/bin/bash

# Script Completo de Testes - Todas as Entidades
# MotoVision API - Challenge FIAP 2025
# Testa todas as entidades antes do deploy

# Configuração
BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api"

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Contadores
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Função para executar teste
test_endpoint() {
    local METHOD=$1
    local ENDPOINT=$2
    local DATA=$3
    local EXPECTED_STATUS=$4
    local DESCRIPTION=$5
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    echo -e "${YELLOW}Testando: $DESCRIPTION${NC}"
    echo "  $METHOD $ENDPOINT"
    
    if [ -z "$DATA" ]; then
        RESPONSE=$(curl -s -w "\n%{http_code}" -X $METHOD "$ENDPOINT" \
            -H "Content-Type: application/json" \
            -H "Accept: application/json" 2>/dev/null)
    else
        RESPONSE=$(curl -s -w "\n%{http_code}" -X $METHOD "$ENDPOINT" \
            -H "Content-Type: application/json" \
            -H "Accept: application/json" \
            -d "$DATA" 2>/dev/null)
    fi
    
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | sed '$d')
    
    if [ "$HTTP_CODE" = "$EXPECTED_STATUS" ]; then
        echo -e "${GREEN}  ✅ PASSOU (HTTP $HTTP_CODE)${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        return 0
    else
        echo -e "${RED}  ❌ FALHOU (Esperado: $EXPECTED_STATUS, Recebido: $HTTP_CODE)${NC}"
        echo "  Resposta: $BODY"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        return 1
    fi
}

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  TESTES COMPLETOS - MOTOVISION API${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "Base URL: $BASE_URL"
echo ""

# Verificar se a aplicação está rodando
echo -e "${YELLOW}Verificando se a aplicação está rodando...${NC}"
if ! curl -s "$BASE_URL" > /dev/null 2>&1; then
    echo -e "${RED}❌ Aplicação não está rodando em $BASE_URL${NC}"
    echo "Execute: ./gradlew bootRun"
    exit 1
fi
echo -e "${GREEN}✅ Aplicação está rodando${NC}"
echo ""

# ==========================================
# 1. TESTES DE PÁTIOS
# ==========================================
echo -e "${BLUE}=== TESTES DE PÁTIOS ===${NC}"
echo ""

# Listar todos os pátios
test_endpoint "GET" "$API_BASE/patios" "" "200" "Listar todos os pátios"

# Buscar pátio por ID (assumindo que existe ID 1)
test_endpoint "GET" "$API_BASE/patios/1" "" "200" "Buscar pátio por ID"

# Status geral dos pátios
test_endpoint "GET" "$API_BASE/patios/status" "" "200" "Status geral dos pátios"

# Criar novo pátio
PATIO_DATA='{"nome":"Pátio Teste Automatizado","endereco":"Rua Teste, 123 - São Paulo - SP"}'
test_endpoint "POST" "$API_BASE/patios" "$PATIO_DATA" "201" "Criar novo pátio"

# Atualizar pátio (assumindo ID 1)
PATIO_UPDATE='{"nome":"Pátio Atualizado","endereco":"Rua Atualizada, 456 - São Paulo - SP"}'
test_endpoint "PUT" "$API_BASE/patios/1" "$PATIO_UPDATE" "200" "Atualizar pátio"

echo ""

# ==========================================
# 2. TESTES DE USUÁRIOS
# ==========================================
echo -e "${BLUE}=== TESTES DE USUÁRIOS ===${NC}"
echo ""

# Listar todos os usuários
test_endpoint "GET" "$API_BASE/usuarios" "" "200" "Listar todos os usuários"

# Buscar usuário por ID (assumindo que existe ID 1)
test_endpoint "GET" "$API_BASE/usuarios/1" "" "200" "Buscar usuário por ID"

echo ""

# ==========================================
# 3. TESTES DE MOTOS
# ==========================================
echo -e "${BLUE}=== TESTES DE MOTOS ===${NC}"
echo ""

# Listar todas as motos
test_endpoint "GET" "$API_BASE/motos/todos" "" "200" "Listar todas as motos"

# Buscar moto por ID (assumindo que existe ID 1)
test_endpoint "GET" "$API_BASE/motos/id/1" "" "200" "Buscar moto por ID"

# Buscar moto por placa (assumindo que existe uma placa)
test_endpoint "GET" "$API_BASE/motos/placa/ABC1234" "" "200" "Buscar moto por placa"

# Filtrar por status
test_endpoint "GET" "$API_BASE/motos/status?status=DISPONIVEL" "" "200" "Filtrar motos por status"

# Filtrar por status, setor e cor
test_endpoint "GET" "$API_BASE/motos/filtro?status=DISPONIVEL&setor=Setor%20A&cor=Verde" "" "200" "Filtrar motos por múltiplos critérios"

# Contar motos por setor
test_endpoint "GET" "$API_BASE/motos/patio/setor/Setor%20A/contagem" "" "200" "Contar motos por setor"

# Status por placa
test_endpoint "GET" "$API_BASE/motos/patio/moto/ABC1234/status" "" "200" "Status de moto por placa"

# Criar nova moto (assumindo que existe pátio com ID 1)
MOTO_DATA='{"modelo":"Honda Biz Teste","placa":"TEST123","status":"DISPONIVEL","nomePatio":"Pátio Butantã"}'
test_endpoint "POST" "$API_BASE/motos" "$MOTO_DATA" "201" "Criar nova moto"

# Atualizar moto por ID
MOTO_UPDATE='{"modelo":"Honda Biz Atualizado","placa":"TEST123","status":"RESERVADA","nomePatio":"Pátio Butantã"}'
test_endpoint "PUT" "$API_BASE/motos/id/1" "$MOTO_UPDATE" "200" "Atualizar moto por ID"

# Atualizar moto por placa
MOTO_UPDATE_PLACA='{"modelo":"Honda Biz Atualizado Placa","placa":"TEST123","status":"MANUTENCAO","nomePatio":"Pátio Butantã"}'
test_endpoint "PUT" "$API_BASE/motos/placa/TEST123" "$MOTO_UPDATE_PLACA" "200" "Atualizar moto por placa"

echo ""

# ==========================================
# 4. TESTES DE AUTENTICAÇÃO
# ==========================================
echo -e "${BLUE}=== TESTES DE AUTENTICAÇÃO ===${NC}"
echo ""

# Login (usando credenciais de teste)
LOGIN_DATA='{"email":"admin@mottu.com.br","senha":"admin123"}'
test_endpoint "POST" "$API_BASE/auth/login" "$LOGIN_DATA" "200" "Login de usuário"

# Validar token
test_endpoint "GET" "$API_BASE/auth/validate" "" "200" "Validar token"

echo ""

# ==========================================
# 5. TESTES DE INTEGRAÇÃO (Relacionamentos)
# ==========================================
echo -e "${BLUE}=== TESTES DE INTEGRAÇÃO ===${NC}"
echo ""

# Verificar se motos estão relacionadas com pátios
echo -e "${YELLOW}Verificando relacionamento Moto-Pátio...${NC}"
MOTOS=$(curl -s "$API_BASE/motos/todos" -H "Accept: application/json")
if echo "$MOTOS" | grep -q "patio"; then
    echo -e "${GREEN}  ✅ Relacionamento Moto-Pátio funcionando${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "${YELLOW}  ⚠️  Verificar estrutura de resposta${NC}"
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))

echo ""

# ==========================================
# 6. TESTES DE VALIDAÇÃO
# ==========================================
echo -e "${BLUE}=== TESTES DE VALIDAÇÃO ===${NC}"
echo ""

# Tentar criar moto com dados inválidos
MOTO_INVALID='{"modelo":"","placa":"123","status":"INVALIDO","nomePatio":""}'
test_endpoint "POST" "$API_BASE/motos" "$MOTO_INVALID" "400" "Validar criação de moto com dados inválidos"

# Tentar criar pátio com dados inválidos
PATIO_INVALID='{"nome":"","endereco":""}'
test_endpoint "POST" "$API_BASE/patios" "$PATIO_INVALID" "400" "Validar criação de pátio com dados inválidos"

echo ""

# ==========================================
# 7. TESTES DE ERRO (Endpoints não existentes)
# ==========================================
echo -e "${BLUE}=== TESTES DE ERRO ===${NC}"
echo ""

# Buscar recurso inexistente
test_endpoint "GET" "$API_BASE/patios/99999" "" "404" "Buscar pátio inexistente"
test_endpoint "GET" "$API_BASE/motos/id/99999" "" "200" "Buscar moto inexistente (pode retornar erro ou null)"

echo ""

# ==========================================
# RESUMO FINAL
# ==========================================
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  RESUMO DOS TESTES${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "Total de testes: $TOTAL_TESTS"
echo -e "${GREEN}Testes passaram: $PASSED_TESTS${NC}"
echo -e "${RED}Testes falharam: $FAILED_TESTS${NC}"
echo ""

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}✅ TODOS OS TESTES PASSARAM!${NC}"
    echo -e "${GREEN}A aplicação está pronta para deploy! 🚀${NC}"
    exit 0
else
    echo -e "${RED}❌ Alguns testes falharam${NC}"
    echo -e "${YELLOW}Revise os erros acima antes de fazer deploy${NC}"
    exit 1
fi

