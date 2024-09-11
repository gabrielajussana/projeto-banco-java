package br.ufpe.cin.banco;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaRepository;
import br.ufpe.cin.banco.transacoes.TransacaoViewModel;

public class CreditarActivity extends AppCompatActivity {
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
        labelContaDestino.setVisibility(View.GONE);
        numeroContaDestino.setVisibility(View.GONE);
        valorOperacao.setHint(valorOperacao.getHint() + " creditado");
        tipoOperacao.setText("CREDITAR");
        btnOperacao.setText("Creditar");


        btnOperacao.setOnClickListener(
                v -> {
                    String numOrigem = numeroContaOrigem.getText().toString();
                    double valor = Double.valueOf(valorOperacao.getText().toString());
                    if(numOrigem != null){
                        viewModel.creditar(numOrigem,valor);
                    }
                    finish();
                }
        );
    }
}