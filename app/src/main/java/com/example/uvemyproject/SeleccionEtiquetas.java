package com.example.uvemyproject;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.uvemyproject.databinding.FragmentSeleccionEtiquetasBinding;
import com.example.uvemyproject.dto.CredencialesDTO;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.viewmodels.SeleccionEtiquetasViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class SeleccionEtiquetas extends Fragment {

    FragmentSeleccionEtiquetasBinding binding;

    SeleccionEtiquetasViewModel viewModel;

    UsuarioDTO usuario;
    private final List<Integer> idEtiquetasSeleccionadas = new ArrayList<>();
    boolean esActualizacionUsuario;

    // Constructor para Registro/Actualizacion de Usuario
    public SeleccionEtiquetas(UsuarioDTO usuario, boolean esActualizacionUsuario) {
        this.usuario = usuario;
        this.esActualizacionUsuario = esActualizacionUsuario;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSeleccionEtiquetasBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(SeleccionEtiquetasViewModel.class);

        observarStatus();
        observarEtiquetas();
        observarStatusCodigoVerificacion();
        observarJwt();

        viewModel.obtenerEtiquetas();

        binding.btnConfirmar.setOnClickListener(c -> {
            if(!esActualizacionUsuario && !idEtiquetasSeleccionadas.isEmpty()) {
                solicitarCodigoVerificacion();
            } else {
                Toast.makeText(getContext(), "Debe seleccionar al menos una etiqueta.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void observarJwt() {
        viewModel.getJwt().observe(getViewLifecycleOwner(), jwt -> {
            if(!esActualizacionUsuario) {
                usuario.setJwt(jwt);
                int[] idsArray = new int[idEtiquetasSeleccionadas.size()];

                for (int i = 0; i < idEtiquetasSeleccionadas.size(); i++) {
                    idsArray[i] = idEtiquetasSeleccionadas.get(i);
                }
                usuario.setIdsEtiqueta(idsArray);
                Log.d("Etiquetas", usuario.getIdsEtiqueta().toString());
                redireccionarConfirmacionCorreo();
            }
        });
    }

    private void redireccionarConfirmacionCorreo() {
        ConfirmacionCorreo confirmacionCorreo = new ConfirmacionCorreo(usuario);
        ((InicioSesion) getActivity()).cambiarFragmentoPrincipal(confirmacionCorreo);
    }

    private void observarStatusCodigoVerificacion() {
        viewModel.getStatusCodigoVerificacion().observe(getViewLifecycleOwner(), statusRequest -> {
            switch (statusRequest) {
                case ERROR:
                    Toast.makeText(getContext(), "Correo electrónico ya registrado. " +
                                    "Verifique la información e intentélo de nuevo más tarde."
                            , Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(), "No hay conexión con el servidor"
                            ,Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void solicitarCodigoVerificacion() {
        CredencialesDTO credenciales = new CredencialesDTO();
        credenciales.setCorreoElectronico(usuario.getCorreoElectronico());

        ponerEspera();
        viewModel.solicitarCodigoVerificacion(credenciales);
    }

    private void observarStatus() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case ERROR:
                    Toast.makeText(getContext(), "Ocurrió un error en el servidor."
                            , Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(), "No hay conexión con el servidor"
                            ,Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void observarEtiquetas() {
        viewModel.getEtiquetas().observe(getViewLifecycleOwner(), etiquetas -> {
            if (etiquetas != null) {
                for (EtiquetaDTO etiqueta : etiquetas) {
                    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(
                            binding.chpGroupEtiquetas.getContext(),
                            R.style.Theme_MaterialComponents_Chip
                    );

                    Chip chip = getChip(etiqueta, contextThemeWrapper);

                    binding.chpGroupEtiquetas.addView(chip);
                }
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
                Log.d("Etiquetas", "Seleccionado: " + idEtiqueta);
                idEtiquetasSeleccionadas.add(idEtiqueta);
            } else {
                Log.d("Etiquetas", "Deseleccionado: " + idEtiqueta);
                idEtiquetasSeleccionadas.remove(Integer.valueOf(idEtiqueta));
            }
        });
        return chip;
    }

    private void ponerEspera() {
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera() {
        binding.progressOverlay.setVisibility(View.GONE);
    }
}