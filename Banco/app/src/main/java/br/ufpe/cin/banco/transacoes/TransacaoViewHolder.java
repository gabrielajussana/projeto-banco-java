package br.ufpe.cin.banco.transacoes;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;

import br.ufpe.cin.banco.R;
import br.ufpe.cin.banco.conta.Conta;
import br.ufpe.cin.banco.conta.EditarContaActivity;

public class TransacaoViewHolder extends RecyclerView.ViewHolder {
    RadioGroup tipoTransacao = null;
    TextView valorTransacao = null;
    TextView numeroConta = null;
    TextView dataTransacao = null;
    Context context = null;


    public TransacaoViewHolder(@NonNull View linha) {
        super(linha);
        this.tipoTransacao = linha.findViewById(R.id.tipoTransacao);
        this.valorTransacao = linha.findViewById(R.id.valorTransacao);
        this.numeroConta = linha.findViewById(R.id.numeroContaTransacao);
        this.dataTransacao = linha.findViewById(R.id.dataTransacao);
        this.context = linha.getContext();
    }

    void bindTo(Transacao t) {
        if(t.tipoTransacao == 'D') {
            this.valorTransacao.setTextColor(context.getResources().getColor(R.color.red, null));
        } else {
            this.valorTransacao.setTextColor(context.getResources().getColor(R.color.green, null));
        }
        this.valorTransacao.setText(String.valueOf(t.valorTransacao));
        this.numeroConta.setText("Número da Conta: " + t.numeroConta);
        this.dataTransacao.setText(t.dataTransacao);

    }
}