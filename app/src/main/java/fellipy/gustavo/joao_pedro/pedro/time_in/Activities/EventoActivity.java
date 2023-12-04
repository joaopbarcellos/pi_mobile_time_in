package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EventoViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;

public class EventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        TextView tvVagasRestantes = findViewById(R.id.tvVagasRestantes);

        Intent i = getIntent();

        int id = i.getIntExtra("id", 0);
        EventoViewModel eventoViewModel = new ViewModelProvider(EventoActivity.this)
                .get(EventoViewModel.class);
        LiveData<Evento> eventoLiveData = eventoViewModel.getEventDetail(id);
        eventoLiveData.observe(EventoActivity.this, new Observer<Evento>() {
            @Override
            public void onChanged(Evento evento) {
                ImageView imvFotoEvento = findViewById(R.id.imvFotoEvento);
                TextView tvDescEvento = findViewById(R.id.tvDescEvento);
                TextView tvLocalizacaoEvento = findViewById(R.id.tvLocalizacao);
                TextView tvDataEvento = findViewById(R.id.tvData);
                TextView tvPublicoAlvo = findViewById(R.id.tvPublicoAlvo);
                TextView tvEsporteEvento = findViewById(R.id.tvEsporteEvento);
                TextView tvHorarioEvento = findViewById(R.id.tvHorario);
                TextView tvCriadorEvento = findViewById(R.id.tvCriadorEvento);
                TextView tvIntuitoEvento = findViewById(R.id.tvIntuitoEvento);

                ImageCache.loadImageUrlToImageView(EventoActivity.this, evento.imagem,
                        imvFotoEvento,200, 200);

                tvIntuitoEvento.setText(evento.intuito);
                tvDescEvento.setText(evento.descricao);
                tvLocalizacaoEvento.setText(evento.endereco);
                tvDataEvento.setText(new SimpleDateFormat("dd/MM/yyyy").format(evento.data));
                tvHorarioEvento.setText(new SimpleDateFormat("HH:mm").
                        format(evento.horario_inicio) + new SimpleDateFormat("HH:mm").
                        format(evento.horario_fim));
                tvPublicoAlvo.setText(evento.idade_publico);
                tvEsporteEvento.setText(evento.classificao);
                tvCriadorEvento.setText(evento.usuario);
            }
        });

        LiveData<Integer> vagasLiveData = eventoViewModel.pegarVagasRestantes(id);
        vagasLiveData.observe(EventoActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvVagasRestantes.setText(Integer.toString(integer));
            }
        });


        // FALTA IFS DOS BOTÕES DA INTERFACE (VC VAI ENTENDER)

    }
}