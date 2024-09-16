package br.ufpe.cin.banco;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.ContaAdapter;
import br.ufpe.cin.banco.transacoes.Transacao;

public class PesquisarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    ContaAdapter adapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        EditText Pesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);

        adapter = new ContaAdapter(getLayoutInflater());
        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        btnPesquisar.setOnClickListener(
                v -> {
                    String oQueFoiDigitado = Pesquisar.getText().toString();
                    int tipoSelecionado = tipoPesquisa.getCheckedRadioButtonId();

                    if (tipoSelecionado == R.id.peloNomeCliente) {
                        viewModel.buscarContasPeloNome(oQueFoiDigitado);
                    }else if (tipoSelecionado == R.id.peloCPFcliente) {
                        viewModel.buscarContasPeloCPF(oQueFoiDigitado);
                    }else if (tipoSelecionado == R.id.peloNumeroConta) {
                        viewModel.buscarContaPeloNumero(oQueFoiDigitado);
                    }
                }
        );

        viewModel.contasFiltradas.observe(this, lista -> {
            List<Conta> novaLista = new ArrayList<>(lista);
            adapter.submitList(novaLista);
        });
     }

}