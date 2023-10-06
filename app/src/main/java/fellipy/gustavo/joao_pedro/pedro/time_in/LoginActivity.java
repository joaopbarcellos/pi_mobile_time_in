package fellipy.gustavo.joao_pedro.pedro.time_in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fellipy.gustavo.joao_pedro.pedro.time_in.Model.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    static int RESULT_REQUEST_PERMISSION = 2;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        Button btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etEmail = findViewById(R.id.etEmail);
                final String login = etEmail.getText().toString();

                EditText etSenha = findViewById(R.id.etSenha);
                final String password = etSenha.getText().toString();

                LiveData<Boolean> resultLD = loginViewModel.login(login, password);
            }
        });
    }
}