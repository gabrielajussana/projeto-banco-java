package br.ufpe.cin.banco.conta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import br.ufpe.cin.banco.R;

public class EditarContaActivity extends AppCompatActivity {

    public static final String KEY_CPF_CONTA = "CPFDaConta";
    public static final String KEY_NOME_CONTA = "NomeDaConta";
    public static final String KEY_NUMERO_CONTA = "NumeroDaConta";
    ContaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_conta);
        viewModel = new ViewModelProvider(this).get(ContaViewModel.class);

        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Button btnRemover = findViewById(R.id.btnRemover);
        EditText campoNome = findViewById(R.id.nome);
        EditText campoNumero = findViewById(R.id.numero);
        EditText campoCPF = findViewById(R.id.cpf);
        EditText campoSaldo = findViewById(R.id.saldo);
        campoNumero.setEnabled(false);

        Intent i = getIntent();
        String numeroConta = i.getStringExtra(KEY_NUMERO_CONTA);

        // Buscar conta pelo número
        viewModel.buscarPeloNumero(numeroConta);

        // Preencher campos
        viewModel.contaAtual.observe(this, conta -> {
            if (conta != null) {
                campoNumero.setText(conta.numero);
                campoNome.setText(conta.nomeCliente);
                campoCPF.setText(conta.cpfCliente);
                campoSaldo.setText(String.valueOf(conta.saldo));
            } else {
                // Exibe toast caso a conta não seja encontrada
                Toast.makeText(this, "Conta não encontrada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnAtualizar.setText("Editar");
        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();

                    // Validação para verificar se todos os campos estão preenchidos
                    if (nomeCliente.isEmpty() || cpfCliente.isEmpty() || saldoConta.isEmpty()) {
                        Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validação tamanho do CPF
                    if (cpfCliente.length() != 11) {
                        Toast.makeText(this, "CPF deve conter 11 dígitos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double saldo;
                    try {
                        saldo = Double.parseDouble(saldoConta);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Saldo inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Conta com dados atualizados
                    Conta atualizada = new Conta(numeroConta, saldo, nomeCliente, cpfCliente);
                    viewModel.atualizar(atualizada);
                    Toast.makeText(this, "Conta atualizada com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }
        );

        btnRemover.setOnClickListener(v -> {
            // Remover conta
            viewModel.remover(new Conta(numeroConta, 0, "", ""));
            Toast.makeText(this, "Conta removida com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        });


        boolean remover = i.getBooleanExtra("remover", false);
        if (remover) {
            btnRemover.performClick();
        }
    }
}