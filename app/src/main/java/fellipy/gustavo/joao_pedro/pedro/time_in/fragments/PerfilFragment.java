package fellipy.gustavo.joao_pedro.pedro.time_in.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fellipy.gustavo.joao_pedro.pedro.time_in.Activities.HomeActivity;
import fellipy.gustavo.joao_pedro.pedro.time_in.EventosRepository;
import fellipy.gustavo.joao_pedro.pedro.time_in.Model.EventoViewModel;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;
import fellipy.gustavo.joao_pedro.pedro.time_in.Usuario;
import fellipy.gustavo.joao_pedro.pedro.time_in.util.Config;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    private Handler handler = new Handler();

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
        EventosRepository eventosRepository = new EventosRepository(getActivity());
        Usuario u = eventosRepository.loadUserDetail();

        TextView tvNome = view.findViewById(R.id.tvNomeUsuario);
        TextView tvEmail = view.findViewById(R.id.tvEmailUsuario);
        TextView tvDataNasc = view.findViewById(R.id.tvDataNascimentoUsuario);
        TextView tvTelefone = view.findViewById(R.id.tvTelefoneUsuario);
        ImageView imgFoto = view.findViewById(R.id.imPerfil);

        tvNome.setText(u.nome);
        tvEmail.setText(u.email);
        tvTelefone.setText(u.telefone);
        tvDataNasc.setText(u.dataNasc.toString());
        new Thread() {
            public void run(){
                Bitmap img = null;
                try {
                    URL url = new URL(u.foto);
                    HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                    InputStream input = conexao.getInputStream();
                    img = BitmapFactory.decodeStream(input);
                } catch(MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }

                final Bitmap imgAux = img;
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        imgFoto.setImageBitmap(imgAux);
                    }
                });
            }
        }.start();

    }
}