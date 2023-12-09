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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.Esporte;
import fellipy.gustavo.joao_pedro.pedro.time_in.pagingsource.CreatedEventsPagingSource;
import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;
import fellipy.gustavo.joao_pedro.pedro.time_in.pagingsource.EventsPagingSource;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.pagingsource.SeachedEventsPagingSource;
import fellipy.gustavo.joao_pedro.pedro.time_in.pagingsource.SportsEventsPagingSource;
import fellipy.gustavo.joao_pedro.pedro.time_in.pagingsource.SportsPagingSource;
import fellipy.gustavo.joao_pedro.pedro.time_in.pagingsource.SubscribedEventsPagingSource;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;
import fellipy.gustavo.joao_pedro.pedro.time_in.pagingsource.TopEventsPagingSource;
import kotlinx.coroutines.CoroutineScope;

public class HomeViewModel extends AndroidViewModel {

    int navigationOpSelected = R.id.homeOp;
    LiveData<PagingData<Evento>> eventsLd, eventsSubsLd, eventsCreLd, eventsTopLd, eventsSportsLd;
    LiveData<PagingData<Esporte>> sportsLd;

    EventosRepository eventosRepository = new EventosRepository(getApplication());
    public HomeViewModel(@NonNull Application application){
        super(application);

        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        Pager<Integer, Evento> topPager = new Pager(new PagingConfig(10), () -> new
                TopEventsPagingSource(eventosRepository));

        Pager<Integer, Evento> pager = new Pager(new PagingConfig(10), () -> new
                EventsPagingSource(eventosRepository));
        Pager<Integer, Evento> subsPager = new Pager(new PagingConfig(10), () -> new
                SubscribedEventsPagingSource(eventosRepository));
        Pager<Integer, Evento> creaPager = new Pager(new PagingConfig(10), () -> new
                CreatedEventsPagingSource(eventosRepository));

        Pager<Integer, Esporte> esportePager = new Pager(new PagingConfig(10), () -> new
                SportsPagingSource(eventosRepository));

        eventsSubsLd = PagingLiveData.cachedIn(PagingLiveData.getLiveData(subsPager),
                viewModelScope);
        eventsLd = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
        eventsCreLd = PagingLiveData.cachedIn(PagingLiveData.getLiveData(creaPager),
                viewModelScope);
        eventsTopLd = PagingLiveData.cachedIn(PagingLiveData.getLiveData(topPager),
                viewModelScope);
        sportsLd = PagingLiveData.cachedIn(PagingLiveData.getLiveData(esportePager),
                viewModelScope);
    }

    public void setNavigationOpSelected(int navigationOpSelected){
        this.navigationOpSelected = navigationOpSelected;
    }

    public LiveData<PagingData<Evento>> getEventsLd(){return eventsLd;}
    public LiveData<PagingData<Evento>> getEventsSubsLd(){return eventsSubsLd;}

    public LiveData<PagingData<Evento>> getEventsCreLd() {
        return eventsCreLd;
    }

    public LiveData<PagingData<Evento>> getTopEventsLd(){return eventsTopLd;}

    public LiveData<PagingData<Esporte>> getSportsLd(){
        return sportsLd;
    }

    public LiveData<PagingData<Evento>> getSportsEventsLd(String idEsporte){
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Evento> sportEventPager = new Pager(new PagingConfig(10), () -> new
                SportsEventsPagingSource(eventosRepository, idEsporte));

        eventsSportsLd = PagingLiveData.cachedIn(PagingLiveData.getLiveData(sportEventPager),
                viewModelScope);
        return eventsSportsLd;
    }
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

    public LiveData<Boolean> updateUserPass(String senha_antiga, String senha_nova){
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EventosRepository eventosRepository = new EventosRepository(getApplication());

                boolean b = eventosRepository.updateUserPass(senha_antiga, senha_nova);

                result.postValue(b);
            }
        });
        return result;
    }

    public LiveData<PagingData<Evento>> loadSearchedEvents(String pesquisa){
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Evento> searPager = new Pager(new PagingConfig(10), () -> new
                SeachedEventsPagingSource(eventosRepository, pesquisa));
        LiveData<PagingData<Evento>> eventsSearLd = PagingLiveData.cachedIn
                (PagingLiveData.getLiveData(searPager),
                viewModelScope);
        return eventsSearLd;
    }


}
