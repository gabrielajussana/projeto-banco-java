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

    // Atualizar conta existente no banco de dados
    @Update
    void atualizar(Conta c);

    // Remover uma conta do banco de dados
    @Delete
    void remover(Conta c);

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    LiveData<List<Conta>> contas();

    // Método para buscar pelo número da conta
    @Query("SELECT * FROM contas WHERE numero = :numeroConta LIMIT 1")
    Conta buscarPeloNumero(String numeroConta);

    // Método para buscar  pelo nome do cliente
    @Query("SELECT * FROM contas WHERE nomeCliente LIKE '%' || :nomeCliente || '%'")
    LiveData<List<Conta>> buscarContasPeloNome(String nomeCliente);

    // Método para buscar  pelo CPF do Cliente
    @Query("SELECT * FROM contas WHERE cpfCliente LIKE '%' || :cpfCliente || '%'")
    LiveData<List<Conta>> buscarContasPeloCPF(String cpfCliente);

    // método para buscar  contas pelo número
    @Query("SELECT * FROM contas WHERE numero LIKE '%' || :numeroConta || '%'")
    LiveData<List<Conta>> buscarContasPeloNumero(String numeroConta);

    // método para buscar  uma única conta pelo número
    @Query("SELECT * FROM contas WHERE numero = :numeroConta")
    Conta buscarContaPorNumero(String numeroConta);

    // método para buscar o saldo total
    @Query("SELECT SUM(CASE WHEN saldo > 0 THEN saldo ELSE 0 END) FROM contas")
    LiveData<Double> getSaldoTotal();
}
