package fellipy.gustavo.joao_pedro.pedro.time_in;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fellipy.gustavo.joao_pedro.pedro.time_in.util.Config;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.HttpRequest;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Util;

public class EventosRepository {

    Context context;
    public EventosRepository(Context context) {
        this.context = context;
    }

    public boolean cadastro(String nome, String data, String email, String senha, String codigo_intuito){
        // Cria uma requisição HTTP a adiona o parâmetros que devem ser enviados ao servidor
        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "registrar.php", "POST", "UTF-8");
        httpRequest.addParam("novo_login", nome);
        httpRequest.addParam("nova_data", data);
        httpRequest.addParam("novo_email", email);
        httpRequest.addParam("nova_senha", senha);
        httpRequest.addParam("novo_intuito", codigo_intuito);

        String result = "";
        try{
            InputStream is = httpRequest.execute();
            result = Util.inputStream2String(is, "UTF-8");
            Log.i("HTTP REGISTER RESULT", result);
            httpRequest.finish();

            JSONObject jsonObject = new JSONObject(result);
            int success = jsonObject.getInt("sucesso");

            if(success == 1) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }
        return false;


    }

    public boolean login(String email, String senha){
        // Cria uma requisição HTTP a adiona o parâmetros que devem ser enviados ao servidor
        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "login.php", "POST", "UTF-8");
        httpRequest.setBasicAuth(email, senha);

        String result = "";
        try {
            InputStream is = httpRequest.execute();
            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP LOGIN RESULT", result);
            JSONObject jsonObject = new JSONObject(result);

            int success = jsonObject.getInt("sucesso");
            if(success == 1) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }
        return false;
    }

    public boolean addEvent(String id, String nome, String preco, String dataHorario, String imagem){
        String login = Config.getLogin(context);
        String password = Config.getPassword(context);
        return true;
    }

    public List<Evento> loadEvents(Integer limit, Integer offSet, String filtro){
        List<Evento> eventosLista = new ArrayList<>();
        Evento e1 = new Evento(1, "Role na Lama com Daniel", 0.0, new Date(), R.mipmap.evento);
        Evento e3 = new Evento(2, "Futizn dos Cria com a Tropa do Flamengo", 122.0, new Date(), R.mipmap.sosias);
        Evento e4 = new Evento(3, "Rolê Comigo (Ryan Gosling)", 10.0, new Date(), R.mipmap.ryan);
        Evento e5 = new Evento(4, "Rolê de carro com o Ednaldo Pereira", 1000.0, new Date(), R.mipmap.ednaldo);
        Evento e6 = new Evento(5, "Fut com o Zé Gatinha", 15.2, new Date(), R.mipmap.zegatinha);

        eventosLista.add(e1);
        eventosLista.add(e3);
        eventosLista.add(e4);
        eventosLista.add(e5);
        eventosLista.add(e6);
        return eventosLista;
    }

    public Evento loadEventDetail(String id){
        return loadEvents(1, 1, "1").get(Integer.parseInt(id) - 1);
    }
}
