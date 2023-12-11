package fellipy.gustavo.joao_pedro.pedro.time_in.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.CadastroActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.CadastroEventoActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.EditarPerfilActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.HomeActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.LoginActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;
import fellipy.gustavo.joao_pedro.pedro.time_in.ImageCache;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EventoViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.HomeViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Config;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static PerfilFragment newInstance(){
        return new PerfilFragment();
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
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        Button btnEncerrarSessao = view.findViewById(R.id.btnDeslogar);
        btnEncerrarSessao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.setLogin(getActivity(), "");
                Config.setPassword(getActivity(), "");
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });

        Button btnAlterarSenha = view.findViewById(R.id.btnAlterarSenha);

        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = requireActivity().getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                View dlgView = inflater.inflate(R.layout.alterar_senha_dlg, null);
                builder.setView(dlgView)
                        // Add action buttons
                        .setPositiveButton("ALterar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText etSenhaAntiga = dlgView.findViewById(R.id.etSenhaAntiga);
                                EditText etConfirmarSenha = dlgView.findViewById(R.id.etConfimarSenha);
                                EditText etSenhaNova = dlgView.findViewById(R.id.etSenhaNova);

                                String senhaNova = etSenhaNova.getText().toString();
                                String senhaAntiga = etSenhaAntiga.getText().toString();
                                String senhaConfirmar = etConfirmarSenha.getText().toString();
                                if(senhaNova.equals(senhaConfirmar)){
                                    LiveData<Boolean> atualizarSenha = homeViewModel.
                                            updateUserPass(senhaAntiga, senhaNova);

                                    atualizarSenha.observe(getViewLifecycleOwner(),
                                            new Observer<Boolean>() {
                                        @Override
                                        public void onChanged(Boolean aBoolean) {
                                            if(aBoolean){
                                                Toast.makeText(getActivity(),
                                                        "Senha atualizada com sucesso",
                                                        Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(getActivity(),
                                                        "Não foi possível atualizar a senha.",
                                                        Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                                }else{
                                    Toast.makeText(getActivity(),
                                            "Não foi possível atualizar a senha.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //LoginDialogFragment.this.getDialog().cancel();
                            }
                        });
                Dialog dlgConfirmar = builder.create();
                dlgConfirmar.show();
            }
        });

    }
}