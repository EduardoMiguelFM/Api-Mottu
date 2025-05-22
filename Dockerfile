# Etapa 1: build da aplicação
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

# Etapa 2: imagem final
FROM eclipse-temurin:21-jre-alpine

# Cria usuário não-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /home/appuser

# Copia o jar gerado
COPY --from=build /app/build/libs/mottu-api-0.0.1-SNAPSHOT.jar app.jar

# Copia o banco H2 
COPY --from=build /app/data ./data

RUN chown -R appuser:appgroup . && chmod -R 755 .

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
