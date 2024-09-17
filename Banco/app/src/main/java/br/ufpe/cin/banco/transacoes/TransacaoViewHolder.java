package br.ufpe.cin.banco.transacoes;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.ufpe.cin.banco.R;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TransacaoViewHolder extends RecyclerView.ViewHolder {

    // Informações da transação
    TextView valorTransacao = null;
    TextView numeroConta = null;
    TextView dataTransacao = null;
    Context context = null;

    public TransacaoViewHolder(@NonNull View linha) {
        super(linha);
        this.valorTransacao = linha.findViewById(R.id.valorTransacao);
        this.numeroConta = linha.findViewById(R.id.numeroContaTransacao);
        this.dataTransacao = linha.findViewById(R.id.dataTransacao);
        this.context = linha.getContext();
    }

    void bindTo(Transacao t) {

        // Formatação do valor da transação para formato da moeda brasileira
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = formatadorMoeda.format(t.valorTransacao);
        this.valorTransacao.setText(valorFormatado);

        // Muda valor da transação para vermelho caso seja débito
        if (t.tipoTransacao == 'D') {
            this.valorTransacao.setTextColor(Color.RED);
            this.valorTransacao.setText(valorFormatado);
        }
        // Muda valor da transação para azul caso seja crédito
        else {
            this.valorTransacao.setTextColor(Color.BLUE);
            this.valorTransacao.setText(valorFormatado);
        }

        // Envia informações de número da conta e data da transação
        this.numeroConta.setText("Conta: " + t.numeroConta);
        this.dataTransacao.setText(t.dataTransacao);
    }
}