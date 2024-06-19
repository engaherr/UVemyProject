package com.example.uvemyproject;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentFormularioEtiquetaBinding;
import com.example.uvemyproject.databinding.FragmentFormularioUsuarioBinding;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.viewmodels.FormularioEtiquetaViewModel;

public class FormularioEtiqueta extends Fragment {
    private FragmentFormularioEtiquetaBinding binding;
    private FormularioEtiquetaViewModel viewModel;

    public static FormularioEtiqueta newInstance() {
        return new FormularioEtiqueta();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormularioEtiquetaBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(FormularioEtiquetaViewModel.class);

        observarStatus();
        quitarEspera();

        binding.btnRegistrar.setOnClickListener(v -> {
            String nombreEtiqueta = binding.regTextNombre.getText().toString().trim();

            if (!nombreEtiqueta.isEmpty()) {
                if (nombreEtiqueta.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+")) {
                    ponerEspera();
                    viewModel.registrarEtiqueta(nombreEtiqueta);
                } else {
                    Toast.makeText(requireContext(), "Solo se permiten letras y números.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Ingrese un nombre para la etiqueta",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.imgViewRegresar.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return binding.getRoot();
    }

    private void observarStatus() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case DONE:
                    Toast.makeText(getContext(), "Etiqueta registrada correctamente.",
                            Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Ocurrió un error al registrar la etiqueta.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(), "No hay conexión con el servidor.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case BAD_REQUEST:
                    Toast.makeText(getContext(), "Nombre de etiqueta existente.",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }
    private void quitarEspera() {
        binding.progressOverlay.setVisibility(View.GONE);
    }

}