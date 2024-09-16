package br.ufpe.cin.banco.transacoes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransacaoDAO {

    @Insert
    void adicionar(Transacao t);

    //não deve ser possível editar ou remover uma transação

    // Listar todas transações
    @Query("SELECT * FROM transacoes ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> transacoes();

    // Buscar Transações pelo número da conta
    @Query("SELECT * FROM transacoes WHERE numeroConta = :numeroConta ORDER BY dataTransacao")
    LiveData<List<Transacao>> buscarTransacaoPeloNumero(String numeroConta);

    // Buscar todas Transações pela data
    @Query("SELECT * FROM transacoes WHERE dataTransacao = :dataTransacao ORDER BY dataTransacao ")
    LiveData<List<Transacao>> buscarTodasTransacoesPelaData(String dataTransacao);

    //Buscar por data e tipo crédito
    @Query("SELECT * FROM transacoes WHERE dataTransacao = :data AND tipoTransacao = :credito ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> buscarPorDataCredito(String data, char credito);

    // Buscar por data e tipo débito
    @Query("SELECT * FROM transacoes WHERE dataTransacao = :data AND tipoTransacao = :debito ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> buscarPorDataDebito(String data, char debito);

    // Buscar por número da conta do tipo crédito
    @Query("SELECT * FROM transacoes WHERE numeroConta = :numero AND tipoTransacao = :credito ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> buscarPorNumeroCredito(String numero, char credito);

    // Buscar por número da conta e tipo débito
    @Query("SELECT * FROM transacoes WHERE numeroConta = :numero AND tipoTransacao = :debito ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> buscarPorNumeroDebito(String numero, char debito);

}
