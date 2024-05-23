package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.dto.EstadisticasCursoDTO;

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
        String auth = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3ByaW1hcnlzaWQiOjEsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6Im1lbHVzQGdtYWlsLmNvbSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2dpdmVubmFtZSI6InMiLCJpc3MiOiJVVmVteVNlcnZpZG9ySldUIiwiYXVkIjoiVXN1YXJpb3NVVmVteUpXVCIsImlhdCI6MTcxNjQzOTczNiwiZXhwIjoxNzE2NDgyOTM2fQ.GRe4vPPwChq8qrXxaXDYiZKNl6kWcs9CnvJGZtZ4iew";
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

