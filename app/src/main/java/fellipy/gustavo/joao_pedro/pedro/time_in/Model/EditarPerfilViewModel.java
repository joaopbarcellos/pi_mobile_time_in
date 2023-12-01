package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;

public class EditarPerfilViewModel extends AndroidViewModel {
    String currentPhotoPath = "";
    public EditarPerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }
    public String getCurrentPhotoPath() {
        return currentPhotoPath;
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

    public LiveData<Boolean> updateUserDetails(String id, String nome, String email, String data,
                                     String telefone, String imgLocation, String codigo_intuito){

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EventosRepository eventosRepository = new EventosRepository(getApplication());

                boolean b = eventosRepository.updateUserDetail(id, nome, email, data, telefone,
                        imgLocation, codigo_intuito);

                result.postValue(b);
            }
        });
        return result;
    }
}
