package br.ufpe.cin.banco.transacoes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufpe.cin.banco.R;

public class TransacoesActivity extends AppCompatActivity {
    private TransacaoViewModel transacaoViewModel;
    private TransacaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes);

        // Inicialize os componentes
        transacaoViewModel = new ViewModelProvider(this).get(TransacaoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoTransacao = findViewById(R.id.tipoTransacao);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);

        // Configuração do RecyclerView
        adapter = new TransacaoAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        // Inicialize a lista com todas as transações
        transacaoViewModel.getTransacoes().observe(this, new Observer<List<Transacao>>() {
            @Override
            public void onChanged(List<Transacao> transacoes) {
                adapter.submitList(transacoes);
            }
        });

        // Configuração do botão de pesquisa
        btnPesquisar.setOnClickListener(v -> {
            String oQueFoiDigitado = aPesquisar.getText().toString();
            int tipoPesquisaSelecionado = tipoPesquisa.getCheckedRadioButtonId();

            if (tipoPesquisaSelecionado == R.id.numeroContaTransacao) {
                transacaoViewModel.buscarTransacaoPeloNumero(oQueFoiDigitado).observe(this, new Observer<List<Transacao>>() {
                    @Override
                    public void onChanged(List<Transacao> transacoes) {
                        adapter.submitList(transacoes);
                    }
                });
            } else if (tipoPesquisaSelecionado == R.id.dataTransacao) {
                transacaoViewModel.buscarTransacaoPelaData(oQueFoiDigitado).observe(this, new Observer<List<Transacao>>() {
                    @Override
                    public void onChanged(List<Transacao> transacoes) {
                        adapter.submitList(transacoes);
                    }
                });
            } else if (tipoPesquisaSelecionado == R.id.tipoTransacao) {
                int tipoSelecionado = tipoTransacao.getCheckedRadioButtonId();
                char tipo = ' '; // Inicialize com um valor padrão, se necessário.

                // Defina o tipo com base no ID do botão selecionado
                if (tipoSelecionado == R.id.peloTipoCredito) {
                    tipo = 'C'; // Defina o tipo de transação para crédito
                } else if (tipoSelecionado == R.id.peloTipoDebito) {
                    tipo = 'D'; // Defina o tipo de transação para débito
                }

                transacaoViewModel.filtrarPorTipo(tipo).observe(this, new Observer<List<Transacao>>() {
                    @Override
                    public void onChanged(List<Transacao> transacoes) {
                        adapter.submitList(transacoes);
                    }
                });
            }
        });
    }
}
