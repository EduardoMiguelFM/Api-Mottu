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
LOCATION="brazilsouth"  # Região do Brasil Sul (suporta PostgreSQL Flexible Server)
POSTGRES_SERVER="motovision-postgres-$(date +%s | tail -c 5)"  # Nome único com timestamp
POSTGRES_DB="motovisiondb"
POSTGRES_USER="motovisionadmin"
POSTGRES_PASSWORD="Fiap2025!Secure@Database#Pass"  # Senha segura (sem referências a recursos)
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
echo -e "${YELLOW}[2/9] Verificando servidor PostgreSQL Flexible Server...${NC}"

# Tentar encontrar servidor PostgreSQL existente
EXISTING_POSTGRES=$(az postgres flexible-server list --resource-group $RESOURCE_GROUP --query "[0].name" -o tsv 2>/dev/null)

if [ -n "$EXISTING_POSTGRES" ] && [ "$EXISTING_POSTGRES" != "null" ]; then
    POSTGRES_SERVER="$EXISTING_POSTGRES"
    echo -e "${GREEN}✅ Servidor PostgreSQL já existe: $POSTGRES_SERVER${NC}"
elif az postgres flexible-server show --resource-group $RESOURCE_GROUP --name $POSTGRES_SERVER &> /dev/null; then
    echo -e "${GREEN}✅ Servidor PostgreSQL já existe: $POSTGRES_SERVER${NC}"
else
    echo "  Criando servidor PostgreSQL..."
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
fi
echo ""

# ==========================================
# 3. CRIAR DATABASE
# ==========================================
echo -e "${YELLOW}[3/9] Verificando database...${NC}"
if az postgres flexible-server db show --resource-group $RESOURCE_GROUP --server-name $POSTGRES_SERVER --database-name $POSTGRES_DB &> /dev/null; then
    echo -e "${GREEN}✅ Database '$POSTGRES_DB' já existe.${NC}"
else
    echo "  Criando database..."
    az postgres flexible-server db create \
      --resource-group $RESOURCE_GROUP \
      --server-name $POSTGRES_SERVER \
      --database-name $POSTGRES_DB \
      --output none
    echo -e "${GREEN}✅ Database '$POSTGRES_DB' criado.${NC}"
fi
echo ""

# ==========================================
# 4. CONFIGURAR FIREWALL (Permitir Azure Services)
# ==========================================
echo -e "${YELLOW}[4/9] Verificando regra de firewall...${NC}"
if az postgres flexible-server firewall-rule show --resource-group $RESOURCE_GROUP --name $POSTGRES_SERVER --rule-name AllowAzureServices &> /dev/null; then
    echo -e "${GREEN}✅ Regra de firewall já existe.${NC}"
else
    echo "  Criando regra de firewall..."
    az postgres flexible-server firewall-rule create \
      --resource-group $RESOURCE_GROUP \
      --name $POSTGRES_SERVER \
      --rule-name AllowAzureServices \
      --start-ip-address 0.0.0.0 \
      --end-ip-address 0.0.0.0 \
      --output none
    echo -e "${GREEN}✅ Firewall configurado.${NC}"
fi
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
echo -e "${YELLOW}[6/9] Verificando Web App (Java 21)...${NC}"

# Tentar encontrar Web App existente
EXISTING_WEBAPP=$(az webapp list --resource-group $RESOURCE_GROUP --query "[0].name" -o tsv 2>/dev/null)

if [ -n "$EXISTING_WEBAPP" ] && [ "$EXISTING_WEBAPP" != "null" ]; then
    APP_NAME="$EXISTING_WEBAPP"
    echo -e "${GREEN}✅ Web App já existe: $APP_NAME${NC}"
elif az webapp show --name $APP_NAME --resource-group $RESOURCE_GROUP &> /dev/null; then
    echo -e "${GREEN}✅ Web App já existe: $APP_NAME${NC}"
else
    echo "  Criando Web App..."
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

# Verificar e instalar Java 21 se necessário
echo "  Verificando Java 21..."

# Função para verificar versão Java
check_java_version() {
    local java_path=$1
    if [ -f "$java_path" ]; then
        local version=$($java_path -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
        echo "$version"
    else
        echo "0"
    fi
}

# Verificar se Java 21 já está disponível no PATH
JAVA_VERSION=$(check_java_version "java")

if [ "$JAVA_VERSION" != "21" ]; then
    echo -e "${YELLOW}⚠️  Java 21 não encontrado no PATH. Tentando instalar...${NC}"
    
    # Verificar se Java 21 está instalado em algum lugar
    POSSIBLE_JAVA_HOMES=(
        "/usr/lib/jvm/java-21-openjdk-amd64"
        "/usr/lib/jvm/java-21-openjdk"
        "$HOME/.sdkman/candidates/java/21.0.2-tem"
        "$HOME/.sdkman/candidates/java/21.0.2-open"
        "$HOME/.sdkman/candidates/java/current"
    )
    
    JAVA_FOUND=false
    for JAVA_HOME_PATH in "${POSSIBLE_JAVA_HOMES[@]}"; do
        if [ -d "$JAVA_HOME_PATH" ] && [ -f "$JAVA_HOME_PATH/bin/java" ]; then
            JAVA_VER_CHECK=$(check_java_version "$JAVA_HOME_PATH/bin/java")
            if [ "$JAVA_VER_CHECK" = "21" ]; then
                export JAVA_HOME="$JAVA_HOME_PATH"
                export PATH=$JAVA_HOME/bin:$PATH
                JAVA_FOUND=true
                echo -e "${GREEN}✅ Java 21 encontrado em: $JAVA_HOME${NC}"
                break
            fi
        fi
    done
    
    # Se não encontrou, tentar instalar via SDKMAN (sem sudo)
    if [ "$JAVA_FOUND" = false ]; then
        echo -e "${YELLOW}📦 Instalando Java 21 via SDKMAN...${NC}"
        
        # Instalar SDKMAN se não existir
        if [ ! -d "$HOME/.sdkman" ]; then
            echo "  Instalando SDKMAN..."
            curl -s "https://get.sdkman.io" | bash > /dev/null 2>&1
        fi
        
        # Carregar SDKMAN
        if [ -f "$HOME/.sdkman/bin/sdkman-init.sh" ]; then
            source "$HOME/.sdkman/bin/sdkman-init.sh"
            
            # Instalar Java 21 (tentando versões disponíveis)
            echo "  Baixando Java 21 (isso pode demorar alguns minutos)..."
            if sdk install java 21.0.2-tem 2>/dev/null || \
               sdk install java 21.0.2-open 2>/dev/null || \
               sdk install java 21.0.1-tem 2>/dev/null || \
               sdk install java 21.0.1-open 2>/dev/null; then
                
                # Configurar como padrão
                sdk default java 21.0.2-tem 2>/dev/null || \
                sdk default java 21.0.2-open 2>/dev/null || \
                sdk default java 21.0.1-tem 2>/dev/null || \
                sdk default java 21.0.1-open 2>/dev/null || true
                
                # Recarregar SDKMAN
                source "$HOME/.sdkman/bin/sdkman-init.sh"
                
                # Verificar se funcionou
                JAVA_HOME_SDKMAN="$HOME/.sdkman/candidates/java/current"
                if [ -f "$JAVA_HOME_SDKMAN/bin/java" ]; then
                    JAVA_VER_CHECK=$(check_java_version "$JAVA_HOME_SDKMAN/bin/java")
                    if [ "$JAVA_VER_CHECK" = "21" ]; then
                        export JAVA_HOME="$JAVA_HOME_SDKMAN"
                        export PATH=$JAVA_HOME/bin:$PATH
                        JAVA_FOUND=true
                        echo -e "${GREEN}✅ Java 21 instalado via SDKMAN${NC}"
                    fi
                fi
            fi
        fi
        
        # Se ainda não encontrou, tentar download direto do OpenJDK
        if [ "$JAVA_FOUND" = false ]; then
            echo -e "${YELLOW}📦 Tentando download direto do OpenJDK 21...${NC}"
            JAVA_DIR="$HOME/java-21"
            mkdir -p "$JAVA_DIR"
            
            # Tentar baixar OpenJDK 21 (Linux x64)
            if command -v wget &> /dev/null; then
                echo "  Baixando OpenJDK 21..."
                wget -q -O /tmp/openjdk-21.tar.gz "https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91dc5584760ac0/11/GPL/openjdk-21.0.2_linux-x64_bin.tar.gz" 2>/dev/null || \
                wget -q -O /tmp/openjdk-21.tar.gz "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.2%2B13/OpenJDK21U-jdk_x64_linux_hotspot_21.0.2_13.tar.gz" 2>/dev/null
                
                if [ -f /tmp/openjdk-21.tar.gz ]; then
                    echo "  Extraindo OpenJDK 21..."
                    tar -xzf /tmp/openjdk-21.tar.gz -C "$JAVA_DIR" --strip-components=1 2>/dev/null
                    rm /tmp/openjdk-21.tar.gz 2>/dev/null
                    
                    if [ -f "$JAVA_DIR/bin/java" ]; then
                        JAVA_VER_CHECK=$(check_java_version "$JAVA_DIR/bin/java")
                        if [ "$JAVA_VER_CHECK" = "21" ]; then
                            export JAVA_HOME="$JAVA_DIR"
                            export PATH=$JAVA_HOME/bin:$PATH
                            JAVA_FOUND=true
                            echo -e "${GREEN}✅ Java 21 instalado via download direto${NC}"
                        fi
                    fi
                fi
            fi
        fi
        
        # Se ainda não encontrou, erro
        if [ "$JAVA_FOUND" = false ]; then
            echo -e "${RED}❌ Não foi possível instalar Java 21 automaticamente.${NC}"
            echo ""
            echo -e "${YELLOW}💡 SOLUÇÃO ALTERNATIVA: Faça build localmente e faça upload do JAR${NC}"
            echo ""
            echo "1. Na sua máquina local (onde você tem Java 21), execute:"
            echo "   ./gradlew clean build -x test"
            echo ""
            echo "2. Faça upload do JAR para Cloud Shell:"
            echo "   Arquivo: build/libs/mottu-api-0.0.1-SNAPSHOT.jar"
            echo ""
            echo "3. No Cloud Shell, crie a pasta e execute:"
            echo "   mkdir -p build/libs"
            echo "   mv mottu-api-0.0.1-SNAPSHOT.jar build/libs/"
            echo "   ./scripts/deploy-jar.sh"
            echo ""
            exit 1
        fi
    fi
else
    # Java 21 já está no PATH
    echo -e "${GREEN}✅ Java 21 já está disponível no PATH${NC}"
fi

echo "  Java version:"
java -version 2>&1 | head -n 1

# Build da aplicação
echo "  Compilando aplicação..."
chmod +x gradlew 2>/dev/null || true

# Garantir que está usando Java 21
export JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java-21-openjdk-amd64}
export PATH=$JAVA_HOME/bin:$PATH

# Executar build com timeout (pode demorar na primeira vez)
echo "  Isso pode demorar alguns minutos na primeira vez..."
timeout 600 ./gradlew clean build -x test --no-daemon || {
    echo -e "${RED}❌ Build falhou ou demorou muito${NC}"
    echo ""
    echo -e "${YELLOW}💡 Alternativa: Faça build localmente e faça upload do JAR${NC}"
    echo "  1. Na sua máquina: ./gradlew clean build -x test"
    echo "  2. Faça upload do JAR: build/libs/mottu-api-0.0.1-SNAPSHOT.jar"
    echo "  3. No Cloud Shell: mkdir -p build/libs && mv mottu-api-0.0.1-SNAPSHOT.jar build/libs/"
    echo "  4. Execute: ./scripts/deploy-jar.sh"
    exit 1
}

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

