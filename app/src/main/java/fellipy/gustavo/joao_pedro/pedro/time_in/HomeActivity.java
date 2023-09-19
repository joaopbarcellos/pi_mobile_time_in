package fellipy.gustavo.joao_pedro.pedro.time_in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import fellipy.gustavo.joao_pedro.pedro.time_in.fragments.MeusEventosFragment;
import fellipy.gustavo.joao_pedro.pedro.time_in.fragments.PerfilFragment;
import fellipy.gustavo.joao_pedro.pedro.time_in.fragments.TopEventosFragment;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                        MeusEventosFragment meusEventosFragment = MeusEventosFragment.newInstance();
                        setFragment(meusEventosFragment);
                        break;
                    case R.id.perfilOp:
                        PerfilFragment perfilFragment = PerfilFragment.newInstance();
                        setFragment(perfilFragment);
                        break;
                }
                return true;
            }
        });


    }

    void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}