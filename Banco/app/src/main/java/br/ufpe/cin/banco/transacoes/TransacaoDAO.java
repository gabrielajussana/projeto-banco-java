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

    @Query("SELECT * FROM transacoes ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> transacoes();

    // Buscar Transações pelo número da conta
    @Query("SELECT * FROM transacoes WHERE numeroConta = :numeroConta")
    LiveData<List<Transacao>> buscarTransacaoPeloNumero(String numeroConta);

    // Buscar Transações pela data
    @Query("SELECT * FROM transacoes WHERE dataTransacao LIKE '%' || :dataTransacao || '%'")
    LiveData<List<Transacao>> buscarTransacaoPelaData(String dataTransacao);

    // Filtrar pelo tipo da transação (crédito, débito, ou todas)
    @Query("SELECT * FROM transacoes WHERE tipoTransacao = :tipoTransacao")
    LiveData<List<Transacao>> filtrarPorTipo(char tipoTransacao);

}
