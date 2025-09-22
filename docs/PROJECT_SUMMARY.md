# Projeto Hexagonal — Resumo Completo

Este documento consolida a visão geral do projeto, estrutura de pastas, endpoints, classes e responsabilidades (portas e adaptadores), configurações (MongoDB, Kafka, WireMock) e o fluxo ponta a ponta.

## Visão Geral

- Framework: Spring Boot 3.3.3 (Java 21)
- Principais dependências: Web, Validation, Spring Data MongoDB, Spring Kafka, Spring Cloud OpenFeign, MapStruct, Lombok, WireMock
- Arquitetura: Hexagonal (Ports & Adapters)
- Persistência: MongoDB
- Mensageria: Kafka (tópicos: `tp-cpf-validation` e `tp-cpf-validated`)
- Cliente externo simulado: WireMock (endereço por CEP)

Arquivos relevantes:
- `pom.xml` — dependências e plugins
- `docker-local/docker-compose.yml` — serviços de infraestrutura (Mongo, Kafka, etc.)
- `src/main/resources/application.properties` — propriedades da aplicação
- `src/main/resources/wiremock/mappings` — stubs do WireMock

## Estrutura do Projeto (pastas-chave)

- `com.yuri.hexagonal`
  - `HexagonalApplication.java` — entrypoint (`@SpringBootApplication`, `@EnableFeignClients`)
  - `adapters/` — adaptadores de entrada e saída
    - `in/`
      - `controller/` — REST API
        - `CustomerController.java`
        - `mapper/CustomerMapper.java`
        - `request/CustomerRequest.java`
      - `consumer/` — Kafka consumer
        - `ReceiveValidatedCpfConsumer.java`
        - `mapper/CustomerMessageMapper.java`
        - `message/CustomerMessage.java`
    - `out/`
      - `InsertCustomerAdapter.java`, `FindCustomerByIdAdapter.java`, `UpdateCustomerAdapter.java`, `DeleteCustomerByIdAdapter.java`
      - `FindAddressByZipCodeAdapter.java` (chama o Feign client)
      - `SendCpfValidationAdapter.java` (Kafka producer)
      - `client/` — Feign + mapeamentos
        - `FindAddressByZipCodeClient.java`
        - `mapper/AddressResponseMapper.java`
        - `response/AddressResponse.java`, `CustomerResponse.java`
        - `wiremock/WireMockStandaloneRunner.java` (starter do servidor WireMock local)
      - `repository/` — persistência Mongo
        - `CustomerRepository.java`
        - `entity/CustomerEntity.java`, `entity/AddressEntity.java`
        - `mapper/CustomerEntityMapper.java`
  - `application/`
    - `core/`
      - `domain/Address.java`, `domain/Customer.java`
      - `usecase/InsertCustomerUseCase.java`, `FindCustomerByIdUseCase.java`, `UpdateCustomerUseCase.java`, `DeleteCustomerByIdUseCase.java`
    - `ports/`
      - `in/` — `InsertCustomerInputPort`, `FindCustomerByIdInputPort`, `UpdateCustomerInputPort`, `DeleteCustomerByIdInputPort`
      - `out/` — `InsertCustomerOutputPort`, `FindCustomerByIdOutputPort`, `UpdateCustomerOutputPort`, `DeleteCustomerByIdOutputPort`, `FindAddressByZipCodeOutputPort`, `SendCpfForValidationOutputPort`
  - `config/`
    - `InsertCustomerConfig.java`, `FindCustomerByIdConfig.java`, `UpdateCustomerConfig.java`, `DeleteCustomerByIdConfig.java`
    - `KafkaConsumerConfig.java`

## Endpoints (REST)

Controller: `adapters.in.controller.CustomerController`
Base path: `/api/v1/customers`

1) Criar cliente
- POST `/api/v1/customers`
- Body:
  - `name` (string, obrigatório)
  - `cpf` (string, obrigatório)
  - `zipCode` (string, obrigatório)
- Retorno: 200 OK (sem corpo)
- Fluxo: chama `InsertCustomerUseCase` que busca endereço via Feign (WireMock), persiste no Mongo e publica o CPF no Kafka (`tp-cpf-validation`).

2) Buscar cliente por ID
- GET `/api/v1/customers/{id}`
- Retorno: 200 OK com `CustomerResponse` (id, name, zipCode, cpf, isValidCpf, address mapeado)

3) Atualizar cliente
- PUT `/api/v1/customers/{id}`
- Body: mesmo de criação
- Retorno: 204 No Content
- Fluxo: chama `UpdateCustomerUseCase` (atualiza endereço pelo CEP, persiste no Mongo)

4) Remover cliente
- DELETE `/api/v1/customers/{id}`
- Retorno: 204 No Content

Detalhes e exemplos de requisição estão em `endpoints.md`.

## Ports & Adapters (Hexagonal)

- Ports de Entrada (`application.ports.in`): definem contratos para casos de uso consumidos pelos adaptadores de entrada (Controller e Consumer)
  - Insert, FindById, Update, Delete
- Ports de Saída (`application.ports.out`): definem contratos para integrações externas e persistência
  - FindAddressByZipCode, Insert, FindById, Update, Delete, SendCpfForValidation
- Use cases (`application.core.usecase`): implementam regras de negócio orquestrando ports de saída
  - `InsertCustomerUseCase`: busca endereço, salva, publica CPF
  - `FindCustomerByIdUseCase`: busca por id
  - `UpdateCustomerUseCase`: atualiza dados e endereço
  - `DeleteCustomerByIdUseCase`: remove por id
- Adapters de Entrada (`adapters.in`)
  - REST Controller: recebe HTTP, valida request e chama ports in
  - Kafka Consumer: recebe mensagem validada e chama `UpdateCustomerInputPort`
- Adapters de Saída (`adapters.out`)
  - Repositório Mongo, Feign Client para CEP (WireMock), Kafka Producer

## Integrações e Configurações

- MongoDB
  - `application.properties`: host `localhost`, porta `27017`, database `hexagonal`, user `root`, password `example`
  - Docker: serviço `mongo` e `mongo-express` (UI em `http://localhost:8083`)
- Kafka
  - Docker: serviços `zookeeper`, `kafka` (portas 9092/9093), `kafdrop` (UI em `http://localhost:9000`)
  - Tópicos utilizados:
    - Producer envia para `tp-cpf-validation`
    - Consumer lê de `tp-cpf-validated`
  - `KafkaConsumerConfig` configura o container de listeners
- WireMock
  - Stubs em `src/main/resources/wiremock/mappings/*.json`
  - Runner: `adapters.out.client.wiremock.WireMockStandaloneRunner` inicializa em `http://localhost:8082`
  - Feign URL: `yuri.client.address.url` (usar `http://localhost:8082/addresses`)

## Fluxo ponta a ponta (alto nível)

1) POST `/api/v1/customers` com `name`, `cpf`, `zipCode`
2) `InsertCustomerUseCase` chama `FindAddressByZipCodeOutputPort` (Feign/WireMock) e obtém endereço
3) Persiste o `Customer` no Mongo via `InsertCustomerOutputPort`
4) Publica o `cpf` no Kafka (`tp-cpf-validation`)
5) Um serviço externo valida o CPF e publica em `tp-cpf-validated` a mensagem `CustomerMessage`
6) `ReceiveValidatedCpfConsumer` consome a mensagem e chama `UpdateCustomerInputPort` para atualizar o registro com `isValidCpf`
7) GET `/api/v1/customers/{id}` retorna a versão atualizada

## Referências rápidas

- Endpoints: `endpoints.md`
- Observações e comandos úteis: `anotacoes.md`
- Alterações de Kafka (histórico): `alteracoes-projeto-professor.md`
- Passo a passo de setup detalhado: `SETUP.md`
- Arquitetura detalhada: `ARCHITECTURE.md`
- Fluxo detalhado: `FLOW.md`
