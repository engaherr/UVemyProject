package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.AutenticacionServices;
import com.example.uvemyproject.dto.CredencialesDTO;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioSesionViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<UsuarioDTO> usuarioActual = new MutableLiveData<>();

    public LiveData<StatusRequest> getStatus() { return status; }
    public MutableLiveData<UsuarioDTO> getUsuarioActual() { return usuarioActual; }

    public void iniciarSesion(String correoElectronico, String contrasena) {
        AutenticacionServices service = ApiClient.getInstance().getAutenticacionServices();

        CredencialesDTO credenciales = new CredencialesDTO(correoElectronico, contrasena);

        service.iniciarSesion(convertirRequestBody(credenciales)).enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if(response.isSuccessful()) {
                    Log.d("ViewModel", "Usuario recibido: " + response.body().toString());
                    usuarioActual.postValue(response.body());
                    status.setValue(StatusRequest.DONE);
                } else {
                    status.setValue(StatusRequest.ERROR); //Credenciales incorrectas
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private RequestBody convertirRequestBody(CredencialesDTO credenciales) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<CredencialesDTO> jsonAdapter = moshi.adapter(CredencialesDTO.class);

        String jsonEnviado = "";
        try{
            String json = jsonAdapter.toJson(credenciales);
            JSONObject jsonObject = new JSONObject(json);

            jsonEnviado = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(jsonEnviado, MediaType.get("application/json; charset=utf-8"));
    }
}
