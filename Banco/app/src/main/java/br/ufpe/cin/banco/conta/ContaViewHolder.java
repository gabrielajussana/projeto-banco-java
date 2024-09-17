package br.ufpe.cin.banco.conta;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;

import br.ufpe.cin.banco.R;

public class ContaViewHolder  extends RecyclerView.ViewHolder {
    TextView nomeCliente = null;
    TextView infoConta = null;
    ImageView icone = null;
    ImageView btnEdit = null;
    ImageView btnDelete = null;
    Context context = null;
    ContaViewModel viewModel;

    public ContaViewHolder(@NonNull View linha) {
        super(linha);
        this.nomeCliente = linha.findViewById(R.id.nomeCliente);
        this.infoConta = linha.findViewById(R.id.infoConta);
        this.icone = linha.findViewById(R.id.icone);
        this.btnEdit = linha.findViewById(R.id.btn_edit);
        this.btnDelete = linha.findViewById(R.id.btn_delete);
        this.context = linha.getContext();
    }

    public void setViewModel(ContaViewModel viewModel) {
        this.viewModel = viewModel;
    }

    void bindTo(Conta c) {
        this.nomeCliente.setText(c.nomeCliente);
        this.infoConta.setText(c.numero + " | " + "Saldo atual: R$" + NumberFormat.getCurrencyInstance().format(c.saldo));
        //Atualizar a imagem de acordo com o valor do saldo atual
        if (c.saldo >= 0) {
            this.icone.setImageResource(R.drawable.ok);
        } else {
            this.icone.setImageResource(R.drawable.delete);
        }

        this.btnEdit.setOnClickListener(
                v -> {
                    Intent i = new Intent(
                            this.context,
                            EditarContaActivity.class
                    );
                    //Passar o número da conta pelo Intent
                    i.putExtra(EditarContaActivity.KEY_NUMERO_CONTA, c.numero);
                    this.context.startActivity(i);
                }
        );

        this.btnDelete.setOnClickListener(
                v -> {
                    Intent i = new Intent(context, EditarContaActivity.class);
                    i.putExtra(EditarContaActivity.KEY_NUMERO_CONTA, c.numero);
                    i.putExtra("remover", true);
                    Toast.makeText(context, "Conta deletada com sucesso!", Toast.LENGTH_SHORT).show();
                    context.startActivity(i);
                }
        );
    }
}
