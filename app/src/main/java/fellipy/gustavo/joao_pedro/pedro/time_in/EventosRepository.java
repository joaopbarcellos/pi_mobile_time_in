package fellipy.gustavo.joao_pedro.pedro.time_in;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.EventoActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.HomeActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Config;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.HttpRequest;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Util;

public class EventosRepository {

    Context context;
    public EventosRepository(Context context) {
        this.context = context;
    }

    public boolean cadastro(String nome, String data, String email, String senha,
                            String telefone, String codigo_intuito){
        // Cria uma requisição HTTP a adiona o parâmetros que devem ser enviados ao servidor
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "registrar.php", "POST", "UTF-8");
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

            return success == 1;
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
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +

                "login.php", "POST", "UTF-8");
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

    public boolean addEvent(ArrayList<String> list){
        String login = Config.getLogin(context);
        String password = Config.getPassword(context);

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "criar_evento.php", "POST", "UTF-8");
        // httpRequest.addParam("id", id);
        // [descricao, nome, data, horario_inicio, horario_fim, min_pessoas, preco, foto,
        // max_pessoas, intuito, estado, cidade, bairro, descricao_endereco, numero, cep,
        // idade_publico, classificao]
        httpRequest.addParam("descricao", list.get(0));
        httpRequest.addParam("nome", list.get(1));
        httpRequest.addParam("data", list.get(2));
        httpRequest.addParam("horario_inicio", list.get(3));
        httpRequest.addParam("horario_fim", list.get(4));
        httpRequest.addParam("min_pessoas", list.get(5));
        httpRequest.addParam("preco", list.get(6));
        httpRequest.addFile("foto", new File(list.get(7)));
        httpRequest.addParam("max_pessoas", list.get(8));
        httpRequest.addParam("intuito", list.get(9));
        httpRequest.addParam("estado", list.get(10));
        httpRequest.addParam("cidade", list.get(11));
        httpRequest.addParam("bairro", list.get(12));
        httpRequest.addParam("endereco", list.get(13));
        httpRequest.addParam("numero", list.get(14));
        httpRequest.addParam("cep", list.get(15));
        httpRequest.addParam("idade_publico", list.get(16));
        httpRequest.addParam("classificacao", list.get(17));
        httpRequest.addParam("email", login);

        httpRequest.setBasicAuth(login, password);

        String result = "";
        try {
            // Executa a requisição HTTP. É neste momento que o servidor web é contactado. Ao
            // executar a requisição é aberto um fluxo de dados entre o servidor e a app
            // (InputStream is).
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
            // TA DANDO ERRO DE TIMEOUT PRECISO RESOLVER COM O HOMEM
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("HTTP RESULT", result);
        }
        return false;
    }

    public List<Evento> loadEvents(Integer limit, Integer offSet){
        List<Evento> eventosLista = new ArrayList<>();

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "pegar_eventos.php", "GET", "UTF-8");
        httpRequest.addParam("limit", limit.toString());
        httpRequest.addParam("offset", offSet.toString());

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
                    String id = jEvent.getString("id");
                    String nome = jEvent.getString("nome");
                    String preco = jEvent.getString("preco");
                    String foto = jEvent.getString("foto");
                    String data = jEvent.getString("data");
                    String horario_inicio = jEvent.getString("horario_inicio");
                    String horario_fim = jEvent.getString("horario_fim");

                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    date = parser.parse(data);
                    Evento e = new Evento(Integer.parseInt(id), nome, preco, date, horario_inicio,
                            horario_fim, foto);
                    eventosLista.add(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return eventosLista;

    }
    public List<Evento> loadInitialEvents(Integer limit, Integer offSet) {
        List<Evento> eventosLista = new ArrayList<>();

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "pegar_eventos_iniciais.php", "GET", "UTF-8");
        httpRequest.addParam("limit", limit.toString());
        httpRequest.addParam("offset", offSet.toString());

        String result = "";
        try {
            InputStream is = httpRequest.execute();
            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP EVENTS RESULT", result);

            JSONObject jsonObject = new JSONObject(result);

            int sucess = jsonObject.getInt("sucesso");
            if (sucess == 1) {
                JSONArray jsonArray = jsonObject.getJSONArray("eventos");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jEvent = jsonArray.getJSONObject(i);
                    String id = jEvent.getString("id");
                    String nome = jEvent.getString("nome");
                    String preco = jEvent.getString("preco");
                    String foto = jEvent.getString("foto");
                    String data = jEvent.getString("data");
                    String horario_inicio = jEvent.getString("horario_inicio");
                    String horario_fim = jEvent.getString("horario_fim");

                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    date = parser.parse(data);
                    Evento e = new Evento(Integer.parseInt(id), nome, preco, date, horario_inicio,
                            horario_fim, foto);
                    eventosLista.add(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return eventosLista;
    }

    public List<Evento> loadTopEvents(Integer limit, Integer offSet) {
        List<Evento> eventosLista = new ArrayList<>();

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "pegar_top_eventos.php", "GET", "UTF-8");
        httpRequest.addParam("limit", limit.toString());
        httpRequest.addParam("offset", offSet.toString());

        String result = "";
        try {
            InputStream is = httpRequest.execute();
            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP EVENTS RESULT", result);

            JSONObject jsonObject = new JSONObject(result);

            int sucess = jsonObject.getInt("sucesso");
            if (sucess == 1) {
                JSONArray jsonArray = jsonObject.getJSONArray("eventos");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jEvent = jsonArray.getJSONObject(i);
                    String id = jEvent.getString("id");
                    String nome = jEvent.getString("nome");
                    String foto = jEvent.getString("foto");

                    Evento e = new Evento(Integer.parseInt(id), nome, foto);
                    eventosLista.add(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return eventosLista;
    }
    public Evento loadEventDetail(String id){
        //String login = Config.getLogin(context);
        //String password = Config.getPassword(context);

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "pegar_detalhes_evento.php", "GET", "UTF-8");
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

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada
            // ou não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            if(success == 1) {


                String descricao = jsonObject.getString("descricao");
                String min_pessoas = jsonObject.getString("min_pessoas");
                String max_pessoas = jsonObject.getString("max_pessoas");
                String intuito = jsonObject.getString("intuito");
                String endereco = jsonObject.getString("endereco");
                String idade_publico = jsonObject.getString("idade_publico");
                String classificacao = jsonObject.getString("classificacao");
                String usuario = jsonObject.getString("usuario");
                String nome = jsonObject.getString("nome");
                String preco = jsonObject.getString("preco");
                String data = jsonObject.getString("data");
                String foto = jsonObject.getString("foto");
                String horario_inicio = jsonObject.getString("horario_inicio");
                String horario_fim = jsonObject.getString("horario_fim");

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date();
                try {
                    // Use o método parse para converter a string em um objeto Date
                    d = df.parse(data);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Cria um objeto Product e guarda os detalhes do produto dentro dele
                Evento e = new Evento(Integer.parseInt(id), nome, preco, d, horario_inicio,
                        horario_fim, foto, descricao, Integer.parseInt(max_pessoas),
                        Integer.parseInt(min_pessoas), intuito, usuario, idade_publico, endereco,
                        classificacao);

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

    public int loadRemaningSlots(String id){
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "pegar_vagas_restantes.php", "POST", "UTF-8");
        httpRequest.addParam("id", id);

        String result = "";
        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada
            // ou não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            if(success == 1) {
                return jsonObject.getInt("vagas");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Usuario loadUserDetail(){
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "pegar_detalhes_usuario.php", "GET", "UTF-8");
        httpRequest.addParam("email", Config.getLogin(context));
        String result = "";
        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou
            // não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            if(success == 1) {

                String nome = jsonObject.getString("nome");
                String data = jsonObject.getString("data_nascimento");
                String foto = jsonObject.getString("foto");
                String id = jsonObject.getString("id");
                String telefone = jsonObject.getString("telefone");
                String intuito = jsonObject.getString("intuito");

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date();
                try {
                    // Use o método parse para converter a string em um objeto Date
                    d = df.parse(data);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Usuario u = new Usuario(Integer.parseInt(id), nome, Config.getLogin(context),
                        telefone, intuito, foto, d);
                return u;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserDetail(String id, String nome, String email, String data,
                                 String telefone, String imgLocation, String codigo_intuito){
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "atualizar_dados_usuario.php", "POST", "UTF-8");
        httpRequest.addParam("id", id);
        httpRequest.addParam("nome", nome);
        httpRequest.addParam("email", email);
        httpRequest.addParam("data", data);
        httpRequest.addParam("intuito", codigo_intuito);
        httpRequest.addParam("telefone", telefone);
        httpRequest.addFile("img", new File(imgLocation));
        String result = "";

        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou
            // não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            return success == 1;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Evento> loadUserSubscribedEvents(Integer limit, Integer offSet){
        List<Evento> eventosLista = new ArrayList<>();

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "pegar_eventos_inscritos.php", "GET", "UTF-8");
        httpRequest.addParam("email", Config.getLogin(context));
        httpRequest.addParam("limit", limit.toString());
        httpRequest.addParam("offset", offSet.toString());

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
                    String id = jEvent.getString("id");
                    String nome = jEvent.getString("nome");
                    String preco = jEvent.getString("preco");
                    String foto = jEvent.getString("foto");
                    String data = jEvent.getString("data");
                    String horario_inicio = jEvent.getString("horario_inicio");
                    String horario_fim = jEvent.getString("horario_fim");

                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    date = parser.parse(data);
                    Evento e = new Evento(Integer.parseInt(id), nome, preco, date, horario_inicio,
                            horario_fim, foto);
                    eventosLista.add(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return eventosLista;
    }
    public List<Evento> loadUserEventsCreated(Integer limit, Integer offSet){
        List<Evento> eventosLista = new ArrayList<>();

        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "pegar_eventos_criados.php", "GET", "UTF-8");
        httpRequest.addParam("email", Config.getLogin(context));
        httpRequest.addParam("limit", limit.toString());
        httpRequest.addParam("offset", offSet.toString());

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
                    String id = jEvent.getString("id");
                    String nome = jEvent.getString("nome");
                    String preco = jEvent.getString("preco");
                    String foto = jEvent.getString("foto");
                    String data = jEvent.getString("data");
                    String horario_inicio = jEvent.getString("horario_inicio");
                    String horario_fim = jEvent.getString("horario_fim");

                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    date = parser.parse(data);
                    Evento e = new Evento(Integer.parseInt(id), nome, preco, date, horario_inicio,
                            horario_fim, foto);
                    eventosLista.add(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return eventosLista;
    }

    public boolean registerInEvent(String idEvento){
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "inscrever_evento.php", "POST", "UTF-8");
        httpRequest.addParam("email", Config.getLogin(context));
        httpRequest.addParam("id_evento", idEvento);
        String result = "";

        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou
            // não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            return success == 1;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean unRegisterInEvent(String idEvento){
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "desinscrever_evento.php", "GET", "UTF-8");
        httpRequest.addParam("email", Config.getLogin(context));
        httpRequest.addParam("id_evento", idEvento);
        String result = "";

        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou
            // não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            return success == 1;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String relationUserEvent(String idEvento){
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "verificar_usuario_evento.php", "GET", "UTF-8");
        httpRequest.addParam("email", Config.getLogin(context));
        httpRequest.addParam("id_evento", idEvento);
        String result = "";
        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou
            // não.
            String relacionamento = jsonObject.getString("usuario");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            return relacionamento;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public boolean updateUserPass(String senha_antiga, String senha_nova){
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "atualizar_senha.php", "POST", "UTF-8");
        httpRequest.addParam("senha_antiga", senha_antiga);
        httpRequest.addParam("senha_nova", senha_nova);
        httpRequest.addParam("email", Config.getLogin(context));
        String result = "";

        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou
            // não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            return success == 1;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEventsDetails(String id, String nome, String cap_max, String cap_min,
                                       String descricao){
        HttpRequest httpRequest = new HttpRequest(Config.EVENTS_APP_URL +
                "atualizar_dados_evento.php", "POST", "UTF-8");
        httpRequest.addParam("id", id);
        httpRequest.addParam("nome", nome);
        httpRequest.addParam("capacidade_max", cap_max);
        httpRequest.addParam("capacidade_min", cap_min);
        httpRequest.addParam("descricao", descricao);
        String result = "";

        try{
            InputStream is = httpRequest.execute();

            result = Util.inputStream2String(is, "UTF-8");
            httpRequest.finish();

            Log.i("HTTP DETAILS RESULT", result);

            // A classe JSONObject recebe como parâmetro do construtor uma String no formato JSON e
            // monta internamente uma estrutura de dados similar ao dicionário em python.
            JSONObject jsonObject = new JSONObject(result);

            // obtem o valor da chave sucesso para verificar se a ação ocorreu da forma esperada ou
            // não.
            int success = jsonObject.getInt("sucesso");

            // Se sucesso igual a 1, os detalhes do produto são obtidos da String JSON e um objeto
            // do tipo Product é criado para guardar esses dados
            return success == 1;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
