 # Hexagonal — API de Clientes

 Documentação prática para uso rápido. Para detalhes completos, consulte os arquivos vinculados nas seções abaixo.

 - Guia de setup detalhado: [SETUP.md](SETUP.md)
 - Resumo completo do projeto: [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)
 - Arquitetura (Ports & Adapters): [ARCHITECTURE.md](ARCHITECTURE.md)
 - Fluxo ponta a ponta: [FLOW.md](FLOW.md)
 - Endpoints com exemplos: [endpoints.md](endpoints.md)
 - Anotações e dicas úteis: [anotacoes.md](anotacoes.md)
 - Histórico de ajustes (Kafka): [alteracoes-projeto-professor.md](alteracoes-projeto-professor.md)

 ## Quick Start

 1) Suba a infraestrutura (Mongo, Kafka, Kafdrop, Mongo Express)
 - Entre em `docker-local/` e execute: `docker-compose up -d`
 - Kafdrop: http://localhost:9000 | Mongo Express: http://localhost:8083

 2) Inicie o WireMock (mock de CEP)
 - Execute a classe `com.yuri.hexagonal.adapters.out.client.wiremock.WireMockStandaloneRunner` (porta 8082)

 3) Rode a aplicação
 - `./mvnw spring-boot:run` ou execute `com.yuri.hexagonal.HexagonalApplication`
 - Base URL: `http://localhost:8081`

 Detalhes e troubleshooting: ver [SETUP.md](SETUP.md).

 ## Endpoints (resumo)

 Base path: `/api/v1/customers`

 - POST `/api/v1/customers` — cria cliente com `name`, `cpf`, `zipCode`. Retorna 200.
 - GET `/api/v1/customers/{id}` — retorna dados do cliente. Retorna 200.
 - PUT `/api/v1/customers/{id}` — atualiza cliente. Retorna 204.
 - DELETE `/api/v1/customers/{id}` — remove cliente. Retorna 204.

 Exemplos de requisição/resposta: [endpoints.md](endpoints.md).

 ## Fluxo resumido

 1) Criação: busca endereço via Feign/WireMock, persiste no Mongo e publica CPF no Kafka (`tp-cpf-validation`).
 2) Validação: serviço externo publica status em `tp-cpf-validated`.
 3) Consumo: `ReceiveValidatedCpfConsumer` atualiza o cliente com `isValidCpf`.
 4) Consulta: GET por ID retorna a versão atualizada.

 Para o fluxo completo com detalhes: [FLOW.md](FLOW.md).

## Tecnologias

- Spring Boot 3 • Java 21 • Web • Validation • MongoDB • Kafka • OpenFeign • MapStruct • Lombok • WireMock