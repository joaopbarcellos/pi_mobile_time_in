package fellipy.gustavo.joao_pedro.pedro.time_in.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fellipy.gustavo.joao_pedro.pedro.time_in.Adapter.ListAdapter;
import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageDataComparator;
import fellipy.gustavo.joao_pedro.pedro.time_in.MainActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.HomeViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopEventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopEventosFragment extends Fragment {



    private HomeViewModel hViewModel;


    public TopEventosFragment() {
        // Required empty public constructor
    }


    public static TopEventosFragment newInstance(){
        return new TopEventosFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topeventos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        ListAdapter listAdapter = new ListAdapter(new ImageDataComparator());
        LiveData<PagingData<Evento>> liveData = hViewModel.getEventsLd();

        liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
            @Override
            public void onChanged(PagingData<Evento> eventoPagingData) {
                listAdapter.submitData(getViewLifecycleOwner().getLifecycle(), eventoPagingData);
            }
        });
        RecyclerView rvEvento = (RecyclerView) view.findViewById(R.id.rvEventos);
        rvEvento.setAdapter(listAdapter);
        rvEvento.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}