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