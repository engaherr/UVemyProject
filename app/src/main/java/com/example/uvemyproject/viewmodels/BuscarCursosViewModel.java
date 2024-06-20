package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.EtiquetaServices;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.dto.TipoCursoDTO;
import com.example.uvemyproject.utils.StatusRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscarCursosViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private MutableLiveData<List<EtiquetaDTO>> etiquetas = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<TipoCursoDTO>> tiposCursos = new MutableLiveData<>();

    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }
    public MutableLiveData<List<TipoCursoDTO>> getTiposCursos() {
        return tiposCursos;
    }
    public MutableLiveData<List<EtiquetaDTO>> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<EtiquetaDTO> etiquetas) {
        this.etiquetas.setValue(etiquetas);
    }
    public void setTiposCursos(List<TipoCursoDTO> tiposCursos) {
        this.tiposCursos.setValue(tiposCursos);
    }
    public void obtenerEtiquetas() {
        EtiquetaServices service = ApiClient.getInstance().getEtiquetaServices();
        service.getEtiquetas().enqueue(new Callback<List<EtiquetaDTO>>() {
            @Override
            public void onResponse(Call<List<EtiquetaDTO>> call, Response<List<EtiquetaDTO>> response) {
                if(response.isSuccessful()) {
                    Log.d("ViewModel", "Etiquetas recibidas: " +
                            response.body().toString());
                    etiquetas.setValue(response.body());
                    status.setValue(StatusRequest.DONE);
                } else {
                    Log.e("RetrofitError", "Error al obtener etiquetas ");
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<EtiquetaDTO>> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }


}
