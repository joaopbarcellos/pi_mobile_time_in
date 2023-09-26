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

        Evento e1 = new Evento("Treino com GabiGordo", 0.0 , new Date(), R.mipmap.gabigordo);
        Evento e2 = new Evento("Role na Lama com Daniel", 0.0, new Date(), R.mipmap.evento);
        Evento e3 = new Evento("Futizn dos Cria com a Tropa do Flamengo", 122.0, new Date(), R.mipmap.sosias);
        Evento e4 = new Evento("Rolê Comigo (Ryan Gosling)", 10.0, new Date(), R.mipmap.ryan);
        Evento e5 = new Evento("Rolê de carro com o Ednaldo Pereira", 1000.0, new Date(), R.mipmap.ednaldo);
        Evento e6 = new Evento("Fut com o Zé Gatinha", 15.2, new Date(), R.mipmap.ZEGATINHA);

        eventosLista = new ArrayList<>();
        eventosLista.add(e1);
        eventosLista.add(e2);
        eventosLista.add(e3);
        eventosLista.add(e4);
        eventosLista.add(e5);
        eventosLista.add(e6);
    }
    public void setNavigationOpSelected(int navigationOpSelected){
        this.navigationOpSelected = navigationOpSelected;
    }
}
