#!/bin/bash

# Script de Build da Aplicação
# MotoVision API - Challenge FIAP 2025

echo "=== BUILD DA APLICAÇÃO MOTOVISION API ==="
echo ""

# Verificar se o Gradle está disponível
if ! command -v ./gradlew &> /dev/null; then
    echo "Erro: gradlew não encontrado. Execute este script na raiz do projeto."
    exit 1
fi

# Limpar build anterior
echo "1. Limpando build anterior..."
./gradlew clean

# Fazer build da aplicação (sem testes para acelerar)
echo "2. Compilando aplicação..."
./gradlew build -x test

# Verificar se o build foi bem-sucedido
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ BUILD CONCLUÍDO COM SUCESSO!"
    echo ""
    echo "Arquivos gerados:"
    echo "- JAR: build/libs/mottu-api-0.0.1-SNAPSHOT.jar"
    echo "- JAR Plain: build/libs/mottu-api-0.0.1-SNAPSHOT-plain.jar"
    echo ""
    echo "Próximos passos:"
    echo "1. Para testar localmente: ./gradlew bootRun"
    echo "2. Para deploy no Azure: execute os scripts em scripts/"
    echo "3. Para testar API: ./scripts/test-api.sh"
else
    echo ""
    echo "❌ ERRO NO BUILD!"
    echo "Verifique os logs acima para identificar o problema."
    exit 1
fi
