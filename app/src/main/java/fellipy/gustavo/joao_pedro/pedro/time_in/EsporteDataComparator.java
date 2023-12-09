package fellipy.gustavo.joao_pedro.pedro.time_in;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class EsporteDataComparator extends DiffUtil.ItemCallback<Esporte> {

    @Override
    public boolean areItemsTheSame(@NonNull Esporte oldItem, @NonNull Esporte newItem) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Esporte oldItem, @NonNull Esporte newItem) {
        return false;
    }
}