package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fellipy.gustavo.joao_pedro.pedro.time_in.CEP;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.api.RESTService;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Mascara;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroEventoActivity extends AppCompatActivity {

    private final String URL = "https://viacep.com.br/ws/";
    private Retrofit retrofitCEP;
    private Button btnVerificarCEP;
    private Spinner spnEstado;
    private EditText etBairro, etCidade, etEndereco, etCep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

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
}