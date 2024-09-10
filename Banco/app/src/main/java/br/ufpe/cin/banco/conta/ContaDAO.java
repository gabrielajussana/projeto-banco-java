package br.ufpe.cin.banco.conta;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void adicionar(Conta c);

    @Update
    void atualizar(Conta c);// Atualizar conta existente no banco de dados

    @Delete
    void remover(Conta c);// Remove uma conta do banco de dados

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    LiveData<List<Conta>> contas();

    @Query("SELECT * FROM contas WHERE numero = :numeroConta LIMIT 1") //método para buscar pelo número da conta
    Conta buscarPeloNumero(String numeroConta);

    @Query("SELECT * FROM contas WHERE nomeCliente LIKE '%' || :nomeCliente || '%'")//método para buscar  pelo nome do cliente
    LiveData<List<Conta>> buscarContasPeloNome(String nomeCliente);

    @Query("SELECT * FROM contas WHERE cpfCliente LIKE '%' || :cpfCliente || '%'") // método para buscar  pelo CPF do Cliente
    LiveData<List<Conta>> buscarContasPeloCPF(String cpfCliente);

    @Query("SELECT * FROM contas WHERE numero LIKE '%' || :numeroConta || '%'")
    LiveData<List<Conta>> buscarContasPeloNumero(String numeroConta);

    @Query("SELECT * FROM contas WHERE numero = :numeroConta")
    Conta buscarContaPorNumero(String numeroConta);

    @Query("SELECT SUM(CASE WHEN saldo > 0 THEN saldo ELSE 0 END) FROM contas")
    LiveData<Double> getSaldoTotal();
}
