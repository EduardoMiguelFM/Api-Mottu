@echo off
REM Script de Teste da API no Azure
REM MotoVision API - Challenge FIAP 2025

set APP_URL=https://motovision-api.azurewebsites.net/api

echo === TESTANDO API NO AZURE ===
echo URL Base: %APP_URL%
echo.

REM Aguardar a aplicação inicializar
echo Aguardando aplicação inicializar...
timeout /t 30 /nobreak

REM 1. Testar health check - Listar pátios
echo 1. Testando listagem de pátios...
curl -X GET "%APP_URL%/patios" -H "Accept: application/json"

echo.
echo.

REM 2. Testar CRUD Motos
echo 2. Testando CRUD de Motos...

REM CREATE - Criar nova moto
echo 2.1. Criando nova moto...
curl -X POST "%APP_URL%/motos" -H "Content-Type: application/json" -d "{\"modelo\": \"Honda PCX 160\", \"placa\": \"JKL-3456\", \"status\": \"DISPONIVEL\", \"patioId\": 1}"

echo.
echo.

REM READ - Listar todas as motos
echo 2.2. Listando todas as motos...
curl -X GET "%APP_URL%/motos/todos" -H "Accept: application/json"

echo.
echo.

REM READ - Buscar moto por ID
echo 2.3. Buscando moto por ID (1)...
curl -X GET "%APP_URL%/motos/id/1" -H "Accept: application/json"

echo.
echo.

REM UPDATE - Atualizar moto
echo 2.4. Atualizando moto (ID 1)...
curl -X PUT "%APP_URL%/motos/id/1" -H "Content-Type: application/json" -d "{\"modelo\": \"Honda Biz 125\", \"placa\": \"ABC-1234\", \"status\": \"MANUTENCAO\", \"patioId\": 1}"

echo.
echo.

REM Testar endpoints especiais
echo 3. Testando endpoints especiais...

REM Status por setor
echo 3.1. Contagem de motos por setor A...
curl -X GET "%APP_URL%/motos/patio/setor/A/contagem" -H "Accept: application/json"

echo.
echo.

REM Status individual da moto
echo 3.2. Status individual da moto ABC-1234...
curl -X GET "%APP_URL%/motos/patio/moto/ABC-1234/status" -H "Accept: application/json"

echo.
echo.

REM Status geral do pátio
echo 3.3. Status geral do pátio...
curl -X GET "%APP_URL%/patios/status" -H "Accept: application/json"

echo.
echo.

REM DELETE - Deletar moto (se existir ID 4)
echo 2.5. Tentando deletar moto (ID 4)...
curl -X DELETE "%APP_URL%/motos/id/4"

echo.
echo.

echo === TESTES CONCLUÍDOS ===
echo.
echo URLs importantes:
echo - API: https://motovision-api.azurewebsites.net
echo - Swagger: https://motovision-api.azurewebsites.net/swagger-ui.html
echo - Interface Web: https://motovision-api.azurewebsites.net/login

pause
