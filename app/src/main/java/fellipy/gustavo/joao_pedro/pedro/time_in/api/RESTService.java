package fellipy.gustavo.joao_pedro.pedro.time_in.api;


import fellipy.gustavo.joao_pedro.pedro.time_in.CEP;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface RESTService {

    //consultar CEP no webservice do ViaCEP
    @GET("{cep}/json/")
    Call<CEP> consultarCEP(@Path("cep") String cep);
}
