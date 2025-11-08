# Postman Assets

Coloque nesta pasta os arquivos exportados do Postman usados pelo pipeline de CI:

- `MotoVision.postman_collection.json`
- `MotoVision.env.postman_environment.json`

Os arquivos devem estar no formato **Collection v2.1** e **Environment**.
Certifique-se de que quaisquer credenciais sensíveis sejam retiradas antes do commit;
use variáveis no environment e configure os valores reais como secrets no Azure DevOps.
