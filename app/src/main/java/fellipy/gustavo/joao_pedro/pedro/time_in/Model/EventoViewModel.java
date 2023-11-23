package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;

public class EventoViewModel extends AndroidViewModel {

    public EventoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Evento> getEventDetail(int id) {
        MutableLiveData<Evento> eventDetailLD = new MutableLiveData<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EventosRepository eventosRepository = new EventosRepository(getApplication());
                Evento e = eventosRepository.loadEventDetail(Integer.toString(id));

                eventDetailLD.postValue(e);
            }
        });
        return eventDetailLD;
    }
}
