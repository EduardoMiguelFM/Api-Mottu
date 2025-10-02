# 🏍️ MotoVision API - Azure App Service

## 📋 Descrição da Solução

A **MotoVision API** é uma aplicação Java Spring Boot desenvolvida para gestão completa de motos, pátios e usuários. O sistema permite:

- **Gestão de Motos**: CRUD completo com controle de status, localização e manutenção
- **Gestão de Pátios**: Administração de locais de estacionamento com endereços
- **Gestão de Usuários**: Sistema de autenticação e autorização
- **Relatórios e Estatísticas**: Dashboards com métricas operacionais
- **API REST**: Endpoints documentados com Swagger UI

## 💼 Benefícios para o Negócio

### **Problemas Resolvidos:**

- **Controle de Frota**: Rastreamento em tempo real de todas as motos
- **Otimização de Espaço**: Gestão eficiente de pátios e setores
- **Redução de Perdas**: Controle de status previne extravios
- **Automação de Processos**: Reduz trabalho manual e erros
- **Escalabilidade**: Suporte a crescimento da operação

### **Melhorias Implementadas:**

- **Disponibilidade 99.9%**: Hospedagem na nuvem Azure
- **Performance**: Banco PostgreSQL otimizado
- **Segurança**: Autenticação e autorização robustas
- **Monitoramento**: Logs e métricas em tempo real
- **Manutenibilidade**: Código limpo e documentado

## 🚀 Deploy no Azure

### **Pré-requisitos:**

- Azure CLI instalado
- Conta Azure ativa
- Java 21+ instalado
- Gradle instalado

### **1. Configurar Azure CLI:**

```bash
# Login no Azure
az login

# Verificar subscription
az account show
```

### **2. Criar Recursos Azure:**

```bash
# Linux/Mac
chmod +x scripts/azure-setup.sh
./scripts/azure-setup.sh

# Windows
scripts\azure-setup.bat
```

### **3. Deploy da Aplicação:**

```bash
# Linux/Mac
chmod +x scripts/deploy-to-azure.sh
./scripts/deploy-to-azure.sh

# Windows
scripts\deploy-to-azure.bat
```

## 🗄️ Banco de Dados

### **Azure Database for PostgreSQL:**

- **Tipo**: PaaS (Platform as a Service)
- **Tier**: Burstable (B1ms)
- **Storage**: 32GB
- **Backup**: Automático
- **SSL**: Obrigatório

### **Script do Banco:**

O arquivo `scripts/script_bd.sql` contém:

- DDL completo das tabelas
- Índices para performance
- Dados iniciais para teste
- Comentários explicativos

## 📚 API Endpoints

### **Base URL:**

- **Local**: `http://localhost:8080/api`
- **Azure**: `https://mottu-api-fiap.azurewebsites.net/api`

### **Documentação Swagger:**

- **Local**: `http://localhost:8080/swagger-ui.html`
- **Azure**: `https://mottu-api-fiap.azurewebsites.net/swagger-ui.html`

## 🧪 Testes da API

### **Testar CRUD Completo:**

```bash
# 1. Criar (CREATE)
curl -X POST https://mottu-api-fiap.azurewebsites.net/api/motos \
  -H "Content-Type: application/json" \
  -d '{"modelo":"Yamaha Factor","placa":"CRUD123","status":"DISPONIVEL","setor":"Setor B","cor_setor":"Azul","patio":{"id":1}}'

# 2. Ler (READ)
curl https://mottu-api-fiap.azurewebsites.net/api/motos/placa/CRUD123

# 3. Atualizar (UPDATE)
curl -X PUT https://mottu-api-fiap.azurewebsites.net/api/motos/id/1 \
  -H "Content-Type: application/json" \
  -d '{"modelo":"Yamaha Factor 150","placa":"CRUD123","status":"MANUTENCAO","setor":"Setor C","cor_setor":"Amarelo","patio":{"id":1}}'

# 4. Deletar (DELETE)
curl -X DELETE https://mottu-api-fiap.azurewebsites.net/api/motos/id/1
```

## 🎯 Critérios de Avaliação Atendidos

### **✅ Requisitos Obrigatórios:**

1. **Descrição da Solução**: ✅ Sistema completo de gestão de motos
2. **Benefícios para o Negócio**: ✅ Controle de frota e otimização
3. **Banco de Dados em Nuvem**: ✅ Azure Database for PostgreSQL (PaaS)
4. **CRUD Completo**: ✅ Implementado para Motos e Pátios
5. **2+ Registros Reais**: ✅ Dados de teste inseridos
6. **Código no GitHub**: ✅ Repositório separado
7. **PDF com Informações**: ✅ Documentação completa

### **✅ Requisitos Específicos App Service:**

1. **Recursos via Azure CLI**: ✅ Todos os scripts criados
2. **Scripts de Recursos**: ✅ azure-setup.sh/bat
3. **Scripts de Deploy**: ✅ deploy-to-azure.sh/bat
4. **Scripts de Build**: ✅ build-and-deploy.sh/bat

---

**Desenvolvido para FIAP - DevOps Tools & Cloud Computing - Sprint 3**
