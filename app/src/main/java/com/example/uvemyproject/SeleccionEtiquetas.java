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
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.viewmodels.SeleccionEtiquetasViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SeleccionEtiquetas extends Fragment {

    FragmentSeleccionEtiquetasBinding binding;

    SeleccionEtiquetasViewModel viewModel;

    UsuarioDTO usuario;
    private final List<Integer> idEtiquetasSeleccionadas = new ArrayList<>();
    boolean esActualizacionUsuario;

    // Constructor para Registro/Actualizacion de Usuario TODO: Constructor para cursos
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

        //TODO: Poner caso para cuando es creación/actualización de curso
        if(esActualizacionUsuario) {
            binding.btnConfirmar.setVisibility(View.GONE);
            binding.btnGuardar.setVisibility(View.VISIBLE);
            observarStatusActualizarUsuarioEtiquetas();
        }

        binding.btnConfirmar.setOnClickListener(c -> {
            if(!idEtiquetasSeleccionadas.isEmpty()) {
                solicitarCodigoVerificacion();
            } else {
                Toast.makeText(getContext(), "Debe seleccionar al menos una etiqueta.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnGuardar.setOnClickListener(c -> {
            Log.d("Etiquetas", "Guardar " + idEtiquetasSeleccionadas);
            if(esActualizacionUsuario && !idEtiquetasSeleccionadas.isEmpty()) {
                actualizarEtiquetasUsuario();
            } else {
                Toast.makeText(getContext(), "Debe seleccionar al menos una etiqueta.",
                        Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }

    private void observarStatusActualizarUsuarioEtiquetas() {
        viewModel.getStatusActualizarUsuarioEtiquetas().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case DONE:
                    Toast.makeText(getContext(), "Etiquetas actualizadas correctamente.",
                            Toast.LENGTH_SHORT).show();

                    int[] idsArray = new int[idEtiquetasSeleccionadas.size()];

                    for (int i = 0; i < idEtiquetasSeleccionadas.size(); i++) {
                        idsArray[i] = idEtiquetasSeleccionadas.get(i);
                    }

                    SingletonUsuario.setIdsEtiqueta(idsArray);

                    ((MainActivity) getActivity()).cambiarFragmentoPrincipal(
                            new FormularioUsuario(true));
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Ocurrió un error en el servidor. Intentélo " +
                                    "de nuevo más tarde", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(), "No hay conexión con el servidor. Intentélo " +
                                    "de nuevo más tarde" ,Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void actualizarEtiquetasUsuario() {
        int[] idsArray = new int[idEtiquetasSeleccionadas.size()];

        for (int i = 0; i < idEtiquetasSeleccionadas.size(); i++) {
            idsArray[i] = idEtiquetasSeleccionadas.get(i);
        }

        ponerEspera();
        viewModel.actualizarEtiquetasUsuario(idsArray);
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
                Log.d("Etiquetas", Arrays.toString(usuario.getIdsEtiqueta()));
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

                    // TODO: Poner caso para cuando es actualización de curso
                    if(esActualizacionUsuario) {
                        for (int idEtiqueta : SingletonUsuario.getIdsEtiqueta()) {
                            if(idEtiqueta == etiqueta.getIdEtiqueta()) {
                                chip.setChecked(true);
                            }
                        }
                    }

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
                Log.d("Etiquetas", "Seleccionado: " + idEtiqueta + " " +
                        idEtiquetasSeleccionadas);
                idEtiquetasSeleccionadas.add(idEtiqueta);
            } else {
                Log.d("Etiquetas", "Deseleccionado: " + idEtiqueta +
                        " " + idEtiquetasSeleccionadas);
                idEtiquetasSeleccionadas.removeAll(Collections.singleton(idEtiqueta));
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