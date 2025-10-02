# ===============================================
# Dockerfile para Azure App Service
# MotoVision API - FIAP DevOps Sprint 3
# ===============================================

# Etapa 1: build da aplicação
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiar arquivos de configuração do Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copiar código fonte
COPY src src

# Build da aplicação
RUN chmod +x gradlew && ./gradlew clean build -x test

# Etapa 2: imagem final otimizada para Azure
FROM eclipse-temurin:21-jre-alpine

# Instalar dependências necessárias
RUN apk add --no-cache \
    curl \
    tzdata

# Configurar timezone
ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Cria usuário não-root (requisito Azure)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /home/appuser

# Copia o jar gerado
COPY --from=build /app/build/libs/mottu-api-0.0.1-SNAPSHOT.jar app.jar

# Configurar permissões
RUN chown -R appuser:appgroup . && chmod -R 755 .

# Mudar para usuário não-root
USER appuser

# Configurações JVM otimizadas para Azure
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"

# Porta do Azure App Service
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
