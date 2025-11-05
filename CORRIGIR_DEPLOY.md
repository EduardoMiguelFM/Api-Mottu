# 🔧 Correções Necessárias - Deploy Falhou

## Problema Identificado

Java não está configurado explicitamente no App Service, mesmo com `linuxFxVersion` correto.

---

## ✅ Correção 1: Configurar Java Explicitamente

```bash
az webapp config set \
  --name motovision-api-8077 \
  --resource-group MotoVisionRG \
  --java-version "21" \
  --java-container "JAVA" \
  --java-container-version "21"
```

---

## ✅ Correção 2: Adicionar JAVA_OPTS Completo

```bash
az webapp config appsettings set \
  --name motovision-api-8077 \
  --resource-group MotoVisionRG \
  --settings \
    JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom -Dserver.port=8080" \
    SPRING_PROFILES_ACTIVE="cloud" \
    DB_USERNAME="motovisionadmin" \
    DB_PASSWORD="MotoVisionPass123!Secure@2025" \
    WEBSITES_ENABLE_APP_SERVICE_STORAGE="false"
```

---

## ✅ Correção 3: Verificar PostgreSQL

```bash
# Verificar se o servidor PostgreSQL existe e está acessível
az postgres flexible-server show \
  --resource-group MotoVisionRG \
  --name motovision-postgres-8077

# Verificar firewall
az postgres flexible-server firewall-rule list \
  --resource-group MotoVisionRG \
  --name motovision-postgres-8077
```

---

## ✅ Correção 4: Ver Logs Detalhados

```bash
# Ver logs em tempo real
az webapp log tail \
  --name motovision-api-8077 \
  --resource-group MotoVisionRG

# Ou baixar logs
az webapp log download \
  --name motovision-api-8077 \
  --resource-group MotoVisionRG \
  --log-file logs.zip
```

---

## 🔄 Após Correções

1. Reiniciar aplicação:

```bash
az webapp restart \
  --name motovision-api-8077 \
  --resource-group MotoVisionRG
```

2. Monitorar logs:

```bash
az webapp log tail \
  --name motovision-api-8077 \
  --resource-group MotoVisionRG
```

---

**Última atualização**: 2025 - Entrega Final FIAP
