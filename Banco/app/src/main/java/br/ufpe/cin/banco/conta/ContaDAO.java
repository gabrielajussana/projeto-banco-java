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

    @Insert(entity = Conta.class, onConflict = OnConflictStrategy.REPLACE)
    void adicionar(Conta c);

    // Atualizar conta existente no banco de dados
    @Update
    void atualizar(Conta c);

    // Remove uma conta do banco de dados
    @Delete
    void remover(Conta c);

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    LiveData<List<Conta>> contas();

    @Query("SELECT * FROM contas ORDER BY numero ASC")
    List<Conta> todasContas();

    //método para buscar pelo número da conta
    @Query("SELECT * FROM contas WHERE numero = :numeroConta")
    Conta buscarPeloNumero(String numeroConta);

    //método para buscar  pelo nome
    @Query("SELECT * FROM contas WHERE nomeCliente = :nomeCliente")
    List<Conta> buscarPorNomeCliente(String nomeCliente);

    // método para buscar  pelo CPF do Cliente
    @Query("SELECT * FROM contas WHERE cpfCliente = :cpfCliente")
    List<Conta> buscarPorCpf(String cpfCliente);

    @Query("SELECT SUM(saldo) FROM contas")
    Double saldoTotal();
}
