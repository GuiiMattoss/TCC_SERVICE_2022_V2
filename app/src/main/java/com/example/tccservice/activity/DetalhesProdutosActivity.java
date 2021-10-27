package com.example.tccservice.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tccservice.model.Anuncio;
import com.example.tccservice.R;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class DetalhesProdutosActivity extends AppCompatActivity {

    private CarouselView carouselView;
    private TextView titulo, descricao, estado, preco, telefone;
    private Anuncio anuncioSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produtos);

        getSupportActionBar().setTitle("Detalhe Produto");

        inicializarComponentes();

        anuncioSelecionado = (Anuncio) getIntent().getSerializableExtra("anuncioSelecionado");

        if (anuncioSelecionado != null){

            titulo.setText(anuncioSelecionado.getTitulo());
            descricao.setText(anuncioSelecionado.getDescricao());
            estado.setText(anuncioSelecionado.getEstado());
            preco.setText(anuncioSelecionado.getValor());
            telefone.setText(anuncioSelecionado.getTelefone());


            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlString = anuncioSelecionado.getFotos().get(position);
                    Picasso.get().load(urlString).into(imageView);
                }
            };

            carouselView.setPageCount(anuncioSelecionado.getFotos().size());
            carouselView.setImageListener(imageListener);

        }


    }

    public void vizualizarTelefone(View view){

        String numeroWhats = telefone.getText().toString();

        if (!numeroWhats.isEmpty()){

            dialogContato(numeroWhats);

        }else {
            Toast.makeText(DetalhesProdutosActivity.this, "Numero de contato indisponivel", Toast.LENGTH_SHORT).show();
        }

    }

    public void inicializarComponentes(){
        carouselView = findViewById(R.id.carouselView);
        titulo = findViewById(R.id.textTituloDetalhe);
        descricao = findViewById(R.id.textDescricaoDetalhe);
        estado = findViewById(R.id.textEstadoDetalhe);
        preco = findViewById(R.id.textPrecoDetalhe);
        telefone = findViewById(R.id.button_whats);

    }

    private void dialogContato (String numero){

        String contato = numero.replace("","").replace("-","")
                .replace("(","").replace(")","");

        StringBuffer numeroContato = new StringBuffer(contato);

        AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesProdutosActivity.this)
                .setTitle("Entrar em contato")
                .setMessage("Escolha a forma de contato:")
                .setPositiveButton("WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        entrarEmContatoWhatsapp(numeroContato);
                    }
                })
                .setNegativeButton("Ligar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        entrarEmContatoLigacao(numeroContato);

                    }
                });

        builder.show();
    }

    private void entrarEmContatoWhatsapp(StringBuffer numeroContato){

        numeroContato.deleteCharAt(0);
        numeroContato.deleteCharAt(2);

        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent( new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
        intent.putExtra("jid", PhoneNumberUtils.stripSeparators("55"+numeroContato) + "@s.whatsapp.net");


    }

    private void entrarEmContatoLigacao(StringBuffer numeroContato){
        Uri uri = Uri.parse("tel:"+numeroContato);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);
    }


}