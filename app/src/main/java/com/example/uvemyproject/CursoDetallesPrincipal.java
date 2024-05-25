package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentCursoDetallesPrincipalBinding;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.interfaces.INotificacionFragmentoClase;

public class CursoDetallesPrincipal extends Fragment implements INotificacionFragmentoClase {

    private FragmentCursoDetallesPrincipalBinding binding;
    private ListadoClases listadoClases;
    private CursoDetallesInformacion detallesCurso;
    private int fragmento = -1;

    public CursoDetallesPrincipal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCursoDetallesPrincipalBinding.inflate(inflater, container, false);

        //Obtener de la API
        listadoClases = new ListadoClases(this);
        detallesCurso = new CursoDetallesInformacion();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(binding.frgDetalles.getId(), detallesCurso).commit();
        fragmento = 2;

        binding.btnCambiarFragmento.setOnClickListener(v -> cambiarFragmento());

        return binding.getRoot();
    }

    private void cambiarFragmento(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (fragmento){
            case 1:
                binding.txtViewNombreBtnInformacion.setText("Listado de clases del curso");
                transaction.replace(binding.frgDetalles.getId(), detallesCurso).commit();
                fragmento = 2;
                break;

            case 2:
                binding.txtViewNombreBtnInformacion.setText("Información del curso");
                transaction.replace(binding.frgDetalles.getId(), listadoClases).commit();
                fragmento = 1;
                break;
        }
    }

    @Override
    public void cambiarVerMas(ClaseDTO clase) {
        ClaseDetalles claseDetalles = new ClaseDetalles();
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(claseDetalles);
    }

    @Override
    public void cambiarFormularioClase() {
        FormularioClase formularioClase = new FormularioClase();
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(formularioClase);
    }
}