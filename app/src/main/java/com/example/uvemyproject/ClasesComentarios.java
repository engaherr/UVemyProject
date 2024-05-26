package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentClasesComentariosBinding;
import com.example.uvemyproject.databinding.FragmentEstadisticasCursoBinding;
import com.example.uvemyproject.dto.ClaseEstadisticaDTO;
import com.example.uvemyproject.viewmodels.ClaseDetallesViewModel;
import com.example.uvemyproject.viewmodels.ClasesComentariosViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClasesComentarios extends Fragment {

    private FragmentClasesComentariosBinding binding;
    private ClasesComentariosViewModel viewModel;

    public ClasesComentarios() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClasesComentariosBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(ClasesComentariosViewModel.class);

        ClaseComentarioAdapter adapter = new ClaseComentarioAdapter();
        binding.rcyViewListadoClases.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewListadoClases.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null) {
            ArrayList<ClaseEstadisticaDTO> listaEstadisticas = args.getParcelableArrayList("clave_clases");
            Log.i("aa", "no creo " + listaEstadisticas);
            if (listaEstadisticas != null) {
                Log.i("ya vi", "no creo");
                viewModel.guardarListaClases(listaEstadisticas);
            }
        }

        viewModel.getListaClases().observe(getViewLifecycleOwner(), clasesComentarios ->{
            adapter.submitList(clasesComentarios);
        });

        return binding.getRoot();
    }
}