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

    public boolean addEvento(String name, String preco, String data, String img){
        return true;
    }

    public List<Evento> loadEventos(Integer limit, Integer offSet){
        List<Evento> eventosList = new ArrayList<>();

    }
    
    Evento loadEventoDetail(String id){

    }
}
