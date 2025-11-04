@echo off
REM Script para Deploy do JAR no Azure App Service
REM MotoVision API - Challenge FIAP 2025

set APP_NAME=motovision-api
set RESOURCE_GROUP=MotoVisionRG
set JAR_FILE=build\libs\mottu-api-0.0.1-SNAPSHOT.jar

echo === DEPLOY DO JAR PARA AZURE APP SERVICE ===
echo.

REM Verificar se o JAR existe
if not exist "%JAR_FILE%" (
    echo ❌ JAR não encontrado: %JAR_FILE%
    echo Execute primeiro: scripts\build.bat
    pause
    exit /b 1
)

echo ✅ JAR encontrado: %JAR_FILE%
echo.

REM Fazer deploy usando Azure CLI
echo 1. Fazendo deploy do JAR...
az webapp deploy --resource-group %RESOURCE_GROUP% --name %APP_NAME% --src-path %JAR_FILE% --type jar

if %ERRORLEVEL% equ 0 (
    echo.
    echo ✅ DEPLOY CONCLUÍDO COM SUCESSO!
    echo.
    echo URLs da aplicação:
    echo - API: https://%APP_NAME%.azurewebsites.net
    echo - Swagger: https://%APP_NAME%.azurewebsites.net/swagger-ui.html
    echo - Interface Web: https://%APP_NAME%.azurewebsites.net/login
    echo.
    echo Aguarde alguns minutos para a aplicação inicializar...
    echo Execute os testes: scripts\test-api.bat
) else (
    echo.
    echo ❌ ERRO NO DEPLOY!
    echo Verifique se os recursos Azure foram criados corretamente.
    pause
    exit /b 1
)
