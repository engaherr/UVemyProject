package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.PerfilServices;
import com.example.uvemyproject.dto.UsuarioRegistroDTO;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmacionCorreoViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();

    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public MutableLiveData<String> getMensajeError() {
        return mensajeError;
    }

    public void registrarUsuario(UsuarioRegistroDTO usuarioRegistro) {
        PerfilServices service = ApiClient.getInstance().getPerfilServices();
        String auth = "Bearer " + usuarioRegistro.getJwt();

        service.registrarUsuario(auth, convertirRequestBody(usuarioRegistro))
                .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    status.setValue(StatusRequest.DONE);
                } else {
                    status.setValue(StatusRequest.ERROR);
                    try {
                        String error = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(error);
                        String detalles = jsonObject.optString("detalles", "Error desconocido");
                        mensajeError.postValue(detalles);
                    } catch (IOException | JSONException e) {
                        mensajeError.postValue("Error desconocido");
                        Log.e("ViewModel", "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private RequestBody convertirRequestBody(UsuarioRegistroDTO usuarioRegistroDTO) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UsuarioRegistroDTO> jsonAdapter = moshi.adapter(UsuarioRegistroDTO.class);

        String jsonEnviado = "";

        try {
            String json = jsonAdapter.toJson(usuarioRegistroDTO);
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.remove("jwt");
            jsonObject.remove("idUsuario");
            jsonEnviado = jsonObject.toString();
        } catch (JSONException e) {
            Log.e("ViewModel", "convertirRequestBody: ", e);
        }

        return RequestBody.create(jsonEnviado,
                MediaType.parse("application/json; charset=utf-8"));
    }
}
