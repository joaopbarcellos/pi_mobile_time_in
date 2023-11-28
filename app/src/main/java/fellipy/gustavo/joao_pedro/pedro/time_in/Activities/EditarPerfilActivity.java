package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EditarPerfilViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Util;

public class EditarPerfilActivity extends AppCompatActivity {

    static int RESULT_TAKE_PICTURE = 1;
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
                tvDataNasc.setText(new SimpleDateFormat("dd/MM/yyyy")
                        .format(usuario.dataNasc));
                ImageCache.loadImageUrlToImageView(EditarPerfilActivity.this, usuario.foto,
                        imgFoto,200, 200);
            }
        });

        String currentPhotoPath = editarPerfilViewModel.getCurrentPhotoPath();
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchGalleryOrCameraIntent();
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

                final String data = tvDataNasc.getText().toString();
                if(data.isEmpty()) {
                    Toast.makeText(EditarPerfilActivity.this,
                                    "O campo data do usuário não foi preenchido",
                                    Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = parser.parse(data);
                } catch (ParseException e) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "A data tem que ser dd/MM/yyyy", Toast.LENGTH_LONG).show();
                }

                String s = new SimpleDateFormat("yyyy-MM-dd").format(date);

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
                    // PERGUNTAR PRO DANIEL SOBRE PEGAR A FOTO DO IMGVIEW E ESSA BOMBA AKI
                }

                try {
                    int h = (int) getResources().getDimension(R.dimen.img_height);
                    Util.scaleImage(currentPhotoPath, -1, 2*h);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }


                LiveData<Boolean> resultLd = editarPerfilViewModel.updateUserDetails(id[0],
                        name, email, s, telefone, currentPhotoPath);

                resultLd.observe(EditarPerfilActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            Toast.makeText(EditarPerfilActivity.this, "O usuário foi " +
                                    "alterado com sucesso.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(EditarPerfilActivity.this,
                                    HomeActivity.class);
                            // Comentar sobre o resultado da Activity do produto e comparar com o
                            // nosso
                            startActivity(i);
                            finish();
                        }else{
                            v.setEnabled(true);
                            Toast.makeText(EditarPerfilActivity.this, "Ocorreu um " +
                                            "erro ao alterar as informações do usuário",
                                            Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
    private void dispatchGalleryOrCameraIntent() {

        // Primeiro, criamos o arquivo que irá guardar a imagem.
        File f = null;
        try {
            f = createImageFile();
        } catch (IOException e) {
            Toast.makeText(EditarPerfilActivity.this, "Não foi possível criar o arquivo"
                    , Toast.LENGTH_LONG).show();
            return;
        }

        // Se o arquivo foi criado com sucesso...
        if(f != null) {

            // setamos o endereço do arquivo criado dentro do ViewModel
            EditarPerfilViewModel editarPerfilViewModel = new ViewModelProvider(this)
                    .get(EditarPerfilViewModel.class);
            editarPerfilViewModel.setCurrentPhotoPath(f.getAbsolutePath());

            // Criamos e configuramos o INTENT que dispara a câmera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri fUri = FileProvider.getUriForFile(EditarPerfilActivity.this,
                    "fellipy.gustavo.joao_pedro_pedro.time_in.fileprovider", f);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fUri);

            // Criamos e configuramos o INTENT que dispara a escolha de imagem via galeria
            Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            galleryIntent.setType("image/*");

            // Criamos o INTENT que gera o menu de escolha. Esse INTENT contém os dois INTENTS
            // anteriores e permite que o usuário esolha entre câmera e galeria de fotos.
            Intent chooserIntent = Intent.createChooser(galleryIntent, "Pegar imagem de...");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });
            startActivityForResult(chooserIntent, RESULT_TAKE_PICTURE);
        }
        else {
            Toast.makeText(EditarPerfilActivity.this, "Não foi possível criar o arquivo"
                    , Toast.LENGTH_LONG).show();
            return;
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);
        return f;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_TAKE_PICTURE) {

            // Pegamos o endereço do arquivo vazio que foi criado para guardar a foto escolhida
            EditarPerfilViewModel editarPerfilViewModel = new ViewModelProvider(this)
                    .get(EditarPerfilViewModel.class);
            String currentPhotoPath = editarPerfilViewModel.getCurrentPhotoPath();

            // Se a foto foi efetivamente escolhida pelo usuário...
            if(resultCode == Activity.RESULT_OK) {
                ImageView imvPhoto = findViewById(R.id.imgbtnFotoPerfil2);

                // se o usuário escolheu a câmera, então quando esse método é chamado, a foto tirada
                // já está salva dentro do arquivo currentPhotoPath. Entretanto, se o usuário
                // escolheu uma foto da galeria, temos que obter o URI da foto escolhida:
                Uri selectedPhoto = data.getData();
                if(selectedPhoto != null) {
                    try {
                        // carregamos a foto escolhida em um bitmap
                        Bitmap bitmap = Util.getBitmap(this, selectedPhoto);
                        // salvamos o bitmao dentro do arquivo currentPhotoPath
                        Util.saveImage(bitmap, currentPhotoPath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                // Carregamos a foto salva em currentPhotoPath com a escala correta e setamos no
                // ImageView
                Bitmap bitmap = Util.getBitmap(currentPhotoPath, imvPhoto.getWidth(), imvPhoto
                        .getHeight());
                imvPhoto.setImageBitmap(bitmap);
            }
            else {
                // Se a imagem não foi escolhida, deletamos o arquivo que foi criado para guardá-la
                File f = new File(currentPhotoPath);
                f.delete();
                editarPerfilViewModel.setCurrentPhotoPath("");
            }
        }
    }
}
