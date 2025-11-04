@echo off
REM Script Completo de Testes - Todas as Entidades
REM MotoVision API - Challenge FIAP 2025
REM Testa todas as entidades antes do deploy

setlocal enabledelayedexpansion

set BASE_URL=http://localhost:8080
set API_BASE=%BASE_URL%/api

set TOTAL_TESTS=0
set PASSED_TESTS=0
set FAILED_TESTS=0

echo ========================================
echo   TESTES COMPLETOS - MOTOVISION API
echo ========================================
echo.
echo Base URL: %BASE_URL%
echo.

REM Verificar se a aplicação está rodando
echo Verificando se a aplicação está rodando...
curl -s %BASE_URL% >nul 2>&1
if errorlevel 1 (
    echo ❌ Aplicação não está rodando em %BASE_URL%
    echo Execute: gradlew.bat bootRun
    exit /b 1
)
echo ✅ Aplicação está rodando
echo.

REM Função para executar teste
:test_endpoint
set METHOD=%~1
set ENDPOINT=%~2
set DATA=%~3
set EXPECTED_STATUS=%~4
set DESCRIPTION=%~5

set /a TOTAL_TESTS+=1

echo Testando: %DESCRIPTION%
echo   %METHOD% %ENDPOINT%

if "%DATA%"=="" (
    curl -s -w "\n%%{http_code}" -X %METHOD% "%ENDPOINT%" -H "Content-Type: application/json" -H "Accept: application/json" > temp_response.txt 2>nul
) else (
    echo %DATA% > temp_data.json
    curl -s -w "\n%%{http_code}" -X %METHOD% "%ENDPOINT%" -H "Content-Type: application/json" -H "Accept: application/json" -d @temp_data.json > temp_response.txt 2>nul
    del temp_data.json
)

for /f "tokens=*" %%a in (temp_response.txt) do set LAST_LINE=%%a
set HTTP_CODE=!LAST_LINE!

if "!HTTP_CODE!"=="%EXPECTED_STATUS%" (
    echo   ✅ PASSOU (HTTP !HTTP_CODE!)
    set /a PASSED_TESTS+=1
) else (
    echo   ❌ FALHOU (Esperado: %EXPECTED_STATUS%, Recebido: !HTTP_CODE!)
    set /a FAILED_TESTS+=1
)

del temp_response.txt 2>nul
goto :eof

REM ==========================================
REM 1. TESTES DE PÁTIOS
REM ==========================================
echo === TESTES DE PÁTIOS ===
echo.

call :test_endpoint "GET" "%API_BASE%/patios" "" "200" "Listar todos os pátios"
call :test_endpoint "GET" "%API_BASE%/patios/1" "" "200" "Buscar pátio por ID"
call :test_endpoint "GET" "%API_BASE%/patios/status" "" "200" "Status geral dos pátios"

set PATIO_DATA={"nome":"Pátio Teste Automatizado","endereco":"Rua Teste, 123 - São Paulo - SP"}
call :test_endpoint "POST" "%API_BASE%/patios" "%PATIO_DATA%" "201" "Criar novo pátio"

set PATIO_UPDATE={"nome":"Pátio Atualizado","endereco":"Rua Atualizada, 456 - São Paulo - SP"}
call :test_endpoint "PUT" "%API_BASE%/patios/1" "%PATIO_UPDATE%" "200" "Atualizar pátio"

echo.

REM ==========================================
REM 2. TESTES DE USUÁRIOS
REM ==========================================
echo === TESTES DE USUÁRIOS ===
echo.

call :test_endpoint "GET" "%API_BASE%/usuarios" "" "200" "Listar todos os usuários"
call :test_endpoint "GET" "%API_BASE%/usuarios/1" "" "200" "Buscar usuário por ID"

echo.

REM ==========================================
REM 3. TESTES DE MOTOS
REM ==========================================
echo === TESTES DE MOTOS ===
echo.

call :test_endpoint "GET" "%API_BASE%/motos/todos" "" "200" "Listar todas as motos"
call :test_endpoint "GET" "%API_BASE%/motos/id/1" "" "200" "Buscar moto por ID"
call :test_endpoint "GET" "%API_BASE%/motos/placa/ABC1234" "" "200" "Buscar moto por placa"
call :test_endpoint "GET" "%API_BASE%/motos/status?status=DISPONIVEL" "" "200" "Filtrar motos por status"
call :test_endpoint "GET" "%API_BASE%/motos/filtro?status=DISPONIVEL&setor=Setor A&cor=Verde" "" "200" "Filtrar motos por múltiplos critérios"
call :test_endpoint "GET" "%API_BASE%/motos/patio/setor/Setor A/contagem" "" "200" "Contar motos por setor"
call :test_endpoint "GET" "%API_BASE%/motos/patio/moto/ABC1234/status" "" "200" "Status de moto por placa"

set MOTO_DATA={"modelo":"Honda Biz Teste","placa":"TEST123","status":"DISPONIVEL","nomePatio":"Pátio Butantã"}
call :test_endpoint "POST" "%API_BASE%/motos" "%MOTO_DATA%" "201" "Criar nova moto"

set MOTO_UPDATE={"modelo":"Honda Biz Atualizado","placa":"TEST123","status":"RESERVADA","nomePatio":"Pátio Butantã"}
call :test_endpoint "PUT" "%API_BASE%/motos/id/1" "%MOTO_UPDATE%" "200" "Atualizar moto por ID"

set MOTO_UPDATE_PLACA={"modelo":"Honda Biz Atualizado Placa","placa":"TEST123","status":"MANUTENCAO","nomePatio":"Pátio Butantã"}
call :test_endpoint "PUT" "%API_BASE%/motos/placa/TEST123" "%MOTO_UPDATE_PLACA%" "200" "Atualizar moto por placa"

echo.

REM ==========================================
REM 4. TESTES DE AUTENTICAÇÃO
REM ==========================================
echo === TESTES DE AUTENTICAÇÃO ===
echo.

set LOGIN_DATA={"email":"admin@mottu.com.br","senha":"admin123"}
call :test_endpoint "POST" "%API_BASE%/auth/login" "%LOGIN_DATA%" "200" "Login de usuário"
call :test_endpoint "GET" "%API_BASE%/auth/validate" "" "200" "Validar token"

echo.

REM ==========================================
REM RESUMO FINAL
REM ==========================================
echo ========================================
echo   RESUMO DOS TESTES
echo ========================================
echo.
echo Total de testes: !TOTAL_TESTS!
echo Testes passaram: !PASSED_TESTS!
echo Testes falharam: !FAILED_TESTS!
echo.

if !FAILED_TESTS! equ 0 (
    echo ✅ TODOS OS TESTES PASSARAM!
    echo ✅ A aplicação está pronta para deploy! 🚀
    exit /b 0
) else (
    echo ❌ Alguns testes falharam
    echo Revise os erros acima antes de fazer deploy
    exit /b 1
)

