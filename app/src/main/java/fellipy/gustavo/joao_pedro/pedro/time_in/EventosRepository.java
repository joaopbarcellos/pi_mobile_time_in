package fellipy.gustavo.joao_pedro.pedro.time_in;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventosRepository {

    Context context;
    public EventosRepository(Context context) {
        this.context = context;
    }

    public boolean register(){
        return true;
    }

    public boolean login(){
        return true;
    }

    public boolean addEvent(String name, String preco, String data, String img){
        return true;
    }

    public List<Evento> loadEvents(Integer limit, Integer offSet, String filtro){
        List<Evento> eventosLista = new ArrayList<>();
        Evento e1 = new Evento(1, "Role na Lama com Daniel", 0.0, new Date(), R.mipmap.evento);
        Evento e3 = new  (2, "Futizn dos Cria com a Tropa do Flamengo", 122.0, new Date(), R.mipmap.sosias);
        Evento e4 = new Evento(3, "Rolê Comigo (Ryan Gosling)", 10.0, new Date(), R.mipmap.ryan);
        Evento e5 = new Evento(4, "Rolê de carro com o Ednaldo Pereira", 1000.0, new Date(), R.mipmap.ednaldo);
        Evento e6 = new Evento(5, "Fut com o Zé Gatinha", 15.2, new Date(), R.mipmap.zegatinha);

        eventosLista.add(e1);
        eventosLista.add(e3);
        eventosLista.add(e4);
        eventosLista.add(e5);
        eventosLista.add(e6);
        return eventosLista;
    }

    public Evento loadEventDetail(String id){
        return loadEvents(1, 1, "1").get(Integer.parseInt(id) - 1);
    }
}
