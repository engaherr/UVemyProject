package com.example.uvemyproject.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListadoEstudiantesCursoViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> listadoEstudiantes = new MutableLiveData<>();

    public MutableLiveData<ArrayList<String>> getListadoEstudiantes() {
        return listadoEstudiantes;
    }

    public void guardarListadoEstudiantes(ArrayList<String> listado){
        listadoEstudiantes.setValue(listado);
    }
}
