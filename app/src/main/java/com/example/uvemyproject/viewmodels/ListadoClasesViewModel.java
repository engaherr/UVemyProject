package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.interfaces.INotificacionFragmentoClase;

import java.util.ArrayList;

public class ListadoClasesViewModel extends ViewModel {

    private final MutableLiveData<INotificacionFragmentoClase> notificacion = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ClaseDTO>> clasesDelCurso = new MutableLiveData<>();
    private final MutableLiveData<String> rol = new MutableLiveData<>();

    public MutableLiveData<INotificacionFragmentoClase> getNotificacion() {
        return notificacion;
    }

    public MutableLiveData<ArrayList<ClaseDTO>> getClasesDelCurso() {
        return clasesDelCurso;
    }

    public void setClasesDelCurso(ArrayList<ClaseDTO> clasesDelCurso) {
        this.clasesDelCurso.setValue(clasesDelCurso);
    }

    public void setNotificacion(INotificacionFragmentoClase notificacion) {
        this.notificacion.setValue(notificacion);
    }

    public void setRol(String rolNuevo) {
        rol.setValue(rolNuevo);
    }

    public String getRol() {
        return rol.getValue();
    }
}