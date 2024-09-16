package br.ufpe.cin.banco.transacoes;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TransacaoRepository {
    private final TransacaoDAO dao;
    private final LiveData<List<Transacao>> transacoes;

    public TransacaoRepository(TransacaoDAO dao) {
        this.dao = dao;
        this.transacoes = dao.transacoes();
    }

    public LiveData<List<Transacao>> getTransacoes() {
        return transacoes;
    }

    @WorkerThread
    public void inserir(Transacao t) {
        dao.adicionar(t);
    }

    public LiveData<List<Transacao>> buscarTodos() {
        return dao.transacoes();
    }

    public LiveData<List<Transacao>> buscarTodasTransacoesPeloNumero(String numeroConta){
        return dao. buscarTransacaoPeloNumero(numeroConta);
    }

    public LiveData<List<Transacao>> buscarDataTodos(String dataTransacao){
        return dao.buscarTodasTransacoesPelaData(dataTransacao);
    }

    public LiveData<List<Transacao>> buscarDataCredito(String dataTransacao, char credito){
        return dao.buscarPorDataCredito(dataTransacao, credito);
    }

    public LiveData<List<Transacao>> buscarDataDebito(String dataTransacao, char debito){
        return dao.buscarPorDataDebito(dataTransacao, debito);
    }

    public LiveData<List<Transacao>> buscarNumeroCredito(String numeroConta, char credito){
        return dao.buscarPorNumeroCredito(numeroConta, credito);
    }

    public LiveData<List<Transacao>> buscarNumeroDebito(String numeroConta, char debito){
        return dao.buscarPorNumeroDebito(numeroConta, debito);
    }


}
