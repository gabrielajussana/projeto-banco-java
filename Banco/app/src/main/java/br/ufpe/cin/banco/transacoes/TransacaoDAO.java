package br.ufpe.cin.banco.transacoes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TransacaoDAO {

    @Insert
    void inserir(Transacao t);

    @Query("SELECT * FROM transacoes ORDER BY dataTransacao DESC")
    LiveData<List<Transacao>> transacoes();

    @Query("SELECT * FROM transacoes WHERE dataTransacao = :dataTransacao ORDER BY dataTransacao DESC")
    List<Transacao> buscarPelaDataTransacao(String dataTransacao);

    @Query("SELECT * FROM transacoes WHERE dataTransacao = :dataTransacao AND tipoTransacao =:tipoTransacao ORDER BY dataTransacao DESC")
    List<Transacao> buscarPelaData(String dataTransacao, char tipoTransacao);

    @Query("SELECT * FROM transacoes WHERE numeroConta =:numeroConta  ORDER BY dataTransacao DESC")
    List<Transacao> buscarPeloNumConta(String numeroConta);

    @Query("SELECT * FROM transacoes WHERE numeroConta =:numeroConta AND tipoTransacao =:tipoTransacao  ORDER BY dataTransacao DESC")
    List<Transacao> buscarPeloNumeroConta(String numeroConta, char tipoTransacao);

    @Query("SELECT * FROM transacoes WHERE tipoTransacao =:tipoTransacao ORDER BY dataTransacao DESC")
    List<Transacao> buscarPeloTipo(char tipoTransacao);

}