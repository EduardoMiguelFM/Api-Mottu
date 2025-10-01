@echo off
REM Script de Build da Aplicação
REM Mottu API - Challenge FIAP 2025

echo === BUILD DA APLICAÇÃO MOTTU API ===
echo.

REM Verificar se o Gradle está disponível
if not exist "gradlew.bat" (
    echo Erro: gradlew.bat não encontrado. Execute este script na raiz do projeto.
    pause
    exit /b 1
)

REM Limpar build anterior
echo 1. Limpando build anterior...
gradlew.bat clean

REM Fazer build da aplicação (sem testes para acelerar)
echo 2. Compilando aplicação...
gradlew.bat build -x test

REM Verificar se o build foi bem-sucedido
if %ERRORLEVEL% equ 0 (
    echo.
    echo ✅ BUILD CONCLUÍDO COM SUCESSO!
    echo.
    echo Arquivos gerados:
    echo - JAR: build\libs\mottu-api-0.0.1-SNAPSHOT.jar
    echo - JAR Plain: build\libs\mottu-api-0.0.1-SNAPSHOT-plain.jar
    echo.
    echo Próximos passos:
    echo 1. Para testar localmente: gradlew.bat bootRun
    echo 2. Para deploy no Azure: execute os scripts em scripts\
    echo 3. Para testar API: scripts\test-api.bat
) else (
    echo.
    echo ❌ ERRO NO BUILD!
    echo Verifique os logs acima para identificar o problema.
    pause
    exit /b 1
)
