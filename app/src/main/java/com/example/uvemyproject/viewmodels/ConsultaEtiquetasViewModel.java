package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.EtiquetaServices;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.utils.StatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultaEtiquetasViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<List<EtiquetaDTO>> etiquetas = new MutableLiveData<>();
    private final MutableLiveData<String> jwt = new MutableLiveData<>();
    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public MutableLiveData<List<EtiquetaDTO>> getEtiquetas() {
        return etiquetas;
    }
    public MutableLiveData<String> getJwt() {
        return jwt;
    }

    public void obtenerEtiquetas() {
        EtiquetaServices service = ApiClient.getInstance().getEtiquetaServices();

        //TODO JWT
        service.getEtiquetas().enqueue(new Callback<List<EtiquetaDTO>>() {
            @Override
            public void onResponse(Call<List<EtiquetaDTO>> call, Response<List<EtiquetaDTO>> response) {
                if(response.isSuccessful()) {
                    etiquetas.postValue(response.body());
                    status.setValue(StatusRequest.DONE);
                } else {
                    Log.e("RetrofitError", "Error al obtener etiquetas");
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

    public void eliminarEtiquetasSeleccionadas(List<Integer> idsEtiquetas) {
        EtiquetaServices service = ApiClient.getInstance().getEtiquetaServices();

        //TODO JWT
        for (int idEtiqueta : idsEtiquetas) {
            service.eliminarEtiqueta(idEtiqueta).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("ConsultaEtiquetasViewModel", "Etiqueta eliminada exitosamente: " + idEtiqueta);
                        status.setValue(StatusRequest.DONE);
                        obtenerEtiquetas();
                    } else {
                        Log.e("RetrofitError", "Error al eliminar etiqueta: " + idEtiqueta);
                        status.setValue(StatusRequest.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("RetrofitError", "Error de red o excepción al eliminar etiqueta: " + idEtiqueta, t);
                    status.setValue(StatusRequest.ERROR_CONEXION);
                }
            });
        }
    }
}