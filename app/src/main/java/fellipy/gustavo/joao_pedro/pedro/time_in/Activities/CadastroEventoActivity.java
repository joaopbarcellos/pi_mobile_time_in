package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fellipy.gustavo.joao_pedro.pedro.time_in.CEP;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.CadastroEventoViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EditarPerfilViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.api.RESTService;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Mascara;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroEventoActivity extends AppCompatActivity {

    private final String URL = "https://viacep.com.br/ws/";
    static int RESULT_TAKE_PICTURE = 1;
    private Retrofit retrofitCEP;
    private Button btnVerificarCEP;
    private Spinner spnEstado;
    private EditText etBairro, etCidade, etEndereco, etCep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        CadastroEventoViewModel cadastroEventoViewModel = new ViewModelProvider
                (CadastroEventoActivity.this).get(CadastroEventoViewModel.class);


        EditText etNome = findViewById(R.id.et_nome_evento);
        EditText etData = findViewById(R.id.et_data_evento);
        EditText etHorarioInicio = findViewById(R.id.et_horario_evento);
        EditText etHorarioFim = findViewById(R.id.et_horario_final);
        EditText etCapacidadeMax = findViewById(R.id.et_capacidade_maxima);
        EditText etCapacidadeMin = findViewById(R.id.et_capacidade_minima);
        EditText etPrecoInscricao = findViewById(R.id.et_preco_evento);
        EditText etNumero = findViewById(R.id.etNumero);
        EditText etDescricao = findViewById(R.id.etDescricaoEvento);
        CheckBox checkBox = findViewById(R.id.cb_evento_pago);
        Spinner spnEsporte = findViewById(R.id.spnEsportes);
        Spinner spnIntuitoEvento = findViewById(R.id.spn_intuito);
        Spinner spnIdadePublico = findViewById(R.id.spn_idade_publico);
        Button btnEscolherFoto = findViewById(R.id.btnAdicionarFoto);
        Button btnAddEvento = findViewById(R.id.btnCadastrarEvento);

        etCep = findViewById(R.id.etCep);
        etBairro = findViewById(R.id.etBairro);
        etCidade = findViewById(R.id.etCidade);
        etEndereco = findViewById(R.id.etEndereco);
        spnEstado = findViewById(R.id.spnEstados);

        etCep.addTextChangedListener(Mascara.insert(Mascara.MASCARA_CEP, etCep));

        retrofitCEP = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        btnVerificarCEP = findViewById(R.id.btnVerificaCep);
        btnVerificarCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnVerificaCep:
                        if (validarCampos()) {
                            esconderTeclado();
                            consultarCEP();
                        }
                        break;
                }
            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    etPrecoInscricao.setEnabled(false);
                    etPrecoInscricao.setVisibility(View.GONE);
                    etPrecoInscricao.setText("0");
                }else{
                    etPrecoInscricao.setEnabled(true);
                    etPrecoInscricao.setVisibility(View.VISIBLE);
                }
            }
        });


        btnEscolherFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchGalleryOrCameraIntent();
            }
        });

        btnAddEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                String name = etNome.getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(CadastroEventoActivity.this,
                                    "O campo nome do usuário não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String preco = etPrecoInscricao.getText().toString();
                if(preco.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo preço do usuário não foi preeenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String data = etData.getText().toString();
                if(data.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo data do usuário não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String horarioInicio = etHorarioInicio.getText().toString();
                if(horarioInicio.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo horário inicial não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String horarioFim = etHorarioFim.getText().toString();
                if(horarioFim.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo horário final não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String capacidadeMaxima = etCapacidadeMax.getText().toString();
                if(capacidadeMaxima.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo capacidade máxima não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String capacidadeMinima = etCapacidadeMin.getText().toString();
                if(capacidadeMinima.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo capacidade mínima não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String cep = etCep.getText().toString();
                if(cep.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo CEP não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String bairro = etBairro.getText().toString();
                if(bairro.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo bairro não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String endereco = etEndereco.getText().toString();
                if(endereco.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo endereço não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String cidade = etCidade.getText().toString();
                if(cidade.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo cidade não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String numero = etNumero.getText().toString();
                if(numero.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo número não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                String descricao = etDescricao.getText().toString();
                if(descricao.isEmpty()){
                    Toast.makeText(CadastroEventoActivity.this,
                            "O campo descrição não foi preenchido",
                            Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                String currentPhotoPath = cadastroEventoViewModel.getCurrentPhotoPath();

                try {
                    int h = (int) getResources().getDimension(R.dimen.img_height);
                    Util.scaleImage(currentPhotoPath, -1, 2*h);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                ArrayList<String> list = new ArrayList<>();
                list.add(descricao);
                list.add(name);
                list.add(data);
                list.add(horarioInicio);
                list.add(horarioFim);
                list.add(capacidadeMinima);
                list.add(preco);
                list.add(currentPhotoPath);
                list.add(capacidadeMaxima);
                list.add(spnIntuitoEvento.getSelectedItem().toString());
                list.add(spnEstado.getSelectedItem().toString());
                list.add(cidade);
                list.add(bairro);
                list.add(endereco);
                list.add(numero);
                list.add(cep);
                list.add(spnIdadePublico.getSelectedItem().toString());
                list.add(spnEsporte.getSelectedItem().toString());

                LiveData<Boolean> resultLd = cadastroEventoViewModel.addEvent(list);

                resultLd.observe(CadastroEventoActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            Toast.makeText(CadastroEventoActivity.this, "O evento " +
                                    "foi cadastrado com sucesso.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(CadastroEventoActivity.this,
                                    HomeActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            v.setEnabled(true);
                            Toast.makeText(CadastroEventoActivity.this, "Ocorreu " +
                                    "um erro ao cadastrar as informações do evento.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // FALTA MEXER NO PHP
            }
        });


    }
    private Boolean validarCampos(){
        Boolean status = true;
        String cep = etCep.getText().toString().trim();

        if(cep.isEmpty()){
            etCep.setError("Digite um CEP válido.");
            status = false;
        }

        if ((cep.length() > 1) && (cep.length() < 10)){
            etCep.setError("O CEP deve possuir 8 dígitos.");
            status = false;
        }
        return status;
    }


    private void esconderTeclado() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void consultarCEP(){
        String sCep = etCep.getText().toString().trim();

        sCep = sCep.replaceAll("[.-]+", "");

        RESTService restService = retrofitCEP.create(RESTService.class);

        Call<CEP> call = restService.consultarCEP(sCep);

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if(response.isSuccessful()){

                    List<String> estados = new ArrayList<>();
                    estados.add("AC");
                    estados.add("AL");
                    estados.add("AM");
                    estados.add("AP");
                    estados.add("BA");
                    estados.add("CE");
                    estados.add("DF");
                    estados.add("ES");
                    estados.add("GO");
                    estados.add("MA");
                    estados.add("MG");
                    estados.add("MS");
                    estados.add("MT");
                    estados.add("PA");
                    estados.add("PE");
                    estados.add("PI");
                    estados.add("PR");
                    estados.add("RJ");
                    estados.add("RN");
                    estados.add("RO");
                    estados.add("RR");
                    estados.add("RS");
                    estados.add("SC");
                    estados.add("SE");
                    estados.add("SP");
                    estados.add("TO");

                    CEP cep = response.body();
                    etEndereco.setText(cep.getLogradouro());
                    etBairro.setText(cep.getBairro());
                    etCidade.setText(cep.getLocalidade());
                    spnEstado.setSelection(estados.indexOf(cep.getUf()));
                    Toast.makeText(getApplicationContext(), "CEP consultado com sucesso", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Ocorreu um erro ao tentar consultar o CEP. Erro: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    private void dispatchGalleryOrCameraIntent() {

        // Primeiro, criamos o arquivo que irá guardar a imagem.
        File f = null;
        try {
            f = createImageFile();
        } catch (IOException e) {
            Toast.makeText(CadastroEventoActivity.this, "Não foi possível criar o arquivo"
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
            Uri fUri = FileProvider.getUriForFile(CadastroEventoActivity.this,
                    "fellipy.gustavo.joao.pedro.time_in.fileprovider", f);
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
            Toast.makeText(CadastroEventoActivity.this, "Não foi possível criar o arquivo"
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