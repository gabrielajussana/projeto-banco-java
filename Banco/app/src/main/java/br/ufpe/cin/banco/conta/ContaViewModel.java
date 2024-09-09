package br.ufpe.cin.banco.conta;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.banco.BancoDB;

public class ContaViewModel extends AndroidViewModel {

    private ContaRepository repository;
    public LiveData<List<Conta>> contas;
    private MutableLiveData<Conta> _contaAtual = new MutableLiveData<>();
    public LiveData<Conta> contaAtual = _contaAtual;

    public ContaViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.contas = repository.getContas();
    }

    public LiveData<List<Conta>> buscarContas() {
        return contas;
    }

    public void inserir(Conta c) {
        new Thread(() -> repository.inserir(c)).start();
    }

    //Thread para chamar o método atualizar do ContaRepository
    public void atualizar(Conta c) {
        new Thread(() -> repository.atualizar(c)).start();
    }

    //Thread para chamar o método remover do ContaRepository
    public void remover(Conta c) {
        new Thread(() -> repository.remover(c)).start();
    }

    //Thread para buscar a conta pelo número
    void buscarPeloNumero(String numero) {
        new Thread(() -> repository.buscarPeloNumero(numero)).start();
    }
}
