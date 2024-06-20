package com.example.uvemyproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CursoDTO;

public class FormularioDetallesClaseViewModel extends ViewModel {
    MutableLiveData<ClaseDTO> clase = new MutableLiveData<>();
    MutableLiveData<CursoDTO> curso = new MutableLiveData<>();

    public void setClase(ClaseDTO claseNueva){
        clase.setValue(claseNueva);
    }
    public LiveData<ClaseDTO> obtenerClase(){
        return clase;
    }
    public void setCurso(CursoDTO cursoNuevo){
        curso.setValue(cursoNuevo);
    }
    public LiveData<CursoDTO> obtenerCurso(){
        return curso;
    }


}
