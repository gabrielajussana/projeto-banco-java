package br.ufpe.cin.banco.transacoes;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import br.ufpe.cin.banco.BancoDB;


public class TransacaoViewModel extends AndroidViewModel {

    private TransacaoRepository repository;
    public LiveData<List<Transacao>> transacoes;
    MutableLiveData<List<Transacao>> _transacoesAtuais = new MutableLiveData<>();

    public TransacaoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TransacaoRepository(BancoDB.getDB(application).transacaoDAO());
        this.transacoes = repository.getTransacoes();
    }

    public void inserir(Transacao t) {
        new Thread(() -> repository.inserir(t)).start();
    }

    void  buscarPelaData(String dataTransacao, char tipoTransacao) {
        new Thread(
                () -> {
                    List<Transacao> t = this.repository.buscarPelaDataTipo(dataTransacao, tipoTransacao);
                    _transacoesAtuais.postValue(t);
                }
        ).start();

    }

    void  buscarPeloTipo(char tipoTransacao) {
        new Thread(
                () -> {
                    List<Transacao> t = this.repository.buscarPeloTipo(tipoTransacao);
                    _transacoesAtuais.postValue(t);
                }
        ).start();

    }

    void  buscarPeloNumero(String numeroConta, char tipoTransacao) {
        new Thread(
                () -> {
                    List<Transacao> t = this.repository.buscarPeloNumeroConta(numeroConta,tipoTransacao);
                    _transacoesAtuais.postValue(t);
                }
        ).start();

    }

    void  buscarPelaDataTransacao(String dataTransacao) {
        new Thread(
                () -> {
                    List<Transacao> t = this.repository.buscarPelaDataTransacao(dataTransacao);
                    _transacoesAtuais.postValue(t);
                }
        ).start();

    }


}