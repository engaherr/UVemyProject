package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentClaseDetallesBinding;
import com.example.uvemyproject.databinding.FragmentListadoClasesBinding;
import com.example.uvemyproject.viewmodels.ClaseDetallesViewModel;
import com.example.uvemyproject.viewmodels.EstadisticasCursoViewModel;

public class ClaseDetalles extends Fragment {
    private FragmentClaseDetallesBinding binding;
    private ClaseDetallesViewModel viewModel;
    public ClaseDetalles() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClaseDetallesBinding.inflate(getLayoutInflater());
        binding.imgViewRegresar.setOnClickListener( v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        viewModel = new ViewModelProvider(this).get(ClaseDetallesViewModel.class);

        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor, no se pudieron recuperar los datos de la clase", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });

        viewModel.getClaseActual().observe(getViewLifecycleOwner(), claseDTO -> {
            if(claseDTO != null){
                binding.setClase(claseDTO);
                Log.i("AAAAAAA", claseDTO.getVideoId() + " a");
            }
        });

        binding.lnrLayoutModificarClase.setOnClickListener(v -> cambiarFormularioClase());

        viewModel.recuperarDetallesClase(1);
        ponerEspera();

        return binding.getRoot();
    }

    private void cambiarFormularioClase(){
        FormularioClase formularioClase = new FormularioClase();
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(formularioClase);
    }

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera(){
        binding.progressOverlay.setVisibility(View.GONE);
    }
}