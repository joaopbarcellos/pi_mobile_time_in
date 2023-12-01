package fellipy.gustavo.joao_pedro.pedro.time_in;

import java.util.Date;

public class Usuario {
    public String nome, email, foto, telefone, intuito;
    public int id;
    public Date dataNasc;

    public Usuario(int id, String nome, String email, String telefone, String intuito, String foto, Date dataNasc){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.foto = foto;
        this.dataNasc = dataNasc;
        this.telefone = telefone;
        this.intuito = intuito;
    }
}
