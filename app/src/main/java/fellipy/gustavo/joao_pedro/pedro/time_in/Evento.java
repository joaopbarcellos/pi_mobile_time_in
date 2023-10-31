package fellipy.gustavo.joao_pedro.pedro.time_in;

import android.graphics.Bitmap;

import java.util.Date;

public class Evento {
    public int id;
    public String nome;
    public double preco;
    public Date data;
    public String horario_inicio;
    public String horario_fim;
    public String imagem;



    public Evento(int id, String n, double p, Date d, String hi, String hf, String img){
        this.nome = n;
        this.preco = p;
        this.data = d;
        this.horario_fim = hf;
        this.horario_inicio = hi;
        this.imagem = img;
    }
}
