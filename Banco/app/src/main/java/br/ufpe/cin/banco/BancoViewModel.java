package br.ufpe.cin.banco;

import android.app.Application;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaRepository;
import br.ufpe.cin.banco.transacoes.Transacao;
import br.ufpe.cin.banco.transacoes.TransacaoRepository;
import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

public class BancoViewModel extends AndroidViewModel {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private final MutableLiveData<List<Conta>> contasPeloNome = new MutableLiveData<>();
    private final MutableLiveData<List<Conta>> contasPeloCPF = new MutableLiveData<>();
    public final MutableLiveData<Conta> contaPeloNumero = new MutableLiveData<>();
    private final MutableLiveData<List<Transacao>> _listaTransacoes = new MutableLiveData<>();
    private final MutableLiveData<LiveData<List<Transacao>>> _transacaoAtual = new MutableLiveData<LiveData<List<Transacao>>>();
    private Date dataTransacao = new Date();
    private TransacaoRepository repository;
    public LiveData<LiveData<List<Transacao>>> transacaoAtual = _transacaoAtual;
    public LiveData<List<Transacao>> listaTransacoes = _listaTransacoes;
    public BancoViewModel(@NonNull Application application) {
        super(application);
        this.contaRepository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.transacaoRepository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
    }

    public LiveData<Double> getTotalSaldo() {
        return contaRepository.getTotalSaldo();
    }

    SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
    String dataFormatada = formatadorData.format(dataTransacao);

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
                    Transacao transacaoDebito = new Transacao(0, 'C', numeroContaOrigem, valor, dataFormatada);

                    // Creditar na conta de destino
                    Transacao transacaoCredito = new Transacao(0,'D', numeroContaDestino, valor, dataFormatada );

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
            Transacao transacao = new Transacao(0, 'C', numeroConta, valor, dataFormatada);

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
            Transacao transacao = new Transacao(0, 'D', numeroConta, valor, dataFormatada);
            // Criar um novo TransacaoViewModel se necessário
            TransacaoViewModel transacaoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                    getApplication()).create(TransacaoViewModel.class);
            transacaoViewModel.inserir(transacao);
        }).start();
    }

    public LiveData<Double> getSaldoTotal() {
        return contaRepository.getTotalSaldo();
    }

    public LiveData<List<Conta>> buscarContasPeloNome(String nomeCliente) {
        new Thread(() -> {
            List<Conta> contas = (List<Conta>) this.contaRepository.buscarPeloNome(nomeCliente);
            contasPeloNome.postValue(contas);
        }).start();
        return contasPeloNome;
    }

    public LiveData<List<Conta>> buscarContasPeloCPF(String cpfCliente) {
        new Thread(() -> {
            List<Conta> contas = (List<Conta>) this.contaRepository.buscarContasPeloCPF(cpfCliente);
            contasPeloCPF.postValue(contas);
        }).start();
        return contasPeloCPF;
    }

    public LiveData<Conta> buscarContaPeloNumero(String numeroConta) {
        new Thread(() -> {
            Conta conta = this.contaRepository.buscarContaPorNumero(numeroConta);
            contaPeloNumero.postValue(conta);
        }).start();
        return contaPeloNumero;
    }


    public void buscarTransacoesPeloNumero(String numeroConta, char tipoTransacao) {
        new Thread(
                () -> {
                    List<Transacao> transacao = this.transacaoRepository.buscarPeloNumeroConta(numeroConta, tipoTransacao);
                    _listaTransacoes.postValue(transacao);
                }
        ).start();

    }
    public void buscarTransacoesPeloNum(String numeroConta) {
        new Thread(
                () -> {
                    List<Transacao> transacao = this.transacaoRepository.buscarPeloNumConta(numeroConta);
                    _listaTransacoes.postValue(transacao);
                }
        ).start();
    }


    public void buscarTransacoesPelaDataTipo(String dataTransacao, char tipoTransacao) {
        new Thread(
                () -> {
                    List<Transacao> transacao = this.transacaoRepository.buscarPelaDataTipo(dataTransacao, tipoTransacao);
                    _listaTransacoes.postValue(transacao);
                }
        ).start();
    }
    public void buscarTransacoesPelaData(String dataTransacao) {
        new Thread(
                () -> {
                    List<Transacao> transacao = this.transacaoRepository.buscarPelaDataTransacao(dataTransacao);
                    _listaTransacoes.postValue(transacao);
                }
        ).start();
    }


}
