#!/bin/bash

# Script para Deletar Recursos Azure
# MotoVision API - Challenge FIAP 2025
# Use este script para limpar recursos antes de recriar

# Variáveis
RESOURCE_GROUP="MotoVisionRG"

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  DELETAR RECURSOS AZURE${NC}"
echo -e "${BLUE}  MotoVision API${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Verificar se está logado no Azure
if ! az account show &> /dev/null; then
    echo -e "${RED}❌ Não autenticado no Azure. Execute: az login${NC}"
    exit 1
fi

# Verificar se Resource Group existe
if ! az group show --name $RESOURCE_GROUP &> /dev/null; then
    echo -e "${YELLOW}⚠️  Resource Group '$RESOURCE_GROUP' não encontrado.${NC}"
    echo "Nada para deletar."
    exit 0
fi

echo -e "${YELLOW}ATENÇÃO: Este script vai deletar TODOS os recursos no Resource Group: $RESOURCE_GROUP${NC}"
echo ""
echo "Recursos que serão deletados:"
az resource list --resource-group $RESOURCE_GROUP --query "[].{Nome:name, Tipo:type}" -o table 2>/dev/null || echo "  (nenhum recurso encontrado)"
echo ""
echo -e "${RED}⚠️  Esta ação é IRREVERSÍVEL!${NC}"
echo ""
read -p "Tem certeza que deseja continuar? (digite 'SIM' para confirmar): " CONFIRM

if [ "$CONFIRM" != "SIM" ]; then
    echo "Operação cancelada."
    exit 0
fi

echo ""
echo -e "${YELLOW}Deletando Resource Group e todos os recursos...${NC}"
echo "Isso pode demorar alguns minutos..."
echo ""

# Deletar Resource Group (deleta tudo dentro dele)
az group delete --name $RESOURCE_GROUP --yes --no-wait

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ Delete iniciado com sucesso!${NC}"
    echo ""
    echo -e "${YELLOW}A exclusão está sendo processada em background.${NC}"
    echo "Pode levar alguns minutos para completar."
    echo ""
    echo "Para verificar o status:"
    echo "  az group show --name $RESOURCE_GROUP"
    echo ""
    echo "Para verificar se foi deletado:"
    echo "  az group list --query \"[?name=='$RESOURCE_GROUP']\" -o table"
    echo ""
    echo -e "${GREEN}Após a exclusão, você pode executar o deploy novamente:${NC}"
    echo "  ./scripts/deploy-azure-cloud-shell.sh"
else
    echo ""
    echo -e "${RED}❌ Erro ao deletar recursos!${NC}"
    exit 1
fi

