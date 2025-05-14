# MotoVision API 🚀

### Projeto desenvolvido para o Challenge FIAP 2025 (2º ano ADS)

API RESTful construída com Spring Boot para **gestão de motos e pátios**, simulando a estrutura de uma solução utilizada pela startup **Mottu**, com base no desafio oficial proposto no semestre.

---

## 🔧 Funcionalidades

- Cadastro e gerenciamento de motos
- Cadastro e gerenciamento de pátios
- Busca por status da moto
- Atualização e remoção por ID ou placa
- Campos adicionais como:
  - Status operacional (enum)
  - Coordenada GPS
  - Última manutenção
  - Descrição do problema

---

## 🧪 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Validation
- H2 Database
- Swagger OpenAPI 3 (springdoc)
- Spring Cache (simulado)

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
- ├── controller
- ├── dto
- ├── model
- ├── repository
- ├── service
- ├── exception
- └── ApiCp2Application.java


## 📌 Exemplos de Endpoints

- GET /api/motos/id/{{id}}
- GET /api/motos/placa/{{placa}}
- GET /api/motos/status?status=DISPONIVEL
- POST /api/motos
- PUT /api/motos/id/{{id}}
- DELETE /api/motos/placa/{{placa}}


## 👥 Equipe

- Eduardo Miguel Forato Monteiro – RM 555871
- Cícero Gabriel Oliveira Serafim – RM 556996
