package fellipy.gustavo.joao_pedro.pedro.time_in.pagingsource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import fellipy.gustavo.joao_pedro.pedro.time_in.Esporte;
import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;

public class SportsPagingSource extends ListenableFuturePagingSource<Integer, Esporte> {
    @Nullable

    Integer initialLoadSize = 0;
    EventosRepository eventosRepository;
    String id_classificacao;

    public SportsPagingSource(EventosRepository eventosRepository){
        this.eventosRepository = eventosRepository;
    }
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Esporte> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, Esporte>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        Integer nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
            initialLoadSize = loadParams.getLoadSize();
        }

        Integer offSet = 0;
        if(nextPageNumber == 2) {
            offSet = initialLoadSize;
        }
        else {
            offSet = ((nextPageNumber - 1) * loadParams.getLoadSize()) + (initialLoadSize - loadParams.getLoadSize());
        }

        // cria uma nova linha de execução
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        Integer finalOffSet = offSet;
        Integer finalNextPageNumber = nextPageNumber;

        // executa a nova linha de execução.
        ListenableFuture<LoadResult<Integer, Esporte>> lf = service.submit(new Callable<LoadResult<Integer, Esporte>>() {
            /**
             * Tudo que estiver dentro dessa função será executado na nova linha de execução.
             */
            @Override
            public LoadResult<Integer, Esporte> call() {
                List<Esporte> esportes = new ArrayList<>();
                esportes.add(new Esporte(20, "https://i.imgur.com/TYIPraO.png"));
                esportes.add(new Esporte(17, "https://i.imgur.com/vMuAhDM.png"));
                esportes.add(new Esporte(38, "https://i.imgur.com/NhPrtov.png"));
                esportes.add(new Esporte(35, "https://i.imgur.com/6NuOUMb.png"));
                esportes.add(new Esporte(30, "https://i.imgur.com/FairGYm.png"));
                esportes.add(new Esporte(16, "https://i.imgur.com/MYqBQsg.png"));
                esportes.add(new Esporte(26, "https://i.imgur.com/QOL3og9.png"));
                esportes.add(new Esporte(7, "https://i.imgur.com/dQA1VJL.png"));
                esportes.add(new Esporte(6, "https://i.imgur.com/QLvQ8ER.png"));
                esportes.add(new Esporte(25, "https://i.imgur.com/mEgigru.png"));
                esportes.add(new Esporte(8, "https://i.imgur.com/rxUImLx.png"));
                esportes.add(new Esporte(34, "https://i.imgur.com/bMytAsE.png"));
                Integer nextKey = null;
                if(esportes.size() >= loadParams.getLoadSize()) {
                    nextKey = finalNextPageNumber + 1;
                }
                // monta uma página do padrão da biblioteca Paging 3.
                return new LoadResult.Page<Integer, Esporte>(esportes,
                        null,
                        nextKey);
            }
        });

        return lf;
    }
}