package fellipy.gustavo.joao_pedro.pedro.time_in;

import android.graphics.Bitmap;

import java.util.Date;

public class Evento {
    public String nome, horario_inicio, horario_fim, imagem, descricao, intuito, usuario,
            idade_publico, endereco, classificao, preco, email_usuario_criador;
    public int id, max_pessoas, min_pessoas;
    public Date data;

    public Evento(int id, String n, String p, Date d, String hi, String hf, String img){
        // Construtor para resumo dos eventos
        this.id = id;
        this.nome = n;
        this.preco = p;
        this.data = d;
        this.horario_fim = hf;
        this.horario_inicio = hi;
        this.imagem = img;
    }

    public Evento(int id, String n, String p, Date d, String hi, String hf, String img, String descricao,
                  int max_pessoas, int min_pessoas, String intuito, String usuario,
                  String idade_publico, String endereco, String classificacao, String email){
        // Construtor para detalhes dos eventos
        this.id = id;
        this.nome = n;
        this.preco = p;
        this.data = d;
        this.horario_fim = hf;
        this.horario_inicio = hi;
        this.imagem = img;
        this.descricao = descricao;
        this.intuito = intuito;
        this.usuario = usuario;
        this.idade_publico = idade_publico;
        this.endereco = endereco;
        this.classificao = classificacao;
        this.max_pessoas = max_pessoas;
        this.min_pessoas = min_pessoas;
        this.email_usuario_criador = email;
    }
}
