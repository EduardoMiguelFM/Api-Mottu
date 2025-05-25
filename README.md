# MotoVision API 🚀

### Projeto desenvolvido para o Challenge FIAP 2025 (2º ano ADS)

API RESTful construída com **Spring Boot** para **gestão de motos, pátios e usuários de pátio**, simulando a operação da startup **Mottu**, conforme o desafio oficial proposto no semestre.

---

## 🔧 Funcionalidades

- ✅ Cadastro e gerenciamento de **motos**
- ✅ Cadastro e gerenciamento de **pátios**
- ✅ Cadastro e **autenticação de usuários de pátio**
- ✅ Relacionamento entre entidades (Moto ↔ Pátio)
- 🔍 Filtros por status, setor e cor
- 🔍 Contagem de motos por setor
- 🔍 Status geral do pátio por tipo de ocorrência
- 🛠 Atualização e remoção por **ID ou placa**
- 🧭 Regras automáticas:
  - Status define **setor** e **cor**
  - Ex: `DISPONIVEL` → `Setor A` / `Verde`

---

## 🧪 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Validation
- H2 Database (persistência em disco)
- Swagger OpenAPI 3 (springdoc)
- Spring Cache (simulado)
- Docker (execução em nuvem via container)
- Azure VM Linux com Docker (DevOps)

---

## ▶️ Como Rodar

### 1. Clonar o projeto
```bash
git clone https://github.com/seu-usuario/mottu-api.git
cd mottu-api
```

### 2. Rodar localmente
```bash
./gradlew bootRun
```

### 3. Acessar
- Swagger: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

---

## 📁 Estrutura de Pastas

```
br.com.fiap.mottu_api
├── controller
├── dto
├── model
├── repository
├── service
├── exception
└── MottuApiApplication.java
```

---

## 🧠 Lógica do Setor e Cor por Status

| Status             | Setor     | Cor        |
|--------------------|-----------|------------|
| DISPONIVEL         | Setor A   | Verde      |
| RESERVADA          | Setor B   | Azul       |
| MANUTENCAO         | Setor C   | Amarelo    |
| FALTA_PECA         | Setor D   | Laranja    |
| INDISPONIVEL       | Setor E   | Cinza      |
| DANOS_ESTRUTURAIS  | Setor F   | Vermelho   |
| SINISTRO           | Setor G   | Preto      |

---


## 📌 Exemplos de Endpoints

### 🔄 MotoController
- `GET /api/motos` → Lista todas as motos cadastradas
- `GET /api/motos/id/{id}` → Retorna os detalhes de uma moto pelo ID
- `GET /api/motos/placa/{placa}` → Retorna os detalhes de uma moto pela placa
- `GET /api/motos/status?status=DISPONIVEL` → Lista todas as motos com status específico
- `GET /api/motos/filtro?status=&setor=&cor=` → Permite filtrar motos por status, setor e cor
- `GET /api/patios/setor/{setor}/contagem` → Retorna a quantidade de motos por setor
- `GET /api/patios/moto/{placa}/status` → Retorna o status individual da moto, setor e cor
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
  "nomePatio": "Pátio Butantã"
}
```
- `GET /api/patios/status` → Retorna um resumo geral do status das motos no pátio

### 👷 Usuário de Pátio
- `GET /api/usuarios` → Lista todos os usuários cadastrados
- `POST /api/usuarios/cadastro` → Realiza o cadastro de um novo usuário
```json
{
  "nome": "Carlos Junior",
  "email": "carlos@mottu.com.br",
  "senha": "senha123",
  "cpf": "46608272761",
  "funcao": "Supervisora"
}
```
- `POST /api/usuarios/login?email=&senha=` → Realiza o login de um usuário por e-mail e senha
```json
{
  "email": "carlos@mottu.com.br",
  "senha": "senha123"
}
```
- `GET /api/usuarios/{id}` → Retorna os dados de um usuário pelo ID
- `DELETE /api/usuarios/{id}` → Exclui um usuário pelo ID


---

## 👥 Equipe

- Eduardo Miguel Forato Monteiro – RM 555871
- Cícero Gabriel Oliveira Serafim – RM 556996
