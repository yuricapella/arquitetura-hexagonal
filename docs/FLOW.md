# Fluxo ponta a ponta

Este documento descreve o fluxo completo da aplicação, desde a criação de um cliente até a atualização do status de CPF via Kafka e consulta do cliente atualizado.

## 1) Criação de Cliente (POST /api/v1/customers)

1. O `CustomerController` recebe a requisição com `name`, `cpf` e `zipCode`.
2. Mapeamento do DTO (`CustomerRequest`) para domínio `Customer` via `CustomerMapper` (sem endereço ainda).
3. O caso de uso `InsertCustomerUseCase` é chamado pelo `InsertCustomerInputPort`.
4. O caso de uso:
   - Consulta endereço via `FindAddressByZipCodeOutputPort` (adapter usa Feign `FindAddressByZipCodeClient`, apontando para WireMock).
   - Preenche `customer.address` com o `Address` retornado.
   - Persiste no Mongo via `InsertCustomerOutputPort` (adapter `InsertCustomerAdapter` com `CustomerRepository`).
   - Publica o CPF no Kafka via `SendCpfForValidationOutputPort` (adapter `SendCpfValidationAdapter`) no tópico `tp-cpf-validation`.
5. O controller retorna 200 OK.

## 2) Validação de CPF (serviço externo + tópico `tp-cpf-validated`)

1. Um serviço externo lê o CPF do tópico `tp-cpf-validation`, realiza a validação e publica a mensagem completa do cliente com o campo `isValidCpf` no tópico `tp-cpf-validated`.
2. O consumidor `ReceiveValidatedCpfConsumer` (Kafka) recebe a mensagem `CustomerMessage`.
3. O `CustomerMessageMapper` converte a mensagem para `Customer` (endereço é ignorado na mensagem e pode ser recuperado/atualizado quando necessário).
4. O consumidor chama o `UpdateCustomerInputPort` com o `Customer` e o `zipCode` presente na mensagem.

## 3) Atualização do Cliente (UpdateCustomerUseCase)

1. O caso de uso `UpdateCustomerUseCase`:
   - Busca o endereço atualizado pelo CEP via `FindAddressByZipCodeOutputPort`.
   - Atualiza o `Customer` com novo endereço e `isValidCpf` vindo da mensagem.
   - Persiste via `UpdateCustomerOutputPort` (adapter `UpdateCustomerAdapter`).

## 4) Consulta do Cliente (GET /api/v1/customers/{id})

1. O `CustomerController` chama o `FindCustomerByIdInputPort`.
2. O caso de uso `FindCustomerByIdUseCase` retorna o domínio `Customer` encontrado.
3. O `CustomerMapper` mapeia para `CustomerResponse` e o controller retorna 200 OK.

## Observações importantes

- WireMock precisa estar servindo os stubs em `http://localhost:8082/addresses/{zipCode}`.
- Ajuste a propriedade `yuri.client.address.url` para incluir protocolo (ex.: `http://localhost:8082/addresses`).
- Tópicos Kafka:
  - Producer: `tp-cpf-validation`
  - Consumer: `tp-cpf-validated`
- Schema da mensagem consumida (`CustomerMessage`) está em `adapters/in/consumer/message/CustomerMessage.java`.
