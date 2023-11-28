package fellipy.gustavo.joao_pedro.pedro.time_in.fragments;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.EditarPerfilActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.HomeActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Adapter.ListAdapter;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageDataComparator;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.HomeViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeusEventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeusEventosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeusEventosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeusEventosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeusEventosFragment newInstance(String param1, String param2) {
        MeusEventosFragment fragment = new MeusEventosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static MeusEventosFragment newInstance(){
        return new MeusEventosFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meus_eventos, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListAdapter listAdapter = new ListAdapter(new ImageDataComparator(), (HomeActivity) getActivity());
        HomeViewModel homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        LiveData<Usuario> usuarioLiveData = homeViewModel.loadUserDetailsLv();
        usuarioLiveData.observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                TextView tvNome = view.findViewById(R.id.tvNomeUsuario);
                TextView tvEmail = view.findViewById(R.id.tvEmailUsuario);
                TextView tvDataNasc = view.findViewById(R.id.tvDataNascimentoUsuario);
                TextView tvTelefone = view.findViewById(R.id.tvTelefoneUsuario);
                ImageView imgFoto = view.findViewById(R.id.imPerfil);

                tvNome.setText(usuario.nome);
                tvEmail.setText(usuario.email);
                tvTelefone.setText(usuario.telefone);
                tvDataNasc.setText(new SimpleDateFormat("dd/MM/yyyy").format(usuario.dataNasc));
                ImageCache.loadImageUrlToImageView(getActivity(), usuario.foto, imgFoto,200, 200);
            }
        });
        Button btnEditarInformacoes;
        btnEditarInformacoes = view.findViewById(R.id.btnEditarInformacoesPerfil);

        btnEditarInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.navegarTelas(EditarPerfilActivity.class);
            }
        });


    }
}