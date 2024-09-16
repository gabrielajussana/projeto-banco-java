package br.ufpe.cin.banco.transacoes;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import java.util.List;


public class TransacaoRepository {
    private TransacaoDAO dao;
    private LiveData<List<Transacao>> transacoes;

    public TransacaoRepository(TransacaoDAO dao) {
        this.dao = dao;
        this.transacoes = dao.transacoes();
    }

    public LiveData<List<Transacao>> getTransacoes() {
        return this.transacoes;
    }

    @WorkerThread
    public void inserir(Transacao t) {
        dao.inserir(t);
    }

    // Busca transação pela data
    @WorkerThread
    public List<Transacao> buscarPelaDataTransacao(String dataTransacao){
        return dao.buscarPelaDataTransacao(dataTransacao);
    }

    // Busca transação pelo número da conta
    @WorkerThread
    public List<Transacao> buscarPeloNumConta(String numeroConta){
        return dao.buscarPeloNumConta(numeroConta);
    }

    // Busca transação pelo tipo
    @WorkerThread
    public List<Transacao> buscarPeloTipo(char tipoTransacao){
        return dao.buscarPeloTipo(tipoTransacao);
    }

    // Busca transação por data  e tipo
    @WorkerThread
    public List<Transacao> buscarPelaDataTipo(String dataTransacao, char tipoTransacao){
        return dao.buscarPelaData(dataTransacao, tipoTransacao);
    }

    // Busca transação por número da conta e tipo
    @WorkerThread
    public List<Transacao> buscarPeloNumeroConta(String numeroConta, char tipoTransaco){
        return dao.buscarPeloNumeroConta(numeroConta, tipoTransaco);
    }

}