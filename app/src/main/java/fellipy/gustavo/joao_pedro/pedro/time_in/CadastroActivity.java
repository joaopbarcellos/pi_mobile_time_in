package fellipy.gustavo.joao_pedro.pedro.time_in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fellipy.gustavo.joao_pedro.pedro.time_in.Model.CadastroViewModel;

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

                EditText etSenhaCadastro = findViewById(R.id.etSenhaCadastro);
                final String senha = etSenhaCadastro.getText().toString();

                EditText etConfirmarSenhaCadastro = findViewById(R.id.etConfirmarSenhaCadastro);
                final String confirmar_senha = etConfirmarSenhaCadastro.getText().toString();

                LiveData<Boolean> resultLD = cadastroViewModel.cadastro(nome, date, email, senha, 0);

            }
        });
    }
}