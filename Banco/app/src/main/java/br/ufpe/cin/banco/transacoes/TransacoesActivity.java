package br.ufpe.cin.banco.transacoes;

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
import java.util.List;
import br.ufpe.cin.banco.BancoViewModel;
import br.ufpe.cin.banco.R;


public class TransacoesActivity extends AppCompatActivity {

    // ViewModels para acessar os dados do banco e transações
    BancoViewModel bancoViewModel;
    TransacaoViewModel transacaoViewModel;

    // Adapter para o RecyclerView
    TransacaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacoes);

        // Inicializa os ViewModels
        bancoViewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        transacaoViewModel = new ViewModelProvider(this).get(TransacaoViewModel.class);

        // Componentes da interface
        EditText Pesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoTransacao = findViewById(R.id.tipoTransacao);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rv = findViewById(R.id.rvResultado);

        // Configura o RecyclerView
        adapter = new TransacaoAdapter(getLayoutInflater());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


        // Botão de pesquisar transações
        btnPesquisar.setOnClickListener(
                v -> {
                    String oQueFoiDigitado = Pesquisar.getText().toString();
                    int TransacaoSelecionado = tipoTransacao.getCheckedRadioButtonId();
                    int PesquisaSelecionado = tipoPesquisa.getCheckedRadioButtonId();

                    if (TransacaoSelecionado == -1 || PesquisaSelecionado == -1) {
                        Toast.makeText(TransacoesActivity.this, "Selecione o tipo de transação e o tipo de pesquisa", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TransacaoSelecionado == R.id.peloTipoCredito && PesquisaSelecionado == R.id.pelaData){
                        bancoViewModel.buscarTransacoesPelaDataTipo(oQueFoiDigitado, 'C');
                    } else if (TransacaoSelecionado == R.id.peloTipoCredito && PesquisaSelecionado == R.id.peloNumeroConta){
                        bancoViewModel.buscarTransacoesPeloNumero(oQueFoiDigitado, 'C');
                    }else if (TransacaoSelecionado == R.id.peloTipoDebito && PesquisaSelecionado == R.id.pelaData){
                        bancoViewModel.buscarTransacoesPelaDataTipo(oQueFoiDigitado, 'D');
                    } else if (TransacaoSelecionado == R.id.peloTipoDebito && PesquisaSelecionado == R.id.peloNumeroConta){
                        bancoViewModel.buscarTransacoesPeloNumero(oQueFoiDigitado, 'D');
                    } else if (TransacaoSelecionado == R.id.peloTipoTodos && PesquisaSelecionado == R.id.pelaData){
                        bancoViewModel.buscarTransacoesPelaData(oQueFoiDigitado);
                    } else if (TransacaoSelecionado == R.id.peloTipoTodos && PesquisaSelecionado == R.id.peloNumeroConta){
                        bancoViewModel.buscarTransacoesPeloNum(oQueFoiDigitado);
                    }
                }

        );

        // Atualiza a lista no adapter
        bancoViewModel.listaTransacoes.observe(
                this,
                lista -> {
                    List<Transacao> novaLista = new ArrayList<>(lista);
                    adapter.submitList(novaLista);
                }
        );

        // Atualiza a lista de transações no RecyclerView
        transacaoViewModel.transacoes.observe(
                this,
                todasTransacoes -> {
                    adapter.submitList(todasTransacoes);
                }
        );

    }

}