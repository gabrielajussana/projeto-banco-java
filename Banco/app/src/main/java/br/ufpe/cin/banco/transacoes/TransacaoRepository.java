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

    public LiveData<List<Transacao>> buscarTransacaoPeloNumero(String numeroConta) {
        return dao.buscarTransacaoPeloNumero(numeroConta);
    }

    public LiveData<List<Transacao>> buscarTransacaoPelaData(String dataTransacao) {
        return dao.buscarTransacaoPelaData(dataTransacao);
    }

    public LiveData<List<Transacao>> filtrarPorTipo(char tipoTransacao) {
        return dao.filtrarPorTipo(tipoTransacao);
    }

}
