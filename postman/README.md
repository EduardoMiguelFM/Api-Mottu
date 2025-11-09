# Postman Assets

Coloque nesta pasta os arquivos exportados do Postman usados pelo pipeline de CI:

- `MotoVision.postman_collection.json`
- `MotoVision.env.postman_environment.json`

Os arquivos devem estar no formato **Collection v2.1** e **Environment**.
Certifique-se de que quaisquer credenciais sensíveis sejam retiradas antes do commit;
use variáveis no environment e configure os valores reais como secrets no Azure DevOps.

## Executar localmente com Newman

```bash
# instalar uma vez (caso ainda não tenha)
npm install -g newman

# executar usando os arquivos exportados
newman run "postman/MotoVision API Tests.postman_collection.json" \
  --environment "postman/MotoVision.postman_environment.json" \
  --env-var base_url=http://localhost:8080
```

Adapte `base_url` para a URL do ambiente que deseja testar
(por exemplo, `https://motovision-api-8077.azurewebsites.net`).
