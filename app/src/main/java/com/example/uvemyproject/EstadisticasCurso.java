package com.example.uvemyproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private EstadisticasCursoViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEstadisticasCursoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(EstadisticasCursoViewModel.class);

        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor, no se pudieron recuperar los datos", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });

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

        viewModel.getReporte().observe(getViewLifecycleOwner(), reporte ->{
            if(reporte == null){
                Toast.makeText(getContext(),"Ocurrió un error y no se pudo recuperar el reporte correctamente", Toast.LENGTH_SHORT).show();
            }else{
                binding.btnGenerarReporte.setText("Descargar reporte de las estadísticas del curso");
                binding.btnGenerarReporte.setOnClickListener( v -> descargarReporte());
            }
            quitarEspera();
        });

        viewModel.recuperarEstadisticasCurso();
        ponerEspera();

        binding.lnrLayoutListadoEstudiantes.setOnClickListener(v -> cambiarListadoEstudiantes());
        binding.lnrLayoutComentariosClase.setOnClickListener(v -> cambiarListadoClases());

        binding.btnGenerarReporte.setOnClickListener( v -> recuperarReporte());

        return binding.getRoot();
    }

    private void recuperarReporte(){
        ponerEspera();
        viewModel.recuperarDocumentoEstadisticas();
    }

    private static final int PICK_DIRECTORY_REQUEST_CODE = 1;
    private void descargarReporte(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona un directorio"), PICK_DIRECTORY_REQUEST_CODE);
        Log.i("Descargando", "Reporte");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DIRECTORY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri treeUri = data.getData();
            int respuesta = viewModel.descargarReporte(getContext(), treeUri);
            if(respuesta == 0){
                Toast.makeText(getContext(),"Se descargó existosamente el reporte", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Hubo un problema al descargar el archivo", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera(){
        binding.progressOverlay.setVisibility(View.GONE);
    }
}