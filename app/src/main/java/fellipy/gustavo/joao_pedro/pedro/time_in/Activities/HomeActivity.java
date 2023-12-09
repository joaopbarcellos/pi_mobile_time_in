package fellipy.gustavo.joao_pedro.pedro.time_in.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import fellipy.gustavo.joao_pedro.pedro.time_in.Model.HomeViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.fragments.MeusEventosFragment;
import fellipy.gustavo.joao_pedro.pedro.time_in.fragments.PerfilFragment;
import fellipy.gustavo.joao_pedro.pedro.time_in.fragments.TopEventosFragment;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Config;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    static int RESULT_REQUEST_PERMISSION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        List<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        checkForPermissions(permissions);

        //TopEventosFragment topEventosFragment = TopEventosFragment.newInstance();
        //setFragment(topEventosFragment);
        bottomNavigationView = findViewById(R.id.btNav);
        final HomeViewModel vm = new ViewModelProvider(this).get(HomeViewModel.class);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                vm.setNavigationOpSelected(item.getItemId());
                switch (item.getItemId()){
                    case R.id.homeOp:

                        TopEventosFragment topEventosFragment = TopEventosFragment.newInstance();
                        setFragment(topEventosFragment);
                        break;
                    case R.id.meusEventosOp:
                        if(Config.getLogin(HomeActivity.this).isEmpty()) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            MeusEventosFragment meusEventosFragment = MeusEventosFragment.newInstance();
                            setFragment(meusEventosFragment);
                        }
                        break;
                    case R.id.perfilOp:
                        if(Config.getLogin(HomeActivity.this).isEmpty()) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            PerfilFragment perfilFragment = PerfilFragment.newInstance();
                            setFragment(perfilFragment);
                        }
                        break;
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.homeOp);


    }

    void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void navegarTelas(Class classe){
        Intent i = new Intent(HomeActivity.this, classe);
        startActivity(i);
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
            return ActivityCompat.checkSelfPermission(HomeActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
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
                    new AlertDialog.Builder(HomeActivity.this).
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
    public void startEventDetailsActivity(int id){
        // Criando a intenção da PhotoActivity
        Intent i = new Intent(HomeActivity.this, EventoActivity.class);
        // Enviando a foto que foi selecioanda para a intent
        i.putExtra("id", id);
        // Iniciando a Intent
        startActivity(i);
    }

}