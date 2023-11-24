package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EditarPerfilViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Util;

public class EditarPerfilActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        EditText tvNome = findViewById(R.id.etNomeEditarPerfil2);
        EditText tvEmail = findViewById(R.id.etEmailEditarPerfil2);
        EditText tvDataNasc = findViewById(R.id.etDataEditarPerfil2);
        EditText tvTelefone = findViewById(R.id.etEditarPerfilTelefone);
        ImageButton imgFoto = findViewById(R.id.imgbtnFotoPerfil2);
        final String[] id = {""};
        EditarPerfilViewModel editarPerfilViewModel = new
                ViewModelProvider(EditarPerfilActivity.this).get(EditarPerfilViewModel.class);
        LiveData<Usuario> usuarioLiveData = editarPerfilViewModel.loadUserDetailsLv();

        usuarioLiveData.observe(EditarPerfilActivity.this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                id[0] = Integer.toString(usuario.id);
                tvNome.setText(usuario.nome);
                tvEmail.setText(usuario.email);
                tvTelefone.setText(usuario.telefone);
                tvDataNasc.setText(new SimpleDateFormat("dd/MM/yyyy").format(usuario.dataNasc));
                ImageCache.loadImageUrlToImageView(EditarPerfilActivity.this, usuario.foto, imgFoto,200, 200);
            }
        });

        String currentPhotoPath = editarPerfilViewModel.getCurrentPhotoPath();
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ESCOLHER FOTO USUARIO
            }
        });

        Button btnAlterar = findViewById(R.id.btnAlterarPerfil);
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                String name = tvNome.getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "O campo nome do usuário não foi preenchido", Toast.LENGTH_LONG)
                            .show();
                    v.setEnabled(true);
                    return;
                }
                String email = tvEmail.getText().toString();
                if(email.isEmpty()) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "O campo email do usuário não foi preenchido", Toast.LENGTH_LONG)
                            .show();
                    v.setEnabled(true);
                    return;
                }
                String data = tvDataNasc.getText().toString();
                data = new SimpleDateFormat("yyyy-MM-dd").format(data);
                if(data.isEmpty()) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "O campo data do usuário não foi preenchido", Toast.LENGTH_LONG)
                            .show();
                    v.setEnabled(true);
                    return;
                }
                String telefone = tvTelefone.getText().toString();
                if(telefone.isEmpty()) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "O campo telefone do usuário não foi preenchido",
                                    Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String currentPhotoPath = editarPerfilViewModel.getCurrentPhotoPath();
                if(currentPhotoPath.isEmpty()) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "O campo foto do usuário não foi preenchido", Toast.LENGTH_LONG)
                            .show();
                    v.setEnabled(true);
                    return;
                }

                try {
                    int h = (int) getResources().getDimension(R.dimen.img_height);
                    Util.scaleImage(currentPhotoPath, -1, 2*h);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }


                if(editarPerfilViewModel.updateUserDetails(id[0], name, email, data, telefone)){
                    Toast.makeText(EditarPerfilActivity.this, "O usuário foi " +
                            "alterado com sucesso.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(EditarPerfilActivity.this,
                            HomeActivity.class);
                    startActivity(i);
                    return;
                }
            }
        });
    }
}