package com.example.uvemyproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentConfirmacionCorreoBinding;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.dto.UsuarioRegistroDTO;
import com.example.uvemyproject.utils.StatusRequest;
import com.example.uvemyproject.viewmodels.ConfirmacionCorreoViewModel;

public class ConfirmacionCorreo extends Fragment {

    private FragmentConfirmacionCorreoBinding binding;

    private ConfirmacionCorreoViewModel viewModel;

    UsuarioRegistroDTO usuario;

    public ConfirmacionCorreo() {
    }

    public ConfirmacionCorreo(UsuarioDTO usuario) {
        this.usuario = new UsuarioRegistroDTO();

        this.usuario.setIdUsuario(usuario.getIdUsuario());
        this.usuario.setNombres(usuario.getNombres());
        this.usuario.setApellidos(usuario.getApellidos());
        this.usuario.setCorreoElectronico(usuario.getCorreoElectronico());
        this.usuario.setContrasena(usuario.getContrasena());
        this.usuario.setJwt(usuario.getJwt());
        this.usuario.setIdsEtiqueta(usuario.getIdsEtiqueta());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfirmacionCorreoBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(ConfirmacionCorreoViewModel.class);

        binding.edtTextCodigoVerificacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                binding.edtTextCodigoVerificacion.setBackgroundResource(R.drawable.background_lightblue);
            }
        });

        binding.btnConfirmar.setOnClickListener(c -> {
            if (validarCampo()) {
                usuario.setCodigoVerificacion(binding.edtTextCodigoVerificacion.getText()
                        .toString().trim());

                ponerEspera();
                viewModel.registrarUsuario(usuario);
            }
        });

        observarStatus();
        observarMensajeError();

        return binding.getRoot();
    }

    private void observarStatus() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case DONE:
                    Toast.makeText(getContext(),
                            "Usuario registrado correctamente", Toast.LENGTH_LONG).show();
                    redireccionarInicioSesion();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),
                            "No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
            if (status.equals(StatusRequest.ERROR_CONEXION)) {
                quitarEspera();
                Toast.makeText(getContext(),
                        "No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redireccionarInicioSesion() {
        Intent intent = new Intent(getActivity(), InicioSesion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void observarMensajeError() {
        viewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            quitarEspera();
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
        });
    }

    private boolean validarCampo() {
        boolean esValido = true;
        String codigoVerificacion = binding.edtTextCodigoVerificacion.getText().toString().trim();

        if (codigoVerificacion.isEmpty() || codigoVerificacion.length() < 4) {
            binding.edtTextCodigoVerificacion.setError("El código de verificación " +
                    "debe tener 4 dígitos");
            binding.edtTextCodigoVerificacion.setBackgroundResource(R.drawable.background_errorcampo);
            binding.edtTextCodigoVerificacion.requestFocus();
            esValido = false;
        }

        return esValido;
    }

    private void ponerEspera() { binding.progressOverlay.setVisibility(View.VISIBLE); }

    private void quitarEspera() { binding.progressOverlay.setVisibility(View.GONE); }
}