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
    public final LiveData<List<Transacao>> transacoes;

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
        new Thread(() -> {
            repository.inserir(t);
        }).start();
    }

    public LiveData<List<Transacao>> buscarNumeroTodos(String numero) {
        return repository.buscarTodasTransacoesPeloNumero(numero);
    }

    public LiveData<List<Transacao>> buscarDataTodos(String data) {
        return repository.buscarDataTodos(data);
    }

    public LiveData<List<Transacao>> buscarDataCredito(String data, char credito) {
        return repository.buscarDataCredito(data, credito);
    }

    public LiveData<List<Transacao>> buscarDataDebito(String data, char debito) {
        return repository.buscarDataDebito(data, debito);
    }

    public LiveData<List<Transacao>> buscarNumeroCredito(String numero, char credito) {
        return repository.buscarNumeroCredito(numero, credito);
    }

    public LiveData<List<Transacao>> buscarNumeroDebito(String numero, char debito) {
        return repository.buscarNumeroDebito(numero, debito);
    }


}
