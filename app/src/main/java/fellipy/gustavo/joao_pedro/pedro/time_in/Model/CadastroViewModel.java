package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import android.app.Application;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CadastroViewModel extends AndroidViewModel {

    public CadastroViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> cadastro(String nome, Date data, String email, String senha, int codigo_intuito) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {

            @Override
            public void run() {

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String s = df.format(data);

                result.setValue(true);
            }
        });

        return result;
    }
}
