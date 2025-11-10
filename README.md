# MotoVision API 🚀

### Projeto desenvolvido para o Challenge FIAP 2025 (2º ano ADS)

API RESTful construída com **Spring Boot** para **gestão de motos, pátios e usuários de pátio**, simulando a operação da startup **Mottu**, conforme o desafio oficial proposto no semestre.

---

## 🔧 Funcionalidades

- ✅ Cadastro e gerenciamento de **motos** com DTOs estruturados
- ✅ Cadastro e gerenciamento de **pátios** com endereços
- ✅ Cadastro e **autenticação de usuários de pátio** com Spring Security
- ✅ Relacionamento entre entidades (Moto ↔ Pátio)
- 🔍 Filtros avançados por status, setor e cor
- 🔍 Contagem de motos por setor específico
- 🔍 Status geral do pátio por tipo de ocorrência
- 🔍 Status individual de motos por placa
- 🛠 Atualização e remoção por **ID ou placa**
- 🌐 Interface web completa com Thymeleaf
- 📊 Dashboard administrativo
- 🧭 Regras automáticas:
  - Status define **setor** e **cor** automaticamente
  - Ex: `DISPONIVEL` → `Setor A` / `Verde`

---

## 🧪 Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.2.5** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security 6** - Autenticação e autorização (Form Login)
- **Spring Validation** - Validação de dados
- **PostgreSQL** - Banco de dados principal
- **Flyway** - Migração e versionamento de banco
- **Thymeleaf** - Engine de templates para frontend
- **Swagger OpenAPI 3** (springdoc 2.5.0) - Documentação da API
- **ModelMapper 3.2.0** - Mapeamento entre DTOs e entidades
- **Jackson** - Serialização JSON
- **Docker** - Containerização da aplicação

---

## ▶️ Como Rodar

### 🏠 **Execução Local**

#### 1. Clonar o projeto

```bash
git clone https://github.com/seu-usuario/mottu-api.git
cd mottu-api
```

#### 2. Configurar banco PostgreSQL local

Certifique-se que o PostgreSQL está rodando na porta 5432 com:

- **Database**: postgres
- **Username**: postgres
- **Password**: dudu0602

#### 3. Executar localmente

```bash
# Build da aplicação
./gradlew build

# Executar
./gradlew bootRun
```

#### 4. Acessar

- **Interface Web**: http://localhost:8080/login
- **Swagger/OpenAPI**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/v3/api-docs

### 🐳 **Execução com Docker**

```bash
# Build da imagem
docker build -t mottu-api .

# Executar container
docker run -p 8080:8080 mottu-api
```

### ☁️ **Deploy no Azure (App Service + PostgreSQL)**

#### 📦 Preparar artefato local

```bash
git clone https://github.com/seu-usuario/Api-Mottu.git
cd Api-Mottu
./gradlew clean bootJar
```

O arquivo `build/libs/mottu-api-*.jar` será enviado para o Azure.

#### ☁️ Passo a passo manual no Azure Cloud Shell

1. Abra o [Portal Azure](https://portal.azure.com) e inicie o **Cloud Shell** (bash).
2. Carregue o JAR gerado (botão `Upload/Download`) ou clone o repositório dentro do shell:
   ```bash
   git clone https://github.com/seu-usuario/Api-Mottu.git
   cd Api-Mottu
   ```
3. Defina variáveis para reutilizar nos comandos:
   ```bash
   export RESOURCE_GROUP=MotoVisionRG
   export LOCATION=brazilsouth
   export APP_SERVICE_PLAN=motovision-plan
   export WEBAPP_NAME=motovision-api-8077
   export DB_SERVER=motovision-db-server
   export DB_NAME=motovisiondb
   export DB_ADMIN=motovisionadmin
   export DB_PASSWORD='DefinaUmaSenhaForte123!'
   ```
4. Crie o resource group:
   ```bash
   az group create \
     --name $RESOURCE_GROUP \
     --location $LOCATION
   ```
5. Provisione o PostgreSQL Flexible Server:

   ```bash
   az postgres flexible-server create \
     --resource-group $RESOURCE_GROUP \
     --name $DB_SERVER \
     --location $LOCATION \
     --admin-user $DB_ADMIN \
     --admin-password $DB_PASSWORD \
     --sku-name Standard_B1ms \
     --storage-size 32 \
     --tier Burstable \
     --version 16

   az postgres flexible-server db create \
     --resource-group $RESOURCE_GROUP \
     --server-name $DB_SERVER \
     --database-name $DB_NAME

   az postgres flexible-server firewall-rule create \
     --resource-group $RESOURCE_GROUP \
     --server-name $DB_SERVER \
     --name AllowAzureServices \
     --rule-type AzureServices
   ```

6. Monte a connection string:
   ```bash
   export JDBC_URL="jdbc:postgresql://$DB_SERVER.postgres.database.azure.com:5432/$DB_NAME?sslmode=require"
   ```
7. Crie o App Service Plan e a Web App:

   ```bash
   az appservice plan create \
     --name $APP_SERVICE_PLAN \
     --resource-group $RESOURCE_GROUP \
     --sku B1 \
     --is-linux

   az webapp create \
     --resource-group $RESOURCE_GROUP \
     --plan $APP_SERVICE_PLAN \
     --name $WEBAPP_NAME \
     --runtime "JAVA|21-java21"
   ```

8. Configure as variáveis de ambiente:
   ```bash
   az webapp config appsettings set \
     --resource-group $RESOURCE_GROUP \
     --name $WEBAPP_NAME \
     --settings SPRING_DATASOURCE_URL="$JDBC_URL" \
                SPRING_DATASOURCE_USERNAME="$DB_ADMIN" \
                SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD" \
                SPRING_PROFILES_ACTIVE="cloud"
   ```
9. Publique o JAR (via ZIP Deploy):
   ```bash
   cd build/libs
   zip app.zip mottu-api-*.jar
   az webapp deploy \
     --resource-group $RESOURCE_GROUP \
     --name $WEBAPP_NAME \
     --type zip \
     --src-path app.zip
   cd ../..
   ```
10. Reinicie a aplicação e monitore:

    ```bash
    az webapp restart \
      --resource-group $RESOURCE_GROUP \
      --name $WEBAPP_NAME

    az webapp log tail \
      --resource-group $RESOURCE_GROUP \
      --name $WEBAPP_NAME
    ```

#### 🌐 Acessar aplicação na nuvem

Após o deploy (aguarde 2-3 minutos para inicialização):

- **API**: https://motovision-api-8077.azurewebsites.net
- **Swagger**: https://motovision-api-8077.azurewebsites.net/swagger-ui.html
- **Interface Web**: https://motovision-api-8077.azurewebsites.net/login
- **Dashboard**: https://motovision-api-8077.azurewebsites.net/dashboard

#### ⚙️ Configurações Importantes

- **Java 21**: Aplicação requer Java 21 (configurado automaticamente)
- **PostgreSQL**: Usa Azure Database for PostgreSQL 
- **Profile Cloud**: Ativado automaticamente (`SPRING_PROFILES_ACTIVE=cloud`)
- **Flyway**: Migrações executadas automaticamente na primeira inicialização

### 👥 **Usuários de Teste**

- **Admin**: admin@teste.com / admin123
- **Supervisor**: supervisor@teste.com / supervisor123
- **Usuário**: user@teste.com / user123

### 📊 **Banco de Dados**

**Local**: PostgreSQL na porta 5432
**Nuvem**: Azure Database for PostgreSQL (configurado automaticamente)

**Nota**: O projeto utiliza Flyway para migração automática do banco de dados. As tabelas e dados iniciais são criados automaticamente na primeira execução.


---

## 🧠 Lógica do Setor e Cor por Status

| Status            | Setor   | Cor      |
| ----------------- | ------- | -------- |
| DISPONIVEL        | Setor A | Verde    |
| RESERVADA         | Setor B | Azul     |
| MANUTENCAO        | Setor C | Amarelo  |
| FALTA_PECA        | Setor D | Laranja  |
| INDISPONIVEL      | Setor E | Cinza    |
| DANOS_ESTRUTURAIS | Setor F | Vermelho |
| SINISTRO          | Setor G | Preto    |

---

## 🔄 Funcionalidades Avançadas

### 🎯 Sistema de Status Inteligente

- **Atualização automática**: Status da moto define automaticamente setor e cor
- **Validação de dados**: Campos obrigatórios e formatos validados
- **Relacionamentos**: Integridade referencial entre motos e pátios

### 📊 Relatórios e Consultas

- **Contagem por setor**: Quantidade de motos em cada setor
- **Status geral**: Resumo de todas as motos por status
- **Filtros avançados**: Busca por múltiplos critérios simultaneamente

### 🔐 Segurança

- **Spring Security**: Autenticação baseada em formulário
- **Controle de acesso**: Diferentes perfis de usuário
- **Validação de entrada**: Prevenção de ataques de injeção

### 🚀 Performance

- **Cache habilitado**: Melhora performance de consultas frequentes
- **DTOs otimizados**: Transferência eficiente de dados
- **Paginação**: Preparado para grandes volumes de dados

---

## 📌 Exemplos de Endpoints

### 🔄 MotoController

- `GET /api/motos/todos` → Lista todas as motos cadastradas (com DTOs)
- `GET /api/motos/id/{id}` → Retorna os detalhes de uma moto pelo ID
- `GET /api/motos/placa/{placa}` → Retorna os detalhes de uma moto pela placa
- `GET /api/motos/status?status=DISPONIVEL` → Lista todas as motos com status específico
- `GET /api/motos/filtro?status=&setor=&cor=` → Permite filtrar motos por status, setor e cor
- `GET /api/motos/patio/setor/{setor}/contagem` → Retorna a quantidade de motos por setor
- `GET /api/motos/patio/moto/{placa}/status` → Retorna o status individual da moto, setor e cor
- `POST /api/motos` → Cadastra uma nova moto

```json
{
  "modelo": "Honda Biz",
  "placa": "ABC1234",
  "status": "DISPONIVEL",
  "nomePatio": "Pátio Butantã"
}
```

- `PUT /api/motos/id/{id}` → Atualiza uma moto pelo ID
- `PUT /api/motos/placa/{placa}` → Atualiza uma moto pela placa
- `DELETE /api/motos/id/{id}` → Remove uma moto pelo ID
- `DELETE /api/motos/placa/{placa}` → Remove uma moto pela placa

### 🏢 PatioController

- `GET /api/patios` → Lista todos os pátios cadastrados
- `POST /api/patios` → Cadastra um novo pátio

```json
{
  "nome": "Pátio Butantã",
  "endereco": "Rua das Flores, 123 - Butantã, São Paulo - SP"
}
```

- `GET /api/patios/status` → Retorna um resumo geral do status das motos no pátio

### 👷 Usuário de Pátio

- `GET /api/usuarios` → Lista todos os usuários cadastrados
- `GET /api/usuarios/{id}` → Retorna os dados de um usuário pelo ID
- `DELETE /api/usuarios/{id}` → Exclui um usuário pelo ID

**Cadastro via Interface Web:**

- `GET /cadastro` → Página de cadastro de usuário
- `POST /cadastro` → Processa o cadastro de novo usuário

**Login via Interface Web:**

- `GET /login` → Página de login
- `POST /login` → Processa o login do usuário

### 🌐 Interface Web (Thymeleaf)

- `GET /` → Redireciona para login
- `GET /login` → Página de login
- `GET /cadastro` → Página de cadastro
- `GET /dashboard` → Dashboard principal (após login)
- `GET /motos` → Lista de motos
- `GET /motos/novo` → Formulário de nova moto
- `GET /motos/{id}` → Detalhes da moto
- `GET /patios` → Lista de pátios
- `GET /patios/novo` → Formulário de novo pátio
- `GET /patios/{id}` → Detalhes do pátio

---

## ☁️ **Deploy na Nuvem - DevOps**

### **Recursos Azure Utilizados:**

- **App Service**: Hospedagem da aplicação Spring Boot
- **Azure Database for PostgreSQL**: Banco de dados gerenciado
- **Azure CLI**: Automação da criação de recursos

### **Scripts de Automação:**

- `scripts/deploy-azure.sh` - Criação de recursos Azure
- `scripts/build.sh` - Build da aplicação
- `scripts/deploy-jar.sh` - Deploy para App Service
- `scripts/test-api.sh` - Testes automatizados

### **Configurações:**

- **Profile Cloud**: `application-cloud.properties`
- **Connection String**: Configurada automaticamente
- **Variáveis de Ambiente**: DB_USERNAME, DB_PASSWORD, SPRING_PROFILES_ACTIVE

---

## 📚 **Disciplinas Integradas**

- **JAVA ADVANCED**: API RESTful com Spring Boot
- **COMPLIANCE, QUALITY ASSURANCE & TESTS**: Testes, validações e garantia de qualidade
- **DEVOPS TOOLS & CLOUD COMPUTING**: Deploy no Azure App Service
- **MOBILE APPLICATION DEVELOPMENT**: API sendo consumida por app mobile

---

## 📹 **Vídeo de Apresentação**

- **Vídeo JAVA**: https://youtu.be/RK14AZZSbVU
- **Vídeo DEVOPS**: https://youtu.be/MrozDZ37MCI

## 🔗 **Links Importantes**

- **Aplicação Online**: https://motovision-api-8077.azurewebsites.net
- **Swagger UI**: https://motovision-api-8077.azurewebsites.net/swagger-ui.html
- **Projeto Azure DevOps**: https://dev.azure.com/RM555871/Sprint%204%20–%20Azure%20DevOps

---

## 👥 Equipe

- Eduardo Miguel Forato Monteiro – RM 555871
- Cícero Gabriel Oliveira Serafim – RM 556996
- Murillo Ari Ferreira Sant'Anna – RM 557183

---
