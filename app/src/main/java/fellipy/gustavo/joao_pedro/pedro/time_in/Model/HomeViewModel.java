package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventsPagingSource;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;
import kotlinx.coroutines.CoroutineScope;

public class HomeViewModel extends AndroidViewModel {

    private ArrayList<Evento> eventosLista;
    private ArrayList<Evento> eventosCarrossel;
    int navigationOpSelected = R.id.homeOp;
    LiveData<PagingData<Evento>> eventsLd, eventsInscLd, eventsCreLd;
    // Lds pra cada um dos ngc dos MeusEventos

    public HomeViewModel(@NonNull Application application){
        super(application);

        EventosRepository eventosRepository = new EventosRepository(getApplication());
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Evento> pager = new Pager(new PagingConfig(10), () -> new
                EventsPagingSource(eventosRepository));
        eventsLd = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    public void setNavigationOpSelected(int navigationOpSelected){
        this.navigationOpSelected = navigationOpSelected;
    }
    public LiveData<Evento> getEventDetailsLD(String eid){
        MutableLiveData<Evento> eventDetailLD = new MutableLiveData<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EventosRepository eventosRepository = new EventosRepository(getApplication());
                Evento e = eventosRepository.loadEventDetail(eid);

                eventDetailLD.postValue(e);
            }
        });
        return eventDetailLD;
    }

    public LiveData<PagingData<Evento>> getEventsLd(){return eventsLd;}

    public LiveData<Usuario> loadUserDetailsLv(){

        MutableLiveData<Usuario> userDetailLD = new MutableLiveData<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EventosRepository eventosRepository = new EventosRepository(getApplication());
                Usuario e = eventosRepository.loadUserDetail();

                userDetailLD.postValue(e);
            }
        });
        return userDetailLD;
    }

}
