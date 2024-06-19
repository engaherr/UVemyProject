package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.ListaCursos;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.ListaCursoDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoCursosViewModel extends ViewModel {
    private final MutableLiveData<List<ListaCursoDTO>> cursos = new MutableLiveData<List<ListaCursoDTO>>();
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }
    public MutableLiveData<List<ListaCursoDTO>> getCursos() {
        return cursos;
    }
    public void setCursos(List<ListaCursoDTO> cursosRecuperados) {
         cursos.setValue(cursosRecuperados);
    }

    public void recuperarCursos(int pagina) {
        ListaCursos services = ApiClient.getInstance().getListaCursos();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        services.recuperarCursosLista(auth, pagina).enqueue(new Callback<List<ListaCursoDTO>>() {
            @Override
            public void onResponse(Call<List<ListaCursoDTO>> call, Response<List<ListaCursoDTO>> response) {
                if (response.isSuccessful()) {
                    List<ListaCursoDTO> cursos = response.body();
                    if (cursos != null && !cursos.isEmpty()) {
                        for (ListaCursoDTO curso : cursos) {
                            Log.d("Log", "ID Curso: " + curso.getIdCurso() + ", Nombre: " + curso.getTitulo()+" ID: "+curso.getDocumentos().get(0).getIdDocumento());
                        }
                    } else {
                        Log.d("Log", "No se encontraron clases");
                        cursos = new ArrayList<>();
                    }
                    setCursos(cursos);
                } else {
                    Log.d("Log", "Error en la solicitud: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ListaCursoDTO>> call, Throwable t) {
                Log.e("Log", "Error en la solicitud: " + t.getMessage(), t);
            }
        });

    }




}
