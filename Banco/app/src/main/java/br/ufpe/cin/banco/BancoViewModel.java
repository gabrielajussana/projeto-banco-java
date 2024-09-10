package br.ufpe.cin.banco;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaRepository;
import br.ufpe.cin.banco.transacoes.TransacaoRepository;
import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

public class BancoViewModel extends AndroidViewModel {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;

    public BancoViewModel(@NonNull Application application) {
        super(application);
        this.contaRepository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.transacaoRepository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
    }

    public LiveData<Double> getTotalSaldo() {
        return contaRepository.getTotalSaldo();
    }

    public LiveData<List<Conta>> getContasFiltradas() {
        return contasFiltradas;
    }

    void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
        new Thread(() -> {
            // Buscar as contas de origem e destino
            Conta contaOrigem = contaRepository.buscarContaPorNumero(numeroContaOrigem);
            Conta contaDestino = contaRepository.buscarContaPorNumero(numeroContaDestino);

            // Verificar se ambas as contas existem
            if (contaOrigem != null && contaDestino != null) {
                // Verificar se a conta de origem tem saldo suficiente
                if (contaOrigem.getSaldo() >= valor) {
                    // Atualizar o saldo das contas
                    contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
                    contaDestino.setSaldo(contaDestino.getSaldo() + valor);

                    // Salvar as contas atualizadas no banco de dados
                    contaRepository.atualizar(contaOrigem);
                    contaRepository.atualizar(contaDestino);

                    // Criar transação para a conta de origem (débito)
                    Transacao transacaoDebito = new Transacao();
                    transacaoDebito.setNumeroConta(numeroContaOrigem);
                    transacaoDebito.setValor(valor);
                    transacaoDebito.setTipo("Débito - Transferência");

                    // Criar transação para a conta de destino (crédito)
                    Transacao transacaoCredito = new Transacao();
                    transacaoCredito.setNumeroConta(numeroContaDestino);
                    transacaoCredito.setValor(valor);
                    transacaoCredito.setTipo("Crédito - Transferência");

                    // Salvar as transações
                    TransacaoViewModel transacaoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                            getApplication()).create(TransacaoViewModel.class);
                    transacaoViewModel.salvarTransacao(transacaoDebito);
                    transacaoViewModel.salvarTransacao(transacaoCredito);
                } else {
                    new android.os.Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(getApplication(), "Saldo insuficiente na conta de origem.", Toast.LENGTH_SHORT).show()
                    );
                }
            } else {
                new android.os.Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(getApplication(), "Conta de origem ou destino não encontrada.", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    void creditar(String numeroConta, double valor) {
        // Executar a operação em uma thread separada
        new Thread(() -> {
            // Buscar a conta pelo número
            Conta conta = contaRepository.buscarContaPorNumero(numeroConta);

            if (conta != null) {
                // Atualizar o saldo da conta
                double novoSaldo = conta.getSaldo() + valor;
                conta.setSaldo(novoSaldo);

                // Salvar a conta atualizada no banco de dados
                contaRepository.atualizar(conta);

                // Criar e salvar a transação
                Transacao transacao = new Transacao();
                transacao.setNumeroConta(numeroConta);
                transacao.setValor(valor);
                transacao.setTipo("Crédito");

                // Criar um novo TransacaoViewModel se necessário
                TransacaoViewModel transacaoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                        getApplication()).create(TransacaoViewModel.class);
                transacaoViewModel.salvarTransacao(transacao);
            }
        }).start();
    }

    void debitar(String numeroConta, double valor) {
        // Executar a operação em uma thread separada
        new Thread(() -> {
            // Buscar a conta pelo número
            Conta conta = contaRepository.buscarContaPorNumero(numeroConta);

            if (conta != null) {
                // Atualizar o saldo da conta
                double novoSaldo = conta.getSaldo() - valor;
                conta.setSaldo(novoSaldo);

                // Salvar a conta atualizada no banco de dados
                contaRepository.atualizar(conta);

                // Criar e salvar a transação
                Transacao transacao = new Transacao();
                transacao.setNumeroConta(numeroConta);
                transacao.setValor(valor);
                transacao.setTipo("Crédito");

                // Criar um novo TransacaoViewModel se necessário
                TransacaoViewModel transacaoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                        getApplication()).create(TransacaoViewModel.class);
                transacaoViewModel.salvarTransacao(transacao);
            }
        }).start();
    }

    void buscarContasPeloNome(String nomeCliente) {
        // Observe the LiveData returned by the repository
        contaRepository.buscarPeloNome(nomeCliente).observeForever(contas -> {
            // Update the MutableLiveData with the filtered accounts
            contasFiltradas.setValue(contas); // Ensure 'contas' is of type List<Conta>
        });
    }

    void buscarContasPeloCPF(String cpfCliente) {
        // Observe the LiveData returned by the repository
        contaRepository.buscarContasPeloCPF(cpfCliente).observeForever(contas -> {
            // Update the MutableLiveData with the filtered accounts
            contasFiltradas.setValue(contas); // Ensure 'contas' is of type List<Conta>
        });
    }

    void buscarContaPeloNumero(String numeroConta) {
        // Observe the LiveData returned by the repository
        contaRepository.buscarContasPeloNumero(numeroConta).observeForever(contas -> {
            // Update the MutableLiveData with the filtered accounts
            contasFiltradas.setValue(contas); // Ensure 'contas' is of type List<Conta>
        });
    }

    void buscarTransacoesPeloNumero(String numeroConta) {
        //TODO implementar
    }

    void buscarTransacoesPeloTipo(String tipoTransacao) {
        //TODO implementar
    }

    void buscarTransacoesPelaData(String dataTransacao) {
        //TODO implementar
    }

}
