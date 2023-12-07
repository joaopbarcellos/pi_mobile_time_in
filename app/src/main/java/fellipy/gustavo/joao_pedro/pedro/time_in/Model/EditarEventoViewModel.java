package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;

public class EditarEventoViewModel extends AndroidViewModel {

    public EditarEventoViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Boolean> updateEventsDetail(String id, String nome, String cap_max,
                                                String cap_min, String descricao){
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {

            @Override
            public void run() {

                EventosRepository eventosRepository = new EventosRepository(getApplication());


                boolean b = eventosRepository.updateEventsDetails(id, nome, cap_max, cap_min,
                        descricao);

                result.postValue(b);
            }
        });

        return result;
    }

    public LiveData<Evento> getEventDetail(String id) {
        MutableLiveData<Evento> eventDetailLD = new MutableLiveData<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EventosRepository eventosRepository = new EventosRepository(getApplication());
                Evento e = eventosRepository.loadEventDetail(id);

                eventDetailLD.postValue(e);
            }
        });
        return eventDetailLD;
    }
}
