package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.ListaCursosServices;
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

    public void recuperarCursos(int pagina, String tituloCurso, int calificacionCurso, int idTipoCurso, int idEtiqueta){
        ListaCursosServices services = ApiClient.getInstance().getListaCursos();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        if(!tituloCurso.isEmpty()){
            recuperarCursosPorTitulo(pagina, tituloCurso);
        } else if(calificacionCurso != 0){
            recuperarCursosPorCalificacion(pagina,calificacionCurso);
        } else if(idTipoCurso != 0){
            recuperarCursosPorTipoCurso(pagina,idTipoCurso);
        } else if(idEtiqueta != 0){
            recuperarCursosPorEtiqueta(pagina,idEtiqueta);
        } else{
            recuperarCursosPagina(pagina);
        }
    }

    public void recuperarCursosPagina(int pagina) {
        ListaCursosServices services = ApiClient.getInstance().getListaCursos();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        services.recuperarCursosLista(auth, pagina).enqueue(new Callback<List<ListaCursoDTO>>() {
            @Override
            public void onResponse(Call<List<ListaCursoDTO>> call, Response<List<ListaCursoDTO>> response) {
                manejarRespuestaCorrecta(call, response);
            }
            @Override
            public void onFailure(Call<List<ListaCursoDTO>> call, Throwable t) {
                Log.e("Log", "Error en la solicitud: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void recuperarCursosPorTitulo(int pagina, String titulo) {
        ListaCursosServices services = ApiClient.getInstance().getListaCursos();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        services.recuperarCursosListaPorTitulo(auth, pagina,titulo).enqueue(new Callback<List<ListaCursoDTO>>() {
            @Override
            public void onResponse(Call<List<ListaCursoDTO>> call, Response<List<ListaCursoDTO>> response) {
                manejarRespuestaCorrecta(call, response);
            }
            @Override
            public void onFailure(Call<List<ListaCursoDTO>> call, Throwable t) {
                Log.e("Log", "Error en la solicitud: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void recuperarCursosPorCalificacion(int pagina, int calificacion) {
        ListaCursosServices services = ApiClient.getInstance().getListaCursos();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        services.recuperarCursosListaPorCalificacion(auth, pagina,calificacion).enqueue(new Callback<List<ListaCursoDTO>>() {
            @Override
            public void onResponse(Call<List<ListaCursoDTO>> call, Response<List<ListaCursoDTO>> response) {
                manejarRespuestaCorrecta(call, response);
            }
            @Override
            public void onFailure(Call<List<ListaCursoDTO>> call, Throwable t) {
                Log.e("Log", "Error en la solicitud: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void recuperarCursosPorTipoCurso(int pagina, int tipoCurso) {
        ListaCursosServices services = ApiClient.getInstance().getListaCursos();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        services.recuperarCursosListaPorTipoCursos(auth, pagina, tipoCurso).enqueue(new Callback<List<ListaCursoDTO>>() {
            @Override
            public void onResponse(Call<List<ListaCursoDTO>> call, Response<List<ListaCursoDTO>> response) {
                manejarRespuestaCorrecta(call, response);
            }
            @Override
            public void onFailure(Call<List<ListaCursoDTO>> call, Throwable t) {
                Log.e("Log", "Error en la solicitud: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void recuperarCursosPorEtiqueta(int pagina, int idEtiqueta) {
        ListaCursosServices services = ApiClient.getInstance().getListaCursos();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        services.recuperarCursosListaPorEtiquetas(auth, pagina, idEtiqueta).enqueue(new Callback<List<ListaCursoDTO>>() {
            @Override
            public void onResponse(Call<List<ListaCursoDTO>> call, Response<List<ListaCursoDTO>> response) {
                manejarRespuestaCorrecta(call, response);
            }
            @Override
            public void onFailure(Call<List<ListaCursoDTO>> call, Throwable t) {
                Log.e("Log", "Error en la solicitud: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private void manejarRespuestaCorrecta(Call<List<ListaCursoDTO>> call, Response<List<ListaCursoDTO>> response) {
        if (response.isSuccessful()) {
            List<ListaCursoDTO> cursos = response.body();
            if (cursos != null && !cursos.isEmpty()) {
                for (ListaCursoDTO curso : cursos) {
                    Log.d("Log", "ID Curso: " + curso.getIdCurso() + ", Nombre: " + curso.getTitulo() + " ID: " + curso.getDocumentos().get(0).getIdDocumento());
                }
            } else {
                Log.d("Log", "No se encontraron clases");
                cursos = new ArrayList<>();
            }
            setCursos(cursos);
            status.setValue(StatusRequest.DONE);
        } else {
            Log.d("Log", "Error en la solicitud: " + response.code());
            status.setValue(StatusRequest.ERROR);
        }
    }
}
