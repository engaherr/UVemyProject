package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentEstadisticasCursoBinding;
import com.example.uvemyproject.databinding.FragmentListadoEstudiantesCursoBinding;
import com.example.uvemyproject.viewmodels.EstadisticasCursoViewModel;
import com.example.uvemyproject.viewmodels.ListadoEstudiantesCursoViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListadoEstudiantesCurso extends Fragment {

    private FragmentListadoEstudiantesCursoBinding binding;
    private ListadoEstudiantesCursoViewModel viewModel;
    private ArrayList<String> estudiantes;
    public ListadoEstudiantesCurso() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListadoEstudiantesCursoBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(ListadoEstudiantesCursoViewModel.class);

        EstudianteAdapter adapter = new EstudianteAdapter();
        binding.rcyViewListadoEstudiantes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewListadoEstudiantes.setAdapter(adapter);

        viewModel.getListadoEstudiantes().observe(getViewLifecycleOwner(), estudiantes ->{
            adapter.submitList(estudiantes);
        });

        if(estudiantes != null){
            viewModel.guardarListadoEstudiantes(estudiantes);
        }

        return binding.getRoot();
    }

    public void recibirEstudiantes(ArrayList<String> estudiantes){
        if(estudiantes != null){
            this.estudiantes = estudiantes;
        }
    }
}