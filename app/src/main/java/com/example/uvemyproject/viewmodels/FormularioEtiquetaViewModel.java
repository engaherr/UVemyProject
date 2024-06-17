package com.example.uvemyproject.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.EtiquetaServices;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioEtiquetaViewModel extends ViewModel {

    private final EtiquetaServices etiquetaServices = ApiClient.getInstance().getEtiquetaServices();
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();

    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public void registrarEtiqueta(String nombreEtiqueta) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"nombre\": \"" + nombreEtiqueta + "\"}");
        String auth = "Bearer " + SingletonUsuario.getJwt();

        etiquetaServices.registrarEtiqueta(requestBody, auth).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("FormularioEtiqueta", "Etiqueta registrada correctamente: " + nombreEtiqueta);
                    status.setValue(StatusRequest.DONE);
                } else {
                    Log.e("FormularioEtiqueta", "Error al registrar etiqueta: " + response.code());
                    if (response.code() == 400) {
                        status.setValue(StatusRequest.BAD_REQUEST);
                    } else {
                        status.setValue(StatusRequest.ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FormularioEtiqueta", "Error al registrar etiqueta", t);
                status.setValue(StatusRequest.ERROR);
            }
        });
    }
}