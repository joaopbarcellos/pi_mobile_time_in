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
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fellipy.gustavo.joao_pedro.pedro.time_in.Model.CadastroViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Mascara;

public class CadastroActivity extends AppCompatActivity {

    CadastroViewModel cadastroViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        cadastroViewModel = new ViewModelProvider(this).get(CadastroViewModel.class);

        Button btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etNome = findViewById(R.id.etNome);
                final String nome = etNome.getText().toString();

                EditText etData = findViewById(R.id.etData);
                etData.addTextChangedListener(Mascara.insert(Mascara.MASCARA_DATA, etData));
                final String data = etData.getText().toString();
                SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = parser.parse(data);
                } catch (ParseException e) {
                    Toast.makeText(CadastroActivity.this, "A data tem que ser dd/MM/yyyy", Toast.LENGTH_LONG).show();
                }

                EditText etEmailCadastro = findViewById(R.id.etEmailCadastro);
                final String email = etEmailCadastro.getText().toString();

                EditText etTelefone = findViewById(R.id.etTelefone);
                final String telefone = etTelefone.getText().toString();

                EditText etSenhaCadastro = findViewById(R.id.etSenhaCadastro);
                final String senha = etSenhaCadastro.getText().toString();

                EditText etConfirmarSenhaCadastro = findViewById(R.id.etConfirmarSenhaCadastro);
                final String confirmar_senha = etConfirmarSenhaCadastro.getText().toString();

                RadioGroup radioButtonGroup = findViewById(R.id.rgIntuito);
                int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonID);
                int idx = radioButtonGroup.indexOfChild(radioButton);

                LiveData<Boolean> resultLD = cadastroViewModel.cadastro(nome, date, email, senha, telefone, Integer.toString(idx));

                resultLD.observe(CadastroActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean == true){
                            Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            Intent loginActivity = new Intent(CadastroActivity.this, LoginActivity.class);
                            startActivity(loginActivity);
                            finish();
                        } else {
                            v.setEnabled(true);
                            Toast.makeText(CadastroActivity.this, "Ocorreu um erro ao se cadastrar", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

        });

    }
}