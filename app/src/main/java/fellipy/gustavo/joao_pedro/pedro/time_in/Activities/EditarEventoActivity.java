package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EditarEventoViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EventoViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;

public class EditarEventoActivity extends AppCompatActivity {

    String idEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);
        Intent i = getIntent();

        idEvento = i.getStringExtra("id_evento");

        EditText etEditarNomeEvento = findViewById(R.id.etEditarNomeEvento);
        EditText etEditarCapMaxima = findViewById(R.id.etEditarCapMaxima);
        EditText etEditarCapMin = findViewById(R.id.etEditarCapMin);
        EditText etEditarDesc = findViewById(R.id.etEditarDesc);

        EditarEventoViewModel editarEventoViewModel = new ViewModelProvider(EditarEventoActivity.this)
                .get(EditarEventoViewModel.class);
        LiveData<Evento> eventoLiveData = editarEventoViewModel.getEventDetail(idEvento);
        eventoLiveData.observe(EditarEventoActivity.this, new Observer<Evento>() {
            @Override
            public void onChanged(Evento evento) {
                etEditarNomeEvento.setText(evento.nome);
                etEditarCapMaxima.setText(Integer.toString(evento.max_pessoas));
                etEditarCapMin.setText(Integer.toString(evento.min_pessoas));
                etEditarDesc.setText(evento.descricao);
            }
        });


        Button btnEditarEvento = findViewById(R.id.btnEditarEvento);

        btnEditarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etEditarNomeEvento.getText().toString();
                if(nome.isEmpty()){
                    Toast.makeText(EditarEventoActivity.this,
                            "O campo nome não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                String descricao = etEditarDesc.getText().toString();
                if(descricao.isEmpty()){
                    Toast.makeText(EditarEventoActivity.this,
                            "O campo descrição não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                String capacidade_min = etEditarCapMin.getText().toString();
                if(capacidade_min.isEmpty()){
                    Toast.makeText(EditarEventoActivity.this,
                            "O campo capacidade mínima não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                String capacidade_max = etEditarCapMaxima.getText().toString();
                if(capacidade_max.isEmpty()){
                    Toast.makeText(EditarEventoActivity.this,
                            "O campo capacidade máxima não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                LiveData<Boolean> atualizarLiveData = editarEventoViewModel.updateEventsDetail(
                        idEvento, nome, capacidade_max, capacidade_min, descricao);
                atualizarLiveData.observe(EditarEventoActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            Toast.makeText(EditarEventoActivity.this, "O evento foi " +
                                    "alterado com sucesso.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(EditarEventoActivity.this,
                                    HomeActivity.class);
                            // Comentar sobre o resultado da Activity do produto e comparar com o
                            // nosso
                            startActivity(i);
                            finish();
                        }else{
                            v.setEnabled(true);
                            Toast.makeText(EditarEventoActivity.this, "Ocorreu um " +
                                            "erro ao alterar as informações do evento",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}