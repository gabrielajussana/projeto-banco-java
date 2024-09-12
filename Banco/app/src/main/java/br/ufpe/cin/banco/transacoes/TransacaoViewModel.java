package br.ufpe.cin.banco.transacoes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.banco.BancoDB;

public class TransacaoViewModel extends AndroidViewModel {

    private final TransacaoRepository repository;
    private final LiveData<List<Transacao>> transacoes;

    // MutableLiveData para a transação atual (opcional, dependendo de como você deseja usar isso)
    private final MutableLiveData<Transacao> _transacaoAtual = new MutableLiveData<>();
    public LiveData<Transacao> transacaoAtual = _transacaoAtual;

    public TransacaoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
        this.transacoes = repository.getTransacoes();
    }

    // Método para obter todas as transações
    public LiveData<List<Transacao>> getTransacoes() {
        return transacoes;
    }

    // Método para inserir uma transação
    @WorkerThread
    public void inserir(Transacao t) {
        repository.inserir(t);
    }

    public LiveData<List<Transacao>> buscarTransacaoPeloNumero(String numeroConta) {
        return repository.buscarTransacaoPeloNumero(numeroConta);
    }

    public LiveData<List<Transacao>> buscarTransacaoPelaData(String dataTransacao) {
        return repository.buscarTransacaoPelaData(dataTransacao);
    }

    public LiveData<List<Transacao>> filtrarPorTipo(char tipoTransacao) {
        return repository.filtrarPorTipo(tipoTransacao);
    }
}
