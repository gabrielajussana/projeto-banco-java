package br.ufpe.cin.banco;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.ufpe.cin.banco.conta.ContaAdapter;

//Ver anotações TODO no código
public class PesquisarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    ContaAdapter adapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        adapter = new ContaAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        btnPesquisar.setOnClickListener(
                v -> {
                    String oQueFoiDigitado = aPesquisar.getText().toString();

                    int tipoSelecionado = tipoPesquisa.getCheckedRadioButtonId();

                    if (oQueFoiDigitado.isEmpty()) {
                        // Tratar caso o campo de pesquisa esteja vazio
                        return;
                    }

                    // Tratar caso nenhum tipo de pesquisa válido esteja selecionado
                    if (tipoSelecionado == R.id.peloCPFcliente) {
                        viewModel.buscarContasPeloCPF(oQueFoiDigitado);
                    }

                    else if (tipoSelecionado == R.id.peloNomeCliente) {
                        viewModel.buscarContasPeloNome(oQueFoiDigitado);
                    }

                    else if (tipoSelecionado == R.id.peloNumeroConta) {
                        viewModel.buscarContaPeloNumero(oQueFoiDigitado);
                    }

                });

        viewModel.getContasFiltradas().observe(this, contas -> {
            adapter.submitList(contas);
        });

    }
}