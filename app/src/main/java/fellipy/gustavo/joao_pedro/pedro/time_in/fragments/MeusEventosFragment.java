package fellipy.gustavo.joao_pedro.pedro.time_in.fragments;

import android.content.res.ColorStateList;
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
import android.widget.ToggleButton;

import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.HomeActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Adapter.ListAdapter;
import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventoDataComparator;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.HomeViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeusEventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeusEventosFragment extends Fragment {

    private HomeViewModel hViewModel;
    public MeusEventosFragment() {
        // Required empty public constructor
    }

    public static MeusEventosFragment newInstance(){
        return new MeusEventosFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meus_eventos, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListAdapter listAdapterSubsEvents = new ListAdapter(new EventoDataComparator(),
                (HomeActivity) getActivity());
        ListAdapter listAdapterCreEvents = new ListAdapter(new EventoDataComparator(),
                (HomeActivity) getActivity());
        HomeViewModel homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        LiveData<PagingData<Evento>> subsEventsLiveData = homeViewModel.getEventsSubsLd();
        LiveData<PagingData<Evento>> creatEventsLiveData = homeViewModel.getEventsCreLd();

        ToggleButton tbtInscritos = (ToggleButton) view.findViewById(R.id.btnEventosInscritos);
        ToggleButton tbtCriados = (ToggleButton) view.findViewById(R.id.btnEventosCriados);

        tbtInscritos.setChecked(true);
        subsEventsLiveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
            @Override
            public void onChanged(PagingData<Evento> eventoPagingData) {
                listAdapterSubsEvents.submitData(getViewLifecycleOwner().getLifecycle(),
                        eventoPagingData);
            }
        });

        creatEventsLiveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
            @Override
            public void onChanged(PagingData<Evento> eventoPagingData) {
                listAdapterCreEvents.submitData(getViewLifecycleOwner().getLifecycle(),
                        eventoPagingData);
            }
        });
        RecyclerView rvMyEvents = (RecyclerView) view.findViewById(R.id.rvMeusEventos);

        if(tbtInscritos.isChecked()){
            rvMyEvents.setAdapter(listAdapterSubsEvents);
            tbtCriados.setBackgroundTintList(ColorStateList.valueOf(getResources()
                    .getColor(R.color.azul)));
            tbtInscritos.setBackgroundTintList(ColorStateList.valueOf(getResources()
                    .getColor(R.color.azul_escuro)));
        }

        tbtInscritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tbtCriados.setChecked(false);
                tbtCriados.setBackgroundTintList(ColorStateList.valueOf(getResources()
                        .getColor(R.color.azul)));
                tbtInscritos.setBackgroundTintList(ColorStateList.valueOf(getResources()
                        .getColor(R.color.azul_escuro)));
                rvMyEvents.setAdapter(listAdapterSubsEvents);
            }
        });

        tbtCriados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tbtInscritos.setChecked(false);
                tbtInscritos.setBackgroundTintList(ColorStateList.valueOf(getResources()
                        .getColor(R.color.azul)));
                tbtCriados.setBackgroundTintList(ColorStateList.valueOf(getResources()
                        .getColor(R.color.azul_escuro)));
                rvMyEvents.setAdapter(listAdapterCreEvents);
            }
        });

        rvMyEvents.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}