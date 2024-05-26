package com.example.uvemyproject;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentListadoClasesBinding;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.interfaces.INotificacionFragmentoClase;
import com.example.uvemyproject.viewmodels.ListadoClasesViewModel;
import com.example.uvemyproject.viewmodels.ListadoEstudiantesCursoViewModel;

import java.util.ArrayList;

public class ListadoClases extends Fragment {
    private FragmentListadoClasesBinding binding;
    private ListadoClasesViewModel viewModel;
    private INotificacionFragmentoClase notificacionFragmentoClase;

    public ListadoClases() {
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
        viewModel = new ViewModelProvider(this).get(ListadoClasesViewModel.class);

        if(notificacionFragmentoClase != null){
            viewModel.setNotificacion(notificacionFragmentoClase);
        }

        ClaseAdapter adapter = new ClaseAdapter();
        adapter.setOnItemClickListener( (clase, position) -> { cambiarFragmentoDetalles(clase, position); });
        binding.rcyViewListadoClases.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewListadoClases.setAdapter(adapter);

        viewModel.getClasesDelCurso().observe(getViewLifecycleOwner(), clases ->{
            adapter.submitList(clases);
        });

        binding.lnrLayoutAgregarClase.setOnClickListener(v -> viewModel.getNotificacion().getValue().cambiarFormularioClase());

        obtenerListadoClases();
        return binding.getRoot();
    }

    private void cambiarFragmentoDetalles(ClaseDTO clase, int posicion){
        viewModel.getNotificacion().getValue().cambiarVerMas(clase);
    }

    private void obtenerListadoClases(){
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<ClaseDTO> clases = args.getParcelableArrayList("clave_clases");
            viewModel.setClasesDelCurso(clases);
        }
    }

}