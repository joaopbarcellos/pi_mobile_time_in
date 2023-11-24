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

    public boolean updateUserDetails(String id, String nome, String email, String data,
                                     String telefone){

        final Boolean[] verifica = {false};
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EventosRepository eventosRepository = new EventosRepository(getApplication());

                verifica[0] = eventosRepository.updateUserDetail(id, nome, email, data, telefone);
            }
        });
        return verifica[0];
    }
}
