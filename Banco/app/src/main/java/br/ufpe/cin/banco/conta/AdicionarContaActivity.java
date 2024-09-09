package br.ufpe.cin.banco.conta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import br.ufpe.cin.banco.R;

public class AdicionarContaActivity extends AppCompatActivity {

    private ContaViewModel viewModel;

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

        btnAtualizar.setText("Inserir");
        btnRemover.setVisibility(View.GONE);

        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString().trim();
                    String cpfCliente = campoCPF.getText().toString().trim();
                    String numeroConta = campoNumero.getText().toString().trim();
                    String saldoContaStr = campoSaldo.getText().toString().trim();

                    if (validateInputs(nomeCliente, cpfCliente, numeroConta, saldoContaStr)) {
                        try {
                            double saldoConta = Double.parseDouble(saldoContaStr);
                            Conta c = new Conta(numeroConta, saldoConta, nomeCliente, cpfCliente);
                            viewModel.inserir(c);
                            Toast.makeText(this, "Conta adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Saldo inválido.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private boolean validateInputs(String nome, String cpf, String numero, String saldo) {
        if (nome.isEmpty() || nome.length() < 5) {
            Toast.makeText(this, "Nome deve ter pelo menos 5 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cpf.isEmpty() || cpf.length() != 11) {
            Toast.makeText(this, "CPF inválido. Deve ter 11 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numero.isEmpty()) {
            Toast.makeText(this, "Número da conta não pode ser vazio.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (saldo.isEmpty()) {
            Toast.makeText(this, "Saldo não pode ser vazio.", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            Double.parseDouble(saldo);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Saldo deve ser um número válido.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
