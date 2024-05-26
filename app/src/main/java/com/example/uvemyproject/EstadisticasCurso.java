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
import java.util.Objects;


public class EstadisticasCurso extends Fragment {
    private int fragmento = -1;
    private ListadoEstudiantesCurso listadoEstudiantes;
    private ClasesComentarios listadoClases;
    private FragmentEstadisticasCursoBinding binding;
    private EstadisticasCursoViewModel viewModel;
    private static final int PICK_DIRECTORY_REQUEST_CODE = 1;

    public EstadisticasCurso() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEstadisticasCursoBinding.inflate(inflater, container, false);
        binding.imgViewRegresar.setOnClickListener( v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        viewModel = new ViewModelProvider(this).get(EstadisticasCursoViewModel.class);

        observarStatus();
        observarEstadisticas();
        observarReporte();
        obtenerDatosCurso();

        binding.lnrLayoutListadoEstudiantes.setOnClickListener(v -> cambiarListadoEstudiantes());
        binding.lnrLayoutComentariosClase.setOnClickListener(v -> cambiarListadoClases());

        binding.lnrLayoutEstadisticas.setOnClickListener( v -> recuperarReporte());

        return binding.getRoot();
    }

    private void obtenerDatosCurso(){
        Bundle args = getArguments();
        if (args != null) {
            int idCurso = args.getInt("clave_id_curso");
            viewModel.recuperarEstadisticasCurso(idCurso);
            ponerEspera();
        }
    }

    private void observarStatus(){
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
    }

    private void observarEstadisticas(){
        viewModel.getEstadisticas().observe(getViewLifecycleOwner(), estadisticas -> {
            if (estadisticas != null) {
                binding.setEstadisticasCurso(estadisticas);
                if(estadisticas.getClasesConComentarios() != null){
                    fragmento = 0;
                    listadoClases = new ClasesComentarios();
                    Bundle bundle = new Bundle();
                    ArrayList<ClaseEstadisticaDTO> clases = new ArrayList<>(estadisticas.getClasesConComentarios());
                    Log.i("a", clases.size() + " ");
                    Log.i("a", estadisticas.getClasesConComentarios().size() + " ");

                    bundle.putParcelableArrayList("clave_clases", clases);
                    listadoClases.setArguments(bundle);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.frgDetalles, listadoClases).commit();
                }

                if(estadisticas.getEstudiantesCurso() != null){
                    listadoEstudiantes = new ListadoEstudiantesCurso();
                    ArrayList<String> arrayListDeEstudiantes = new ArrayList<>(estadisticas.getEstudiantesCurso());
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("clave_listado_estudiantes", arrayListDeEstudiantes);
                    listadoClases.setArguments(bundle);
                }

            }
        });
    }

    private void observarReporte(){
        viewModel.getReporte().observe(getViewLifecycleOwner(), reporte ->{
            if(reporte == null){
                Toast.makeText(getContext(),"Ocurrió un error y no se pudo recuperar el reporte correctamente", Toast.LENGTH_SHORT).show();
            }else{
                binding.txtViewNombreGenerarDocumento.setText("Descargar reporte de las estadísticas del curso");
                binding.lnrLayoutEstadisticas.setOnClickListener( v -> descargarReporte());
            }
            quitarEspera();
        });
    }

    private void recuperarReporte(){
        ponerEspera();
        viewModel.recuperarDocumentoEstadisticas();
    }

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

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera(){
        binding.progressOverlay.setVisibility(View.GONE);
    }
}