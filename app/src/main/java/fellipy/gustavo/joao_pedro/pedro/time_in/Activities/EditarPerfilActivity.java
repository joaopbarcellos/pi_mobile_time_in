package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EditarPerfilViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;

public class EditarPerfilActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        ImageButton imgFoto = findViewById(R.id.imgbtnFotoPerfil2);

        EditarPerfilViewModel editarPerfilViewModel = new
                ViewModelProvider(EditarPerfilActivity.this).get(EditarPerfilViewModel.class);
        LiveData<Usuario> usuarioLiveData = editarPerfilViewModel.loadUserDetailsLv();

        usuarioLiveData.observe(EditarPerfilActivity.this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                EditText tvNome = findViewById(R.id.etNomeEditarPerfil2);
                EditText tvEmail = findViewById(R.id.etEmailEditarPerfil2);
                EditText tvDataNasc = findViewById(R.id.etDataEditarPerfil2);
                EditText tvTelefone = findViewById(R.id.etEditarPerfilTelefone);
                tvNome.setText(usuario.nome);
                tvEmail.setText(usuario.email);
                tvTelefone.setText(usuario.telefone);
                tvDataNasc.setText(new SimpleDateFormat("dd/MM/yyyy").format(usuario.dataNasc));
                ImageCache.loadImageUrlToImageView(EditarPerfilActivity.this, usuario.foto, imgFoto,200, 200);
            }
        });


        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ESCOLHER FOTO USUARIO
            }
        });
    }
}