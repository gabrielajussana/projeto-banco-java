package br.ufpe.cin.banco.transacoes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.ufpe.cin.banco.BancoViewModel;
import br.ufpe.cin.banco.R;

//Ver anotações TODO no código
public class TransacoesActivity extends AppCompatActivity {
    BancoViewModel bancoViewModel;
    TransacaoViewModel transacaoViewModel;
    TransacaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes);
        bancoViewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        transacaoViewModel = new ViewModelProvider(this).get(TransacaoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoTransacao = findViewById(R.id.tipoTransacao);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        adapter = new TransacaoAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        btnPesquisar.setOnClickListener(
                v -> {
                    String oQueFoiDigitado = aPesquisar.getText().toString();
                    //TODO implementar o filtro de transações com o tipo de busca escolhido pelo usuário
                }
        );

        //TODO atualizar o RecyclerView com resultados da busca na medida que encontrar
        // inicialmente deve exibir a lista de todas as transações

    }
}