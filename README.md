## Projeto Banco - Java Android
Este é um projeto que visa implementar uma aplicação de gerenciamento de contas bancárias. 


### Estrutura do Projeto
#### 1. ContasActivity
- Foi adicionada a recuperação de dados do banco de dados para a lista de contas usando o atributo contas (do tipo LiveData<List<Conta>>) de ContaViewModel.
#### 2. ContaViewHolder
- Foi adicionada condições na função bindTo para alterar a imagem exibida quando o saldo da conta estiver negativo.
- O listener dos botões de editar e remover conta foi ajustado, para que as funcionalidades correspondentes sejam implementadas.
- Foi incluido o número da conta no Intent para garantir que a tela EditarContaActivity receba as informações necessárias para recuperar os dados da conta.
#### 3. AdicionarContaActivity
- Foi adicionada a validação das informações inseridas pelo usuário (ex.: campos não podem estar em branco, saldo deve ser um número) antes de criar um objeto Conta no banco de dados.
- Foi implementado o código para usar ContaViewModel e armazenar o objeto no banco de dados.
#### 4. ContaDAO
- Foram adicionados métodos para atualizar e remover contas.
- Foram adicionados métodos para buscar contas pelo número, nome do cliente e CPF do cliente.
#### 5. ContaRepository
- Adicionados métodos de Atualização e Remoção:
- Implementado a lógica para os métodos de atualização e remoção de contas, utilizando os métodos da classe ContaDAO.
- Implementado os métodos para buscar contas pelo número, nome do cliente e CPF do cliente.
#### 6. ContaViewModel
- Adicionado métodos para atualizar e remover contas.
- Incluido um método para buscar contas pelo número, utilizando os métodos da classe ContaRepository.
#### 7. EditarContaActivity
- Implementado a funcionalidade para recuperar as informações da conta com base no número passado pelo Intent e atualizar os campos do formulário.
- Adicionado a validação das informações inseridas antes de atualizar a conta no banco de dados e implemente o uso de ContaViewModel para armazenar o objeto atualizado.
- Adicionado o código para usar ContaViewModel e remover o objeto do banco de dados.
#### 8. BancoViewModel
- Incluido métodos para transferir, creditar e debitar.
- Adicionado métodos para buscar contas pelo número, nome do cliente e CPF do cliente, utilizando métodos da classe ContaRepository.
#### 9. DebitarActivity, CreditarActivity, TransferirActivity
- Implementado a validação dos campos.
#### 10. PesquisarActivity
- Implementado o código para buscar no banco de dados conforme o tipo de busca escolhido pelo usuário (ver RadioGroup tipoPesquisa).
- Atualizado o RecyclerView com os resultados da busca conforme eles forem encontrados.
#### 11. MainActivity
- Exibindo o valor total de dinheiro armazenado no banco na tela principal.
#### 12. TransacaoViewHolder
- Exibição do Valor da Transação:
- Exibindo o valor da transação em vermelho para transações de débito e azul para transações de crédito.
#### 13. TransacaoDAO
- Incluido métodos para buscar transações pelo número da conta, pela data e filtrar por tipo de transação (crédito, débito, ou todas).
#### 14. TransacaoRepository
- Implementado a lógica para os métodos de busca de transações, utilizando os métodos da classe TransacaoDAO.
#### 15. TransacaoViewModel
- Incluido métodos para realizar buscas de transações usando a lógica da classe TransacaoRepository.
#### 16. TransacoesActivity
- Implementado o código para buscar transações no banco de dados de acordo com o tipo de busca escolhido pelo usuário e atualize o RecyclerView para mostrar todas as transações inicialmente.
