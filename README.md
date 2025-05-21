# MotoVision API 🚀

### Projeto desenvolvido para o Challenge FIAP 2025 (2º ano ADS)

API RESTful construída com **Spring Boot** para **gestão de motos, pátios e usuários de pátio**, simulando a operação da startup **Mottu**, conforme o desafio oficial proposto no semestre.

---

## 🔧 Funcionalidades

- ✅ Cadastro e gerenciamento de **motos**
- ✅ Cadastro e gerenciamento de **pátios**
- ✅ Cadastro e **autenticação de usuários de pátio**
- ✅ Relacionamento entre entidades (Moto ↔ Pátio)
- 🔍 Filtros por **status**, **setor** e **cor**
- 🛠 Atualização e remoção por **ID ou placa**
- 🧭 Campos adicionais gerados automaticamente:
  - Status operacional (`enum`)
  - Coordenada GPS simulada
  - Setor (A-G)
  - Cor (baseado no status)

---

## 🧪 Tecnologias Utilizadas

- Java 21 (compatível com Spring Boot 3.2.5)
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- Spring Validation (Jakarta)
- H2 Database
- Swagger OpenAPI 3 (`springdoc-openapi-starter-webmvc-ui:2.5.0`)
- Spring Cache (com HikariCP)
- Gradle como gerenciador de build


---

## ▶️ Como Rodar

### 1. Clonar o projeto
- bash
- git clone https://github.com/seu-usuario/mottu-api.git
- cd mottu-api
### 2. Executar com Gradle
- ./gradlew bootRun
### 3. Acessar a API
- Swagger: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

## 📁 Estrutura de Pastas

br.com.fiap.mottu_api
├── controller         // Endpoints REST
├── dto               // Objetos de Transferência (DTOs)
├── model             // Entidades JPA
├── repository        // Interfaces JPA
├── service           // Regras de negócio
├── exception         // Tratamento centralizado de erros
└── MottuApiApplication.java


## 📌 Exemplos de Endpoints

##🔄 Moto
GET /api/motos/id/{id}

GET /api/motos/placa/{placa}

GET /api/motos/status?status=DISPONIVEL

GET /api/motos/filtro?status=&setor=&cor=

POST /api/motos

PUT /api/motos/id/{id}

DELETE /api/motos/placa/{placa}

## 🏢 Pátio
GET /api/patios

POST /api/patios

## 👷 Usuário de Pátio
GET /api/usuarios

POST /api/usuarios/cadastro

POST /api/usuarios/login?email=&senha=

GET /api/usuarios/{id}

DELETE /api/usuarios/{id}


## 👥 Equipe

- Eduardo Miguel Forato Monteiro – RM 555871
- Cícero Gabriel Oliveira Serafim – RM 556996
