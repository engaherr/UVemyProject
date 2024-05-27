package com.example.uvemyproject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentCalificacionCursoBinding;
import com.example.uvemyproject.viewmodels.CalificacionCursoViewModel;

public class CalificacionCurso extends Fragment {

    private FragmentCalificacionCursoBinding binding;
    private CalificacionCursoViewModel viewModel;

    public CalificacionCurso() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalificacionCursoBinding.inflate(inflater, container, false);
        binding.imgViewRegresar.setOnClickListener( v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        viewModel = new ViewModelProvider(this).get(CalificacionCursoViewModel.class);

        binding.btnRestarCalificacion.setOnClickListener(v -> {
            binding.txtViewCalificacion.setTextColor(Color.parseColor("#9d9d9d"));
            viewModel.disminuirCalificacion();
        });

        binding.btnAgregarCalificacion.setOnClickListener(v ->{
            binding.txtViewCalificacion.setTextColor(Color.parseColor("#9d9d9d"));
            viewModel.aumentarCalificacion();
        });

        binding.btnGuardarCalificacion.setOnClickListener(v ->{
            viewModel.guardarCalificacionCurso();
        });

        observarStatus();
        observarCalificacion();
        obtenerCalificacionCurso();
        ponerEspera();

        return binding.getRoot();
    }
    private void observarStatus(){
        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case DONE:
                    binding.txtViewCalificacion.setTextColor(Color.parseColor("#000000"));
                    Toast.makeText(getContext(),"Curso calificado exitosamente", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void observarCalificacion(){
        viewModel.getUsuarioCalificacion().observe(getViewLifecycleOwner(), usuarioCalificacion ->{
            if(usuarioCalificacion != null){
                if(usuarioCalificacion.getCalificacion() == 0){
                    binding.txtViewCalificacion.setText("10");
                    binding.txtViewCalificacion.setTextColor(Color.parseColor("#9d9d9d"));
                }else{
                    binding.txtViewCalificacion.setText(String.valueOf(usuarioCalificacion.getCalificacion()));
                }
            }
            quitarEspera();
        });
    }


    private void obtenerCalificacionCurso(){
        Bundle args = getArguments();
        if (args != null) {
            int idCurso = args.getInt("clave_id_curso");
            String nombre = args.getString("clave_nombre_curso");
            binding.txtViewNombreClase.setText(nombre);
            viewModel.obtenerUsuarioCalificacion(idCurso);
            ponerEspera();
        }
    }

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera(){
        binding.progressOverlay.setVisibility(View.GONE);
    }

}