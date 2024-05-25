package com.example.uvemyproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.EstadisticaServices;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaseDetallesViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<ClaseDTO> claseActual = new MutableLiveData<>();

    public LiveData<StatusRequest> getStatus(){
        return status;
    }
    public LiveData<ClaseDTO> getClaseActual() { return claseActual; }

    public void recuperarDetallesClase(int idClase){
        ClaseServices service = ApiClient.getInstance().getClaseServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.obtenerClase(auth, idClase).enqueue(new Callback<ClaseDTO>() {
            @Override
            public void onResponse(Call<ClaseDTO> call, Response<ClaseDTO> response) {
                if(response.isSuccessful()){
                    status.setValue(StatusRequest.DONE);
                    claseActual.setValue(response.body());

                }else{
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ClaseDTO> call, Throwable t) {
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });

    }


}
