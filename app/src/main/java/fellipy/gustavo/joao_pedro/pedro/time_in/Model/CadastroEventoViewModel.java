package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;

public class CadastroEventoViewModel extends AndroidViewModel {

    String currentPhotoPath = "";
    public CadastroEventoViewModel(@NonNull Application application) {
        super(application);
    }
    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }
    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public LiveData<Boolean> addEvent(ArrayList<String> list){
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EventosRepository eventosRepository = new EventosRepository(getApplication());
                boolean b = eventosRepository.addEvent(list);
                result.postValue(b);
            }
        });
        return result;
    }
}
