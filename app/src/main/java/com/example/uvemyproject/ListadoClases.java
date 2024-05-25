package com.example.uvemyproject;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentListadoClasesBinding;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.interfaces.INotificacionFragmentoClase;

import java.util.ArrayList;

public class ListadoClases extends Fragment {
    private FragmentListadoClasesBinding binding;
    private INotificacionFragmentoClase notificacionFragmentoClase;


    public ListadoClases() {
        // Required empty public constructor
    }

    public ListadoClases(INotificacionFragmentoClase notificacionVerClase) {
        this.notificacionFragmentoClase = notificacionVerClase;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListadoClasesBinding.inflate(getLayoutInflater());

        ClaseAdapter adapter = new ClaseAdapter();
        adapter.setOnItemClickListener( (clase, position) -> { cambiarFragmentoDetalles(clase, position); });
        binding.rcyViewListadoClases.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewListadoClases.setAdapter(adapter);

        ArrayList<ClaseDTO> clases = new ArrayList<ClaseDTO>();
        clases.add(new ClaseDTO(1, "Es una clase donde ponemos ciertas cosas", "Figuras como hacer figuras"));
        clases.add(new ClaseDTO(2, "Es una clase", "Fromas para mejorar objetcos"));
        adapter.submitList(clases);

        binding.lnrLayoutAgregarClase.setOnClickListener(v -> notificacionFragmentoClase.cambiarFormularioClase());
        return binding.getRoot();
    }

    private void cambiarFragmentoDetalles(ClaseDTO clase, int posicion){
        notificacionFragmentoClase.cambiarVerMas(clase);
    }

}