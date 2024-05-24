package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.dto.EstadisticasCursoDTO;
import com.example.uvemyproject.utils.SingletonUsuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadisticasCursoViewModel extends ViewModel {

    private final MutableLiveData<EstadisticasCursoDTO> estadisticasCurso = new MutableLiveData<>();
    public LiveData<EstadisticasCursoDTO> getEstadisticas(){
        return estadisticasCurso;
    }
    public void recuperarEstadisticasCurso(){
        ApiClient.Services service = ApiClient.getInstance().getService();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        service.obtenerEstadisticasCurso(1, auth).enqueue(new Callback<EstadisticasCursoDTO>() {
            @Override
            public void onResponse(Call<EstadisticasCursoDTO> call, Response<EstadisticasCursoDTO> response) {
                if (response.isSuccessful()) {
                    estadisticasCurso.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<EstadisticasCursoDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
            }
        });
    }

}

