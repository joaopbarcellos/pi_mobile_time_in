package fellipy.gustavo.joao_pedro.pedro.time_in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fellipy.gustavo.joao_pedro.pedro.time_in.Model.LoginViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Config;

public class LoginActivity extends AppCompatActivity {

    static int RESULT_REQUEST_PERMISSION = 2;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        List<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        checkForPermissions(permissions);
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
                resultLD.observe(LoginActivity.this, new Observer<Boolean>() {

                    // Ao ser chamado, o método onChanged informa também qual foi o resultado
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        // aBoolean contém o resultado do login. Se aBoolean for true, significa
                        // que as infos de login e senha enviadas ao servidor estão certas. Neste
                        // caso, guardamos as infos de login e senha dentro da app através da classe
                        // Config. Essas infos de login e senha precisam ser guardadas dentro da app
                        // para que possam ser usadas quando a app pedir dados ao servidor web que só
                        // podem ser obtidos se o usuário enviar o login e senha.
                        if(aBoolean) {

                            // guarda os dados de login e senha dentro da app
                            Config.setLogin(LoginActivity.this, login);
                            Config.setPassword(LoginActivity.this, password);

                            // exibe uma mensagem indicando que o login deu certo
                            Toast.makeText(LoginActivity.this, "Login realizado com sucesso", Toast.LENGTH_LONG).show();

                            // Navega para tela principal
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                        else {

                            // Se o login não deu certo, apenas continuamos na tela de login e
                            // indicamos com uma mensagem ao usuário que o login não deu certo.
                            Toast.makeText(LoginActivity.this, "Não foi possível realizar o login da aplicação", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        Button btnCriarConta = findViewById(R.id.btnCriarConta);
        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(i);
            }
        });
    }
    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();

        for(String permission : permissions) {
            if( !hasPermission(permission)) {
                permissionsNotGranted.add(permission);
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(permissionsNotGranted.size() > 0) {
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]),RESULT_REQUEST_PERMISSION);
            }
        }
    }
    private boolean hasPermission(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(LoginActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<String> permissionsRejected = new ArrayList<>();
        if(requestCode == RESULT_REQUEST_PERMISSION) {

            for(String permission : permissions) {
                if(!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }

        if(permissionsRejected.size() > 0) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(LoginActivity.this).
                            setMessage("Para usar essa app é preciso conceder essas permissões").
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), RESULT_REQUEST_PERMISSION);
                                }
                            }).create().show();
                }
            }
        }
    }
}