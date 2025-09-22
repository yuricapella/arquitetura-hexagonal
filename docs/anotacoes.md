executando wiremock e pegando os mocks, só foi possivel com os arquivos json dentro do resources na pasta nomeada especificamente /mappings
quando roda o programa parece que é localizado os arquivos pela pasta target e eles só são identificados em resources.
e para o wiremock identificar eles, precisa estar dentro de mappings, então ficou: src/main/resources/wiremock/mappings/address.json

faz requisição em http://localhost:8082/addresses/38400000
e o resultado vem igual jsonBody do json mock
{
"street": "Rua Hexagonal",
"city": "Uberlândia",
"state": "Minas Gerais"
}

Observação: o WireMock exige que o path aponte para o diretório anterior a /mappings.
Ao apontar diretamente para /mappings, nenhum stub é carregado


----------
## Fluxo mongo db
primeiro entrar na pasta docker-local na raiz do projeto

rodar o arquivo compose:
docker-compose up

listar as imagens docker ativas:
docker ps

pegar o id do container mongo e rodar comando:
docker exec -it ID /bin/bash

para sair
exit

dentro do mongo usar o comando:
mongosh -u root -p 

para sair
ctrl c

utilizar a senha configurada no docker-compose.yml
MONGO_INITDB_ROOT_PASSWORD: example


## Comandos docker compose
Mostra logs especificos de kafka
docker-compose logs -f kafka


## Kafka
adicionar em kafkalytic, plugin do intellij em topics
tp-cpf-validated e tp-cpf-validation


para testar se a mensagem está indo corretamente e validando cpf no topico
tp-cpf-validated
no kafkalytic, clica com botao direito no tópico
Publish single message
{
"id": "68d1c2b8a9201a559921f9a6",
"name": "Yuri atualizado passou kafka",
"zipCode": "38400000",
"cpf": "12345678910",
"isValidCpf": true
}
ao fazer get
http://localhost:8081/api/v1/customers/68d1c2b8a9201a559921f9a6
nota-se que os dados foram alterados corretamente.
