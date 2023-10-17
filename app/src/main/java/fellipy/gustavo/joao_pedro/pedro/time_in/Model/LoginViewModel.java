package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;

public class LoginViewModel extends AndroidViewModel {

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> login(String email, String senha) {


        MutableLiveData<Boolean> result = new MutableLiveData<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {

            @Override
            public void run() {

                EventosRepository eventosRepository = new EventosRepository(getApplication());

                boolean b = eventosRepository.login(email, senha);

                result.postValue(b);
            }
        });

        return result;
    }
}


