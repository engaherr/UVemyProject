package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentClaseDetallesBinding;
import com.example.uvemyproject.databinding.FragmentListadoClasesBinding;

public class ClaseDetalles extends Fragment {
    private FragmentClaseDetallesBinding binding;
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

        binding.lnrLayoutModificarClase.setOnClickListener(v -> cambiarFormularioClase());

        return binding.getRoot();
    }

    private void cambiarFormularioClase(){
        FormularioClase formularioClase = new FormularioClase();
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(formularioClase);
    }
}