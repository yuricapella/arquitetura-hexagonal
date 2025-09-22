# Setup detalhado

Este guia descreve como iniciar toda a infraestrutura (MongoDB, Kafka, Kafdrop, Mongo Express) e os auxiliares (WireMock), além de executar a aplicação localmente.

Requisitos mínimos:
- Docker e Docker Compose
- Java 21
- Maven 3.9+

## 1) Subir infraestrutura com Docker Compose

Na raiz do projeto há o arquivo `docker-local/docker-compose.yml` com os serviços:
- zookeeper
- kafka (ports 9092 e 9093)
- kafdrop (UI Kafka em http://localhost:9000)
- mongo (porta 27017) com usuário root e senha example
- mongo-express (UI Mongo em http://localhost:8083)

Comandos:
- Entrar no diretório de compose: `docker-local/`
- Subir os serviços: `docker-compose up -d`
- Verificar containers: `docker ps`
- Logs (exemplo para Kafka): `docker-compose logs -f kafka`

Credenciais Mongo:
- user: `root`
- password: `example`

Banco utilizado pela app (config em `application.properties`):
- database: `hexagonal`

## 2) WireMock (mock do serviço de CEP)

Existem stubs em `src/main/resources/wiremock/mappings/*.json`.

Para rodar um servidor WireMock local dedicado:
- Executar a classe `com.yuri.hexagonal.adapters.out.client.wiremock.WireMockStandaloneRunner` (porta 8082)
- A aplicação espera a URL configurada em `application.properties` na chave `yuri.client.address.url`

Observações do WireMock (ver `anotacoes.md`):
- O WireMock precisa apontar para a pasta anterior a `/mappings`
- Ao apontar diretamente para `/mappings`, nenhum stub é carregado

## 3) Variáveis de configuração (application.properties)

Arquivo: `src/main/resources/application.properties`
- `server.port=8081`
- `yuri.client.address.url=localhost:8082/addresses`
- `spring.data.mongodb.*` conforme Docker (host, porta 27017, user root, pass example, auth DB admin)
- `spring.kafka.bootstrap-servers=localhost:9092`

## 4) Executar a aplicação

- Via Maven: `./mvnw spring-boot:run`
- Via IDE: executar `com.yuri.hexagonal.HexagonalApplication`

A aplicação sobe por padrão em `http://localhost:8081`.

## 5) Testar endpoints

Exemplos detalhados estão em `endpoints.md`. Fluxo rápido:
- Criar cliente (POST `/api/v1/customers`)
- Buscar por ID (GET `/api/v1/customers/{id}`)
- Atualizar por ID (PUT `/api/v1/customers/{id}`)
- Remover (DELETE `/api/v1/customers/{id}`)

## 6) Kafka

- UI Kafdrop: http://localhost:9000
- Tópicos usados:
  - Produção: `tp-cpf-validation`
  - Consumo: `tp-cpf-validated`

Consumidor configurado em `config/KafkaConsumerConfig.java` e listener em `adapters/in/consumer/ReceiveValidatedCpfConsumer.java`.

Publicar mensagem de teste (exemplo usando um cliente ou Kafdrop):
- Topic: `tp-cpf-validated`
- Mensagem JSON (conforme `adapters/in/consumer/message/CustomerMessage.java`):
```json
{
  "id": "<id válido do Mongo>",
  "name": "Nome Teste",
  "zipCode": "38400000",
  "cpf": "12345678910",
  "isValidCpf": true
}
```

## 7) Mongo Shell (pegar id customer)

- Acessar container: `docker exec -it <CONTAINER_ID_MONGO> /bin/bash`
- Entrar no shell: `mongosh -u root -p` (senha `example`)
- Selecionar DB: `use hexagonal`
- Listar coleções: `show collections`
- Consultar: `db.customers.find()`
