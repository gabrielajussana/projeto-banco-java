package br.ufpe.cin.banco.transacoes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.banco.BancoDB;
import br.ufpe.cin.banco.conta.Conta;

public class TransacaoViewModel extends AndroidViewModel {

    private TransacaoRepository repository;
    public LiveData<List<Transacao>> transacoes;

    private final MutableLiveData<Transacao> _transacaoAtual = new MutableLiveData<>();
    public LiveData<Transacao> transacaoAtual = _transacaoAtual;

    public TransacaoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
        this.transacoes = repository.getTransacoes();
    }

    public void inserir(Transacao t) {
        new Thread(() -> repository.inserir(t)).start();
    }

    public void buscarTrasacaoPeloNumero(String numeroConta){

    }

}
