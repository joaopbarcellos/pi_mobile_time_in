package fellipy.gustavo.joao_pedro.pedro.time_in.Adapter;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.HomeActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.MyViewHolder;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;

public class CarrosselAdapter extends PagingDataAdapter<Evento, MyViewHolder> {
    HomeActivity homeActivity;

    public CarrosselAdapter(@NonNull DiffUtil.ItemCallback<Evento> diffCallback, HomeActivity homeActivity){
        super(diffCallback);
        this.homeActivity = homeActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_carrossel_top_eventos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Evento evento = getItem(position);

        // Titulo do Evento
        TextView tvTituloItemEvento = holder.itemView.findViewById(R.id.tvEventoCarrossel);
        tvTituloItemEvento.setText(evento.nome);

        ImageView imgImagemEvento = holder.itemView.findViewById(R.id.imvEventoCarrossel);
        ImageCache.loadImageUrlToImageView(holder.itemView.getContext(), evento.imagem, imgImagemEvento, 100, 100);

        ConstraintLayout constraintLayout = holder.itemView.findViewById(R.id.clCarrosselEvento);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.startEventDetailsActivity(evento.id);
            }
        });
    }
}
