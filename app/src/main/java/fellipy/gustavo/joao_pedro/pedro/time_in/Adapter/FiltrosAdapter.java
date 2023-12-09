package fellipy.gustavo.joao_pedro.pedro.time_in.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import fellipy.gustavo.joao_pedro.pedro.time_in.Esporte;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.MyViewHolder;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.fragments.TopEventosFragment;

public class FiltrosAdapter extends PagingDataAdapter<Esporte, MyViewHolder> {

    TopEventosFragment topEventosFragment;

    public FiltrosAdapter(@NonNull DiffUtil.ItemCallback<Esporte> diffCallback, TopEventosFragment topEventosFragment){
        super(diffCallback);
        this.topEventosFragment = topEventosFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_carrossel_categoria_evento, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Esporte esporte = getItem(position);

        ImageView imgImagemEvento = holder.itemView.findViewById(R.id.imvImagemItem);
        ImageCache.loadImageUrlToImageView(holder.itemView.getContext(), esporte.img,
                imgImagemEvento, 100, 100);

        ConstraintLayout constraintLayout = holder.itemView.findViewById(R.id.clCarrosselEsporte);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topEventosFragment.setIdEsporte(Integer.toString(esporte.id));
            }
        });
    }
}
