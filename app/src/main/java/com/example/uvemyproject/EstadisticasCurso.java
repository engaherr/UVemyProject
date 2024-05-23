package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentEstadisticasCursoBinding;
import com.example.uvemyproject.dto.ClaseEstadisticaDTO;
import com.example.uvemyproject.dto.EstadisticasCursoDTO;
import com.example.uvemyproject.viewmodels.EstadisticasCursoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstadisticasCurso#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstadisticasCurso extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EstadisticasCurso() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstadisticasCurso.
     */
    // TODO: Rename and change types and number of parameters
    public static EstadisticasCurso newInstance(String param1, String param2) {
        EstadisticasCurso fragment = new EstadisticasCurso();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private int fragmento = -1;
    private ListadoEstudiantesCurso listadoEstudiantes;
    private ClasesComentarios listadoClases;
    private FragmentEstadisticasCursoBinding binding;
    private EstadisticasCursoDTO estadisticasCursoDTO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEstadisticasCursoBinding.inflate(inflater, container, false);

        EstadisticasCursoViewModel viewModel = new ViewModelProvider(this).get(EstadisticasCursoViewModel.class);
        viewModel.getEstadisticas().observe(getViewLifecycleOwner(), estadisticas -> {
            if (estadisticas != null) {
                estadisticasCursoDTO = estadisticas;
                binding.setEstadisticasCurso(estadisticas);

                if(estadisticas.getClasesConComentarios() != null){
                    fragmento = 0;
                    listadoClases = new ClasesComentarios(convertirObjetos(estadisticas.getClasesConComentarios()));
                    Fragment childFragment = listadoClases;
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.frgDetalles, childFragment).commit();
                }

                if(estadisticas.getEstudiantesCurso() != null){
                    listadoEstudiantes = new ListadoEstudiantesCurso(estadisticas.getEstudiantesCurso());
                }

            }
        });

        viewModel.recuperarEstadisticasCurso();

        binding.lnrLayoutListadoEstudiantes.setOnClickListener(v -> cambiarListadoEstudiantes());
        binding.lnrLayoutComentariosClase.setOnClickListener(v -> cambiarListadoClases());

        return binding.getRoot();
    }

    private void cambiarListadoEstudiantes(){
        if(fragmento != 1 && listadoEstudiantes != null){
            fragmento = 1;

            Fragment childFragment = listadoEstudiantes;
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.frgDetalles, childFragment).commit();
        }
    }

    private void cambiarListadoClases(){
        if(fragmento != 0 && listadoClases != null){
            fragmento = 0;

            Fragment childFragment = listadoClases;
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.frgDetalles, childFragment).commit();
        }
    }

    private List<ClaseEstadisticaDTO> convertirObjetos(List<EstadisticasCursoDTO.ClaseEstadisticaDTO> clases){
        List<ClaseEstadisticaDTO> claseEstadisticaDTOS = new ArrayList<>();
        for (int i = 0; i < clases.size(); i++) {
            ClaseEstadisticaDTO claseNueva = new ClaseEstadisticaDTO();
            claseNueva.setNombre(clases.get(i).getNombre());
            claseNueva.setCantidadComentarios(clases.get(i).getCantidadComentarios());
            claseEstadisticaDTOS.add(claseNueva);
        }
        return claseEstadisticaDTOS;
    }
}