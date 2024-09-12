package br.ufpe.cin.banco;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.List;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaRepository;
import br.ufpe.cin.banco.transacoes.Transacao;
import br.ufpe.cin.banco.transacoes.TransacaoRepository;
import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BancoViewModel extends AndroidViewModel {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private MutableLiveData<List<Conta>> contasFiltradas;
    private Date dataTransacao = new Date();

    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    String dataFormatada = formato.format(dataTransacao);
    private TransacaoRepository repository;
    private final MutableLiveData<Transacao> _transacaoAtual = new MutableLiveData<>();
    public LiveData<Transacao> transacaoAtual = _transacaoAtual;
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

    void buscarContasPeloNome(String nomeCliente) {
        contaRepository.buscarPeloNome(nomeCliente).observeForever(contas -> {
            contasFiltradas.setValue(contas);
        });
    }

    void buscarContasPeloCPF(String cpfCliente) {
        contaRepository.buscarContasPeloCPF(cpfCliente).observeForever(contas -> {
            contasFiltradas.setValue(contas);
        });
    }

    void buscarContaPeloNumero(String numeroConta) {
        contaRepository.buscarContasPeloNumero(numeroConta).observeForever(contas -> {
            contasFiltradas.setValue(contas);
        });
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
            Transacao transacao = new Transacao(0, 'D', numeroConta, valor, dataFormatada);
            // Criar um novo TransacaoViewModel se necessário
            TransacaoViewModel transacaoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                    getApplication()).create(TransacaoViewModel.class);
            transacaoViewModel.inserir(transacao);
        }).start();
    }

    void buscarTransacoesPeloNumero(String numeroConta) {
        new Thread(
                () -> {
                    LiveData<List<Transacao>> transacao = repository.buscarTransacaoPeloNumero(numeroConta);
                }
        ).start();
    }

    void buscarTransacoesPeloTipo(char tipoTransacao) {
        new Thread(
                () -> {
                    LiveData<List<Transacao>> transacao = repository.filtrarPorTipo(tipoTransacao);
                }
        ).start();
    }

    void buscarTransacoesPelaData(String dataTransacao) {
        new Thread(
                () -> {
                    LiveData<List<Transacao>> transacao = repository.buscarTransacaoPelaData(dataTransacao);
                }
        ).start();
    }

}
