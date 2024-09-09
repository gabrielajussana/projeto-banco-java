package br.ufpe.cin.banco.conta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufpe.cin.banco.BancoDB;
import br.ufpe.cin.banco.R;

public class ContasActivity extends AppCompatActivity {
    ContaAdapter adapter;
    ContaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas);
        viewModel = new ViewModelProvider(this).get(ContaViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.rvContas);
        adapter = new ContaAdapter(getLayoutInflater());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button adicionarConta = findViewById(R.id.btn_Adiciona);
        adicionarConta.setOnClickListener(
                v -> {
                    startActivity(new Intent(this, AdicionarContaActivity.class));
                }
        );
        listaContas();
    }

    public void listaContas(){
        buscarContas(1000);
    }

    private void buscarContas(int tempo) {
        final Handler handler = new Handler();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Conta> conta = BancoDB.getDB(getApplicationContext()).contaDAO().todasContas();
                adapter.submitList(conta);
            }
        });
        thread.start();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                listaContas();
            }
        };
        handler.postDelayed(runnable, tempo);
    }
}