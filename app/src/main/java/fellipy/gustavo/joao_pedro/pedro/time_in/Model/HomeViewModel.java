package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.Date;

import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;

public class HomeViewModel extends AndroidViewModel {

    private ArrayList<Evento> eventosLista;
    private ArrayList<Evento> eventosCarrossel;
    int navigationOpSelected = R.id.homeOp;


    public HomeViewModel(@NonNull Application application){
        super(application);

        Evento e1 = new Evento("Treino com GabiGordo", 0.0 , new Date(), R.mipmap.evento );

        eventosLista = new ArrayList<>();
        eventosLista.add(e1);
    }
    public void setNavigationOpSelected(int navigationOpSelected){
        this.navigationOpSelected = navigationOpSelected;
    }
}
