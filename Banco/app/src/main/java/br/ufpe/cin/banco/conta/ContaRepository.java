package br.ufpe.cin.banco.conta;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContaRepository {
    private ContaDAO dao;
    private LiveData<List<Conta>> contas;

    public ContaRepository(ContaDAO dao) {
        this.dao = dao;
        this.contas = dao.contas();
    }

    public LiveData<List<Conta>> getContas() {
        return contas;
    }

    @WorkerThread
    public void inserir(Conta c) {
        dao.adicionar(c);
    }

    @WorkerThread
    public void atualizar(Conta c) {
        dao.atualizar(c);
    }

    @WorkerThread
    public void remover(Conta c) {
        dao.remover(c);
    }
    public  LiveData<List<Conta>> buscarPeloNome(String nomeCliente) {
        return dao.buscarContasPeloNome(nomeCliente);
    }
    public LiveData<List<Conta>> buscarContasPeloCPF(String cpfCliente) {
        return dao.buscarContasPeloCPF(cpfCliente);
    }
    public LiveData<List<Conta>> buscarContasPeloNumero(String numeroConta) {
        return dao.buscarContasPeloNumero(numeroConta);
    }

    @WorkerThread
    public Conta buscarContaPorNumero(String numeroConta) {
        return dao.buscarPeloNumero(numeroConta);
    }
    public LiveData<Double> getTotalSaldo() {
        return dao.getSaldoTotal();
    }
}
