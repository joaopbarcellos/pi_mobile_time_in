package fellipy.gustavo.joao_pedro.pedro.time_in;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ImageDataComparator extends DiffUtil.ItemCallback<Evento> {

    @Override
    public boolean areItemsTheSame(@NonNull Evento oldItem, @NonNull Evento newItem) {
        return oldItem.id == newItem.id;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Evento oldItem, @NonNull Evento newItem) {
        return oldItem.id == newItem.id;
    }
}
