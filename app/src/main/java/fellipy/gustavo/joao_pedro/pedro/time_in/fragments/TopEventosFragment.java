package fellipy.gustavo.joao_pedro.pedro.time_in.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.CadastroEventoActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.HomeActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.LoginActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Adapter.CarrosselAdapter;
import fellipy.gustavo.joao_pedro.pedro.time_in.Adapter.FiltrosAdapter;
import fellipy.gustavo.joao_pedro.pedro.time_in.Adapter.ListAdapter;
import fellipy.gustavo.joao_pedro.pedro.time_in.Esporte;
import fellipy.gustavo.joao_pedro.pedro.time_in.EsporteDataComparator;
import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventoDataComparator;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.HomeViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Config;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopEventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopEventosFragment extends Fragment {


    Button btnCriarEventosTopEventos;
    private HomeViewModel hViewModel;

    View thisView;


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
        thisView = view;
        hViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        FiltrosAdapter filtrosAdapter = new FiltrosAdapter(new EsporteDataComparator(),
                this);

        CarrosselAdapter carrosselAdapter = new CarrosselAdapter(new EventoDataComparator(),
                (HomeActivity) getActivity());
        ListAdapter listAdapter = new ListAdapter(new EventoDataComparator(),
                (HomeActivity) getActivity());
        LiveData<PagingData<Esporte>> sportsLd = hViewModel.getSportsLd();

        LiveData<PagingData<Evento>> liveData = hViewModel.getEventsLd();
        liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
            @Override
            public void onChanged(PagingData<Evento> eventoPagingData) {
                listAdapter.submitData(getViewLifecycleOwner().getLifecycle(), eventoPagingData);
            }
        });

        sportsLd.observe(getViewLifecycleOwner(), new Observer<PagingData<Esporte>>() {
            @Override
            public void onChanged(PagingData<Esporte> esportePagingData) {
                filtrosAdapter.submitData(getViewLifecycleOwner().getLifecycle(), esportePagingData);
            }
        });

        RecyclerView rvFiltrosEsportes = (RecyclerView) view.findViewById(R.id.rvFiltrosEsportes);
        rvFiltrosEsportes.setAdapter(filtrosAdapter);
        rvFiltrosEsportes.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false));

        LiveData<PagingData<Evento>> topLiveData = hViewModel.getTopEventsLd();



        topLiveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
            @Override
            public void onChanged(PagingData<Evento> eventoPagingData) {
                carrosselAdapter.submitData(getViewLifecycleOwner().getLifecycle(),
                        eventoPagingData);
            }
        });
        RecyclerView rvEvento = (RecyclerView) view.findViewById(R.id.rvEventos);
        rvEvento.setAdapter(listAdapter);
        rvEvento.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView rvCarrossel = (RecyclerView) view.findViewById(R.id.rvTopEventos);
        rvCarrossel.setAdapter(carrosselAdapter);
        rvCarrossel.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false));

        btnCriarEventosTopEventos = view.findViewById(R.id.btnCriarEventoTopEventos);
        btnCriarEventosTopEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Config.getLogin(getActivity()).isEmpty()) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.navegarTelas(CadastroEventoActivity.class);
                }else{
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.navegarTelas(LoginActivity.class);
                }
            }
        });

        SearchView etPesquisar = view.findViewById(R.id.etPesquisar);
        etPesquisar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ListAdapter listAdapter = new ListAdapter(new EventoDataComparator(),
                        (HomeActivity) getActivity());
                LiveData<PagingData<Evento>> liveData = hViewModel.loadSearchedEvents(s);
                liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
                    @Override
                    public void onChanged(PagingData<Evento> eventoPagingData) {
                        listAdapter.submitData(getViewLifecycleOwner().getLifecycle(),
                                eventoPagingData);
                    }
                });

                rvEvento.setAdapter(listAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        etPesquisar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                LiveData<PagingData<Evento>> liveData = hViewModel.getEventsLd();

                liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
                    @Override
                    public void onChanged(PagingData<Evento> eventoPagingData) {
                        listAdapter.submitData(getViewLifecycleOwner().getLifecycle(), eventoPagingData);
                    }
                });
                return true;
            }
        });
        ImageButton imgFiltro = view.findViewById(R.id.imgbtnFiltrar);
        ImageCache.loadImageUrlToImageView(getActivity(), "https://i.imgur.com/pbITKlG.png",
                imgFiltro, 60, 60);
        imgFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = requireActivity().getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                View dlgView = inflater.inflate(R.layout.filtros_dlg, null);
                builder.setView(dlgView)
                        .setPositiveButton("ALterar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Spinner spnOrdernarPreco = dlgView.findViewById(R.id.spnOrdernarPreco);
                                Spinner spnPreco = dlgView.findViewById(R.id.spnPrecoFiltro);
                                Spinner spnIntuito = dlgView.findViewById(R.id.spnIntuitoFiltro);
                                Spinner spnIdade = dlgView.findViewById(R.id.spnIdadeFiltro);


                                List<String> filtros = new ArrayList<>();
                                filtros.add(Integer.toString(spnPreco.getSelectedItemPosition() - 1));
                                filtros.add(spnIdade.getSelectedItem().toString());
                                filtros.add(spnIntuito.getSelectedItem().toString());
                                filtros.add(spnOrdernarPreco.getSelectedItem().toString());

                                ListAdapter listAdapter = new ListAdapter(new EventoDataComparator(),
                                        (HomeActivity) getActivity());
                                LiveData<PagingData<Evento>> ldEventosFiltrados = hViewModel.getFilterEventsLd(filtros);
                                ldEventosFiltrados.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
                                    @Override
                                    public void onChanged(PagingData<Evento> eventoPagingData) {
                                        listAdapter.submitData(getViewLifecycleOwner().getLifecycle(), eventoPagingData);
                                    }
                                });
                                rvEvento.setAdapter(listAdapter);
                                rvEvento.setLayoutManager(new LinearLayoutManager(getContext()));
                            }


                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                Dialog dlgConfirmar = builder.create();
                dlgConfirmar.show();
            }
        });

    }

    public void setIdEsporte(String id){
        LiveData<PagingData<Evento>> sportsEventsLd = hViewModel.getSportsEventsLd(id);

        ListAdapter listAdapter = new ListAdapter(new EventoDataComparator(),
                (HomeActivity) getActivity());
        sportsEventsLd.observe(getViewLifecycleOwner(), new Observer<PagingData<Evento>>() {
            @Override
            public void onChanged(PagingData<Evento> eventoPagingData) {
                listAdapter.submitData(getViewLifecycleOwner().getLifecycle(), eventoPagingData);
            }
        });

        RecyclerView rvEvento = (RecyclerView) thisView.findViewById(R.id.rvEventos);
        rvEvento.setAdapter(listAdapter);
        rvEvento.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}