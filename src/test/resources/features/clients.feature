# language: pt
Funcionalidade: Gestão de clientes
  Como consumidor da API
  Quero listar os clientes
  Para que eu possa visualizar os clientes existentes no sistema

  @clientes_existentes
  Cenário: Listar clientes com sucesso
    Dado que existem clientes cadastrados no sistema
    Quando eu enviar uma requisição GET para "/v1/clients"
    Então o status da resposta deve ser 200
    E o tipo de conteúdo da resposta deve ser "application/json"
    E a resposta deve conter uma lista de clientes
    E cada cliente deve conter os campos "id", "nome", "email" e "cpf"

  Cenário: Listar clientes quando não há nenhum cadastrado
    Dado que não existem clientes cadastrados no sistema
    Quando eu enviar uma requisição GET para "/v1/clients"
    Então o status da resposta deve ser 200
    E o tipo de conteúdo da resposta deve ser "application/json"
    E a resposta deve conter uma lista vazia


