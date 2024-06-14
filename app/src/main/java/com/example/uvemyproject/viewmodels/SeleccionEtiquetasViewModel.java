package com.example.uvemyproject.viewmodels;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.EtiquetaServices;
import com.example.uvemyproject.api.services.PerfilServices;
import com.example.uvemyproject.dto.CredencialesDTO;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.dto.JwtDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeleccionEtiquetasViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<List<EtiquetaDTO>> etiquetas = new MutableLiveData<>();
    private final MutableLiveData<StatusRequest> statusCodigoVerificacion = new MutableLiveData<>();
    private final MutableLiveData<String> jwt = new MutableLiveData<>();

    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public MutableLiveData<List<EtiquetaDTO>> getEtiquetas() {
        return etiquetas;
    }
    public MutableLiveData<StatusRequest> getStatusCodigoVerificacion() {
        return statusCodigoVerificacion;
    }

    public MutableLiveData<String> getJwt() { return jwt; }

    public void obtenerEtiquetas() {
        EtiquetaServices service = ApiClient.getInstance().getEtiquetaServices();

        service.getEtiquetas().enqueue(new Callback<List<EtiquetaDTO>>() {
            @Override
            public void onResponse(Call<List<EtiquetaDTO>> call, Response<List<EtiquetaDTO>> response) {
                if(response.isSuccessful()) {
                    Log.d("ViewModel", "Etiquetas recibidas: " +
                            response.body().toString());
                    etiquetas.postValue(response.body());
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

    public void solicitarCodigoVerificacion(CredencialesDTO correoElectronico){
        PerfilServices service = ApiClient.getInstance().getPerfilServices();

        service.solicitarCodigoVerificacion(convertirRequestBody(correoElectronico))
                .enqueue(new Callback<JwtDTO>() {
            @Override
            public void onResponse(Call<JwtDTO> call, Response<JwtDTO> response) {
                if(response.isSuccessful()){
                    jwt.postValue(response.body().getJwt());
                    statusCodigoVerificacion.setValue(StatusRequest.DONE);
                } else {
                    statusCodigoVerificacion.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<JwtDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción" + t.getMessage(), t);
                statusCodigoVerificacion.setValue(StatusRequest.ERROR_CONEXION);
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
            jsonObject.remove("contrasena");

            jsonEnviado = jsonObject.toString();
        }catch(Exception e){
            e.printStackTrace();
        }

        return RequestBody.create(jsonEnviado,
                MediaType.parse("application/json"));
    }
}