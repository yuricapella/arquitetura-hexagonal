# Arquitetura (Hexagonal)

Este documento detalha como o projeto organiza suas camadas utilizando o estilo Hexagonal (Ports & Adapters).

## Camadas

- Domínio (`application/core/domain`)
  - Modelos de negócio puros: `Customer`, `Address`.
  - Sem dependências de frameworks.

- Casos de uso (`application/core/usecase`)
  - Orquestram regras de negócio e acesso a portas de saída.
  - `InsertCustomerUseCase`: busca endereço por CEP (porta de saída), persiste e publica CPF para validação.
  - `FindCustomerByIdUseCase`: recupera cliente por ID.
  - `UpdateCustomerUseCase`: atualiza dados e endereço.
  - `DeleteCustomerByIdUseCase`: remove cliente.

- Portas (`application/ports`)
  - Entrada (`ports/in`): contratos consumidos por adaptadores de entrada (REST, Kafka).
    - `InsertCustomerInputPort`, `FindCustomerByIdInputPort`, `UpdateCustomerInputPort`, `DeleteCustomerByIdInputPort`.
  - Saída (`ports/out`): contratos que os casos de uso dependem para falar com mundo externo.
    - `InsertCustomerOutputPort`, `FindCustomerByIdOutputPort`, `UpdateCustomerOutputPort`, `DeleteCustomerByIdOutputPort`.
    - `FindAddressByZipCodeOutputPort`, `SendCpfForValidationOutputPort`.

- Adaptadores de entrada (`adapters/in`)
  - REST Controller: `CustomerController` expõe `/api/v1/customers`.
  - Kafka Consumer: `ReceiveValidatedCpfConsumer` consome `tp-cpf-validated` e chama `UpdateCustomerInputPort`.
  - Mappers DTO: `CustomerMapper`, `CustomerMessageMapper`.

- Adaptadores de saída (`adapters/out`)
  - Persistência (Mongo): `CustomerRepository`, `CustomerEntity`, `AddressEntity`, `CustomerEntityMapper`.
  - Cliente externo (CEP) via Feign: `FindAddressByZipCodeClient` + `AddressResponseMapper`.
  - Mensageria Kafka (Producer): `SendCpfValidationAdapter` publica em `tp-cpf-validation`.

- Configuração (`config`)
  - Beans de casos de uso: `InsertCustomerConfig`, `FindCustomerByIdConfig`, `UpdateCustomerConfig`, `DeleteCustomerByIdConfig` (fábricas dos use cases injetando as portas de saída).
  - Kafka: `KafkaConsumerConfig` (factory para listeners e deserialização JSON).

## Dependências principais

- Spring Boot Web e Validation para REST.
- Spring Data MongoDB para persistência.
- Spring Kafka para Producer/Consumer.
- Spring Cloud OpenFeign para client HTTP.
- MapStruct para mapeamento entre entidades/DTOs.
- Lombok para reduzir boilerplate.
- WireMock (dev) para simular serviço de CEP.

## Diretrizes de desenho

- O domínio e casos de uso não conhecem detalhes de infraestrutura.
- Portas definem contratos. Adaptadores implementam os contratos.
- Controller/Consumer dependem apenas de portas de entrada.
- Use cases dependem apenas de portas de saída.
- Mappers do MapStruct mantém conversão consistente entre camadas.
