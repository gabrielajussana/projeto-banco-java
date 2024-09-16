package br.ufpe.cin.banco;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaRepository;
import br.ufpe.cin.banco.transacoes.Transacao;
import br.ufpe.cin.banco.transacoes.TransacaoRepository;
import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

public class BancoViewModel extends AndroidViewModel {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private MutableLiveData<List<Conta>> contasFiltradas;
    private Date dataTransacao = new Date();
    private TransacaoRepository repository;
    private final MutableLiveData<LiveData<List<Transacao>>> _transacaoAtual = new MutableLiveData<LiveData<List<Transacao>>>();
    public LiveData<LiveData<List<Transacao>>> transacaoAtual = _transacaoAtual;
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
            Conta contaOrigem = contaRepository.buscarContaPorNumero(numeroContaOrigem);
            Conta contaDestino = contaRepository.buscarContaPorNumero(numeroContaDestino);

            // Verificar se as contas existem
            if (contaOrigem != null && contaDestino != null) {
                // Verificar se a conta de origem tem saldo suficiente
                if (contaOrigem.getSaldo() >= valor) {
                    // Atualizar o saldo das contas
                    contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
                    contaDestino.setSaldo(contaDestino.getSaldo() + valor);

                    // Salvar as contas atualizadas
                    contaRepository.atualizar(contaOrigem);
                    contaRepository.atualizar(contaDestino);

                    // Debitar da conta de Origem
                    Transacao transacaoDebito = new Transacao(0, 'C', numeroContaOrigem, valor, dataTransacao.toString());


                    // Creditar na conta de destino
                    Transacao transacaoCredito = new Transacao(0,'D', numeroContaDestino, valor, dataTransacao.toString() );


                    // Salvar as transações
                    TransacaoViewModel transacaoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                            getApplication()).create(TransacaoViewModel.class);
                    transacaoViewModel.inserir(transacaoDebito);
                    transacaoViewModel.inserir(transacaoCredito);
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
        new Thread(() -> {
            // Buscar a conta pelo número
            Conta conta = contaRepository.buscarContaPorNumero(numeroConta);
            // Atualizar saldo
            if(conta != null) {
                double novoSaldo = conta.getSaldo() + valor;
                conta.setSaldo(novoSaldo);
                contaRepository.atualizar(conta);
            }
            Transacao transacao = new Transacao(0, 'C', numeroConta, valor, dataTransacao.toString());

            // Criar um novo TransacaoViewModel se necessário
            TransacaoViewModel transacaoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                    getApplication()).create(TransacaoViewModel.class);
            transacaoViewModel.inserir(transacao);
        }).start();
    }

    void debitar(String numeroConta, double valor) {
        new Thread(() -> {
            // Buscar a conta pelo número
            Conta conta = contaRepository.buscarContaPorNumero(numeroConta);
            // Atualizar o saldo da conta
            if(conta != null) {
                double novoSaldo = conta.getSaldo() - valor;
                conta.setSaldo(novoSaldo);
                contaRepository.atualizar(conta);
            }
            Transacao transacao = new Transacao(0, 'D', numeroConta, valor, dataTransacao.toString());
            // Criar um novo TransacaoViewModel se necessário
            TransacaoViewModel transacaoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                    getApplication()).create(TransacaoViewModel.class);
            transacaoViewModel.inserir(transacao);
        }).start();
    }

    // Busca pelo nome do Cliente
    public void buscarContasPeloNome(String nomeCliente) {
        List<Conta> conta = (List<Conta>) this.contaRepository.buscarPeloNome(nomeCliente);
        contasFiltradas.postValue(conta);
    }

    // Busca pelo CPF do Cliente
    public void buscarContasPeloCPF(String cpfCliente) {
        List<Conta> conta = (List<Conta>) this.contaRepository.buscarContasPeloCPF(cpfCliente);
        contasFiltradas.postValue(conta);

    }

    // Busca pelo número da Conta
    public void buscarContaPeloNumero(String numeroConta) { //criando uma lista para mostrar na busca por número no PesquisarActivity
        Conta conta = this.contaRepository.buscarContaPorNumero(numeroConta);
        List<Conta> lista = new ArrayList<>();
        if (conta != null) {
            lista.add(conta);
        }
        contasFiltradas.postValue(lista);
    }

}
