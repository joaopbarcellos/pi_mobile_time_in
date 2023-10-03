package fellipy.gustavo.joao_pedro.pedro.time_in;

import android.graphics.Bitmap;

import java.util.Date;

public class Evento {
    public int id;
    public String nome;
    public double preco;
    public Date dataHorario;
    public int imagem;



    public Evento(int id, String n, double p, Date d, int img){
        this.nome = n;
        this.preco = p;
        this.dataHorario = d;
        this.imagem = img;
    }
}
