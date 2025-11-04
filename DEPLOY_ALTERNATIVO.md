# 🔄 Deploy Alternativo - Build Local + Upload JAR

Se o build no Cloud Shell falhar por falta de Java 21, você pode fazer o build localmente e fazer upload apenas do JAR.

---

## 🎯 Opção 1: Build Local + Deploy no Cloud Shell

### Passo 1: Build Local (na sua máquina)

```bash
# Na sua máquina local (Windows/Linux/Mac)
./gradlew clean build -x test
```

Isso vai gerar: `build/libs/mottu-api-0.0.1-SNAPSHOT.jar`

### Passo 2: Fazer Upload do JAR para Cloud Shell

1. No Azure Cloud Shell, clique no ícone de **Upload** (pasta com seta)
2. Selecione o arquivo: `build/libs/mottu-api-0.0.1-SNAPSHOT.jar`
3. Aguarde o upload completar

### Passo 3: Criar Recursos Azure (se ainda não criou)

```bash
# No Cloud Shell
./scripts/deploy-azure.sh
```

### Passo 4: Deploy do JAR

```bash
# No Cloud Shell
chmod +x scripts/deploy-jar-only.sh
./scripts/deploy-jar-only.sh
```

---

## 🎯 Opção 2: Script Completo Atualizado (com instalação de Java)

O script `deploy-azure-cloud-shell.sh` foi atualizado para instalar Java 21 automaticamente.

**Execute novamente:**

```bash
./scripts/deploy-azure-cloud-shell.sh
```

O script agora:

1. ✅ Verifica se Java 21 está instalado
2. ✅ Instala automaticamente se necessário
3. ✅ Configura JAVA_HOME
4. ✅ Faz o build
5. ✅ Faz o deploy

---

## 🎯 Opção 3: Instalar Java Manualmente no Cloud Shell

Se preferir instalar manualmente:

```bash
# Instalar Java 21
sudo apt-get update
sudo apt-get install -y openjdk-21-jdk

# Configurar JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Verificar instalação
java -version

# Depois executar o script normalmente
./scripts/deploy-azure-cloud-shell.sh
```

---

## ⚠️ Problema: "Cannot find a Java installation"

**Causa:** Azure Cloud Shell não tem Java 21 por padrão.

**Solução:**

- ✅ Script atualizado instala automaticamente
- ✅ Ou use Opção 1 (build local + upload)

---

## 📝 Recomendação

**Para a primeira vez:**

1. Tente executar o script atualizado novamente (ele instala Java automaticamente)
2. Se ainda der erro, use a Opção 1 (build local)

**Script atualizado inclui:**

- Verificação de Java 21
- Instalação automática se necessário
- Configuração de JAVA_HOME
- Build e deploy completo

---

**Última atualização**: 2025 - Entrega Final FIAP
