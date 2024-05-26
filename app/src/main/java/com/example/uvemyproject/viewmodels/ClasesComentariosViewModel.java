package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.dto.ClaseEstadisticaDTO;

import java.util.ArrayList;
import java.util.List;

public class ClasesComentariosViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ClaseEstadisticaDTO>> listaClases = new MutableLiveData<>();

    public MutableLiveData<ArrayList<ClaseEstadisticaDTO>> getListaClases() {
        return listaClases;
    }

    public void guardarListaClases(ArrayList<ClaseEstadisticaDTO> clases){
        Log.i("aa", "no creo 2");
        Log.i("a", clases.size() + " noo");
        listaClases.setValue(clases);
    }
}
