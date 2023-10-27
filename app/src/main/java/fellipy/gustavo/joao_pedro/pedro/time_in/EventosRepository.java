package fellipy.gustavo.joao_pedro.pedro.time_in;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public boolean cadastro(String nome, String data, String email, String senha, String telefone, String codigo_intuito){
        // Cria uma requisição HTTP a adiona o parâmetros que devem ser enviados ao servidor
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL + "registrar.php", "POST", "UTF-8");
        httpRequest.addParam("etNome", nome);
        httpRequest.addParam("etData", data);
        httpRequest.addParam("etEmailCadastro", email);
        httpRequest.addParam("etSenhaCadastro", senha);
        httpRequest.addParam("intuito", codigo_intuito);
        httpRequest.addParam("etTelefone", telefone);

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
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL + "login.php", "POST", "UTF-8");
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
        //String login = Config.getLogin(context);
        //String password = Config.getPassword(context);

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL + "criar_evento.php", "POST", "UTF-8");
        // httpRequest.addParam("id", id);
        httpRequest.addParam("nome", nome);
        httpRequest.addParam("preco", preco);
        httpRequest.addParam("dataHorario", dataHorario);
        httpRequest.addParam("imagem", imagem);

        //httpRequest.setBasicAuth(login, password);

        String result = "";
        try {
            // Executa a requisição HTTP. É neste momento que o servidor web é contactado. Ao executar
            // a requisição é aberto um fluxo de dados entre o servidor e a app (InputStream is).
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");

            // Fecha a conexão com o servidor web.
            httpRequest.finish();

            Log.i("HTTP ADD EVENT RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
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

    public List<Evento> loadEvents(Integer limit, Integer offSet, String filtro){
        List<Evento> eventosLista = new ArrayList<>();

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +"pegar_eventos.php", "GET", "UTF-8");
        httpRequest.addParam("limit", "10");
        httpRequest.addParam("offset", "1");
        // httpRequest.addParam("filtro", filtro.toString());

        String result = "";
        try{
            InputStream is = httpRequest.execute();
            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP EVENTS RESULT", result);

            JSONObject jsonObject = new JSONObject(result);

            int sucess = jsonObject.getInt("sucesso");
            if(sucess == 1){
                JSONArray jsonArray = jsonObject.getJSONArray("eventos");

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jEvent = jsonArray.getJSONObject(i);

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

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
        //String login = Config.getLogin(context);
        //String password = Config.getPassword(context);

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL + "pegar_detalhes_evento.php", "GET", "UTF-8");
        httpRequest.addParam("id", id);

        //httpRequest.setBasicAuth(login, password);

        String result = "";
        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            if(success == 1) {

                String nome = jsonObject.getString("nome");
                String preco = jsonObject.getString("preco");
                String dataHorario = jsonObject.getString("dataHorario");
                String imagem = jsonObject.getString("imagem");

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    // Use o método parse para converter a string em um objeto Date
                    Date data = df.parse(dataHorario);

                    // Agora você tem a data em um objeto Date
                    System.out.println("Data: " + data);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date d = new Date(dataHorario);

                // Cria um objeto Product e guarda os detalhes do produto dentro dele
                Evento e = new Evento(Integer.parseInt(id), nome, Double.parseDouble(preco), d, Integer.parseInt(imagem));


                return e;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        // return loadEvents(1, 1, "1").get(Integer.parseInt(id) - 1);
    }
}
