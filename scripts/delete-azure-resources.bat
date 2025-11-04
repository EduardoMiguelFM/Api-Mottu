@echo off
REM Script para Deletar Recursos Azure
REM MotoVision API - Challenge FIAP 2025
REM Use este script para limpar recursos antes de recriar

setlocal enabledelayedexpansion

set RESOURCE_GROUP=MotoVisionRG

echo ========================================
echo   DELETAR RECURSOS AZURE
echo   MotoVision API
echo ========================================
echo.

REM Verificar se está logado no Azure
az account show >nul 2>&1
if errorlevel 1 (
    echo ❌ Não autenticado no Azure. Execute: az login
    exit /b 1
)

REM Verificar se Resource Group existe
az group show --name %RESOURCE_GROUP% >nul 2>&1
if errorlevel 1 (
    echo ⚠️  Resource Group '%RESOURCE_GROUP%' não encontrado.
    echo Nada para deletar.
    exit /b 0
)

echo ATENÇÃO: Este script vai deletar TODOS os recursos no Resource Group: %RESOURCE_GROUP%
echo.
echo Recursos que serão deletados:
az resource list --resource-group %RESOURCE_GROUP% --query "[].{Nome:name, Tipo:type}" -o table 2>nul
echo.
echo ⚠️  Esta ação é IRREVERSÍVEL!
echo.
set /p CONFIRM="Tem certeza que deseja continuar? (digite SIM para confirmar): "

if not "!CONFIRM!"=="SIM" (
    echo Operação cancelada.
    exit /b 0
)

echo.
echo Deletando Resource Group e todos os recursos...
echo Isso pode demorar alguns minutos...
echo.

REM Deletar Resource Group (deleta tudo dentro dele)
az group delete --name %RESOURCE_GROUP% --yes --no-wait

if %ERRORLEVEL% equ 0 (
    echo.
    echo ✅ Delete iniciado com sucesso!
    echo.
    echo A exclusão está sendo processada em background.
    echo Pode levar alguns minutos para completar.
    echo.
    echo Para verificar o status:
    echo   az group show --name %RESOURCE_GROUP%
    echo.
    echo Para verificar se foi deletado:
    echo   az group list --query "[?name=='%RESOURCE_GROUP%']" -o table
    echo.
    echo Após a exclusão, você pode executar o deploy novamente:
    echo   scripts\deploy-azure-cloud-shell.sh
) else (
    echo.
    echo ❌ Erro ao deletar recursos!
    exit /b 1
)

