package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EventoViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;

public class EventoActivity extends AppCompatActivity {

    String idEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        TextView tvVagasRestantes = findViewById(R.id.tvVagasRestantes);

        Intent i = getIntent();

        idEvento = Integer.toString(i.getIntExtra("id", 0));
        EventoViewModel eventoViewModel = new ViewModelProvider(EventoActivity.this)
                .get(EventoViewModel.class);
        LiveData<Evento> eventoLiveData = eventoViewModel.getEventDetail(idEvento);
        eventoLiveData.observe(EventoActivity.this, new Observer<Evento>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Evento evento) {
                ImageView imvFotoEvento = findViewById(R.id.imvFotoEvento);
                TextView tvDescEvento = findViewById(R.id.tvDescEvento);
                TextView tvLocalizacaoEvento = findViewById(R.id.tvLocalizacao);
                TextView tvDataEvento = findViewById(R.id.tvData);
                TextView tvPublicoAlvo = findViewById(R.id.tvPublicoAlvoEvento);
                TextView tvEsporteEvento = findViewById(R.id.tvEsporteEvento);
                TextView tvHorarioEvento = findViewById(R.id.tvHorario);
                TextView tvCriadorEvento = findViewById(R.id.tvCriadorEvento);
                TextView tvIntuitoEvento = findViewById(R.id.tvIntuitoEvento);
                TextView tvPrecoEvento = findViewById(R.id.tvPrecoEvento);

                ImageCache.loadImageUrlToImageView(EventoActivity.this, evento.imagem,
                        imvFotoEvento,200, 200);

                setTitle(evento.nome);
                tvIntuitoEvento.setText(evento.intuito);
                tvDescEvento.setText(evento.descricao);
                tvLocalizacaoEvento.setText(evento.endereco);
                String data = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(
                        evento.data);
                tvDataEvento.setText(data);

                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                Date d = new Date();
                Date d2 = new Date();
                try {
                    // Use o método parse para converter a string em um objeto Date
                    d2 = df.parse(evento.horario_fim);
                    d = df.parse(evento.horario_inicio);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String horario_inicio = new SimpleDateFormat("HH:mm").format(d);
                String horario_fim = new java.text.SimpleDateFormat("HH:mm").format(d2);
                tvHorarioEvento.setText(horario_inicio + " " + horario_fim);
                tvPublicoAlvo.setText(evento.idade_publico);
                tvEsporteEvento.setText(evento.classificao);
                tvCriadorEvento.setText(evento.usuario);
                tvPrecoEvento.setText(evento.preco);
                idEvento = Integer.toString(evento.id);
            }
        });

        Button btnInscrever = findViewById(R.id.btnInscrever);
        btnInscrever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EventoActivity.this);
                builder.setMessage("Deseja mesmo confirmar a inscrição?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LiveData<Boolean> resultLd = eventoViewModel.inscreverEvento(idEvento);

                                resultLd.observe(EventoActivity.this, new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(Boolean aBoolean) {
                                        if(aBoolean){
                                            Toast.makeText(EventoActivity.this, "Inscrito com sucesso!",
                                                    Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(EventoActivity.this,
                                                    HomeActivity.class);
                                            // Comentar sobre o resultado da Activity do produto e comparar com o
                                            // nosso
                                            startActivity(i);
                                            finish();
                                        }else{
                                            v.setEnabled(true);
                                            Toast.makeText(EventoActivity.this, "Ocorreu um " +
                                                            "erro ao se inscrever no evento",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //
                            }
                        });
                // Create the AlertDialog object and return it
                Dialog dlgConfirmar = builder.create();
                dlgConfirmar.show();

            }
        });

        LiveData<Integer> vagasLiveData = eventoViewModel.pegarVagasRestantes(Integer.
                parseInt(idEvento));
        vagasLiveData.observe(EventoActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvVagasRestantes.setText(Integer.toString(integer));
            }
        });


        // FALTA IFS DOS BOTÕES DA INTERFACE (VC VAI ENTENDER)

    }
}