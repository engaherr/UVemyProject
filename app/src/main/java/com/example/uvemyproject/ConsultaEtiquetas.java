package com.example.uvemyproject;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.uvemyproject.databinding.FragmentConsultaEtiquetasBinding;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.viewmodels.ConsultaEtiquetasViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ConsultaEtiquetas extends Fragment {

    FragmentConsultaEtiquetasBinding binding;
    ConsultaEtiquetasViewModel viewModel;
    UsuarioDTO usuario;
    private final List<Integer> idEtiquetasSeleccionadas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConsultaEtiquetasBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(ConsultaEtiquetasViewModel.class);

        observarStatus();
        observarEtiquetas();
        observarJwt();

        viewModel.obtenerEtiquetas();

        binding.btnRegistrar.setOnClickListener(c -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, FormularioEtiqueta.newInstance())
                    .addToBackStack(null).commit();
        });

        binding.btnEliminar.setOnClickListener(c -> {
            if (!idEtiquetasSeleccionadas.isEmpty()){
                viewModel.eliminarEtiquetasSeleccionadas(idEtiquetasSeleccionadas);
                idEtiquetasSeleccionadas.clear();
            }else{
                Toast.makeText(getContext(), "Debe seleccionar al menos una etiqueta",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void observarStatus() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case ERROR:
                    Toast.makeText(getContext(), "Ocurrió un error al procesar la solicitud.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(), "No hay conexión con el servidor.",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void observarEtiquetas() {
        viewModel.getEtiquetas().observe(getViewLifecycleOwner(), etiquetas -> {
            if (etiquetas != null && !etiquetas.isEmpty()) {
                binding.txtInfoEtiquetas.setText("Etiquetas registradas en el sistema.");
                binding.chpGroupEtiquetas.removeAllViews();
                for (EtiquetaDTO etiqueta : etiquetas) {
                    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(
                            binding.chpGroupEtiquetas.getContext(),
                            R.style.Theme_MaterialComponents_Chip
                    );

                    Chip chip = getChip(etiqueta, contextThemeWrapper);
                    binding.chpGroupEtiquetas.addView(chip);
                }
            }else{
                binding.chpGroupEtiquetas.removeAllViews();
                binding.txtInfoEtiquetas.setText("No hay etiquetas disponibles.");
            }
        });
    }

    //TODO JWT
    private void observarJwt(){
        viewModel.getJwt().observe(getViewLifecycleOwner(), jwt -> {
            if (jwt != null && !jwt.isEmpty()) {
                usuario.setJwt(jwt);
            }
        });
    }

    @NonNull
    private Chip getChip(EtiquetaDTO etiqueta, ContextThemeWrapper contextThemeWrapper) {
        Chip chip = new Chip(contextThemeWrapper);
        chip.setText(etiqueta.getNombre());
        chip.setTag(etiqueta.getIdEtiqueta());
        chip.setCheckable(true);

        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int idEtiqueta = (int) buttonView.getTag();
            if (isChecked) {
                idEtiquetasSeleccionadas.add(idEtiqueta);
            } else {
                idEtiquetasSeleccionadas.remove(Integer.valueOf(idEtiqueta));
            }
        });
        return chip;
    }

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }
    private void quitarEspera() {
        binding.progressOverlay.setVisibility(View.GONE);
    }
}