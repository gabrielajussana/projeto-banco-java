package br.ufpe.cin.banco;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

public class TransferirActivity extends AppCompatActivity {

    BancoViewModel viewModel;
    TransacaoViewModel transacaoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operacoes);
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        transacaoViewModel = new ViewModelProvider(this).get(TransacaoViewModel.class);

        TextView tipoOperacao = findViewById(R.id.tipoOperacao);
        EditText numeroContaOrigem = findViewById(R.id.numeroContaOrigem);
        TextView labelContaDestino = findViewById(R.id.labelContaDestino);
        EditText numeroContaDestino = findViewById(R.id.numeroContaDestino);
        EditText valorOperacao = findViewById(R.id.valor);
        Button btnOperacao = findViewById(R.id.btnOperacao);

        valorOperacao.setHint(valorOperacao.getHint() + " transferido");
        tipoOperacao.setText("TRANSFERIR");
        btnOperacao.setText("Transferir");

        btnOperacao.setOnClickListener(
                v -> {
                    String numOrigem = numeroContaOrigem.getText().toString();
                    String numDestino = numeroContaDestino.getText().toString();
                    String valorCampo = valorOperacao.getText().toString();
                    double valor = Double.valueOf(valorOperacao.getText().toString());

                    // Verifica se o campo número da conta não está preenchido
                    if (numOrigem.isEmpty() || numOrigem == null) {
                        Toast.makeText(this, "Número da conta é obrigatório", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Verifica se o campo número da conta não está preenchido
                    if (numDestino.isEmpty() || numDestino == null) {
                        Toast.makeText(this, "Número da conta é obrigatório", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Verifica se o campo valor não está preenchido
                    else if (valorCampo.isEmpty()) {
                        Toast.makeText(this, "Digite um valor a ser creditado", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Verifica se o valor digitado é positivo
                    else if (valor <= 0) {
                        Toast.makeText(this, "Digite um valor positivo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    viewModel.transferir(numOrigem, numDestino, valor);
                    finish();
                }
        );

    }
}