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

    // Atualizar conta
    @WorkerThread
    public void atualizar(Conta c) {
        dao.atualizar(c);
    }

    // Remover conta
    @WorkerThread
    public void remover(Conta c) {
        dao.remover(c);
    }

    // Buscar pelo número da conta
    @WorkerThread
    public Conta buscarPeloNumero(String numeroConta) {
        return dao.buscarPeloNumero(numeroConta);
    }

    // Buscar pelo nome do cliente
    @WorkerThread
    public List<Conta> buscarPeloNome(String nomeCliente) {
        return dao.buscarPorNomeCliente(nomeCliente);
    }

    // Buscar pelo CFP do cliente
    @WorkerThread
    public List<Conta> buscarPorCpf(String cpfCliente) {
        return dao.buscarPorCpf(cpfCliente);
    }


}
