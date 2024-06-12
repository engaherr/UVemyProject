package com.example.uvemyproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uvemyproject.databinding.FragmentFormularioUsuarioBinding;

public class FormularioUsuario extends Fragment {

    private FragmentFormularioUsuarioBinding binding;

    boolean esActualizacion;

    public FormularioUsuario() {
    }

    public FormularioUsuario(boolean esActualizacion) {
        this.esActualizacion = esActualizacion;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormularioUsuarioBinding.inflate(getLayoutInflater());

        if(!esActualizacion) {
            binding.imgViewRegresar.setVisibility(View.GONE);
        }

        binding.txtViewIniciarSesion.setOnClickListener(c -> {
            Intent intent = new Intent(getActivity(), InicioSesion.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return binding.getRoot();
    }
}