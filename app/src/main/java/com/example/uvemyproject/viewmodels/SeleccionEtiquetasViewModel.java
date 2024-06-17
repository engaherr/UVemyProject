package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.EtiquetaServices;
import com.example.uvemyproject.api.services.PerfilServices;
import com.example.uvemyproject.dto.CredencialesDTO;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.dto.JwtDTO;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
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
    private final MutableLiveData<StatusRequest> statusActualizarUsuarioEtiquetas = new MutableLiveData<>();
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
    public MutableLiveData<StatusRequest> getStatusActualizarUsuarioEtiquetas() {
        return statusActualizarUsuarioEtiquetas;
    }

    public MutableLiveData<String> getJwt() { return jwt; }

    public void obtenerEtiquetas() {
        EtiquetaServices service = ApiClient.getInstance().getEtiquetaServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.getEtiquetas(auth).enqueue(new Callback<List<EtiquetaDTO>>() {
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

    public void actualizarEtiquetasUsuario(int[] idEtiquetasSeleccionadas) {
        PerfilServices service = ApiClient.getInstance().getPerfilServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdsEtiqueta(idEtiquetasSeleccionadas);
        usuario.setIdUsuario(SingletonUsuario.getIdUsuario());

        service.actualizarEtiquetas(auth, convertirRequestBody(usuario)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    statusActualizarUsuarioEtiquetas.setValue(StatusRequest.DONE);
                } else {
                    statusActualizarUsuarioEtiquetas.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                statusActualizarUsuarioEtiquetas.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private RequestBody convertirRequestBody(UsuarioDTO etiquetasUsuario) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UsuarioDTO> jsonAdapter = moshi.adapter(UsuarioDTO.class);

        String jsonEnviado = "";
        try{
            String json = jsonAdapter.toJson(etiquetasUsuario);
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.remove("contrasena");
            jsonObject.remove("nombres");
            jsonObject.remove("apellidos");
            jsonObject.remove("correoElectronico");
            jsonObject.remove("jwt");

            jsonEnviado = jsonObject.toString();
        } catch (JSONException e) {
            Log.e("RetrofitError", "Error al convertir el objeto a JSON", e);
        }

        return RequestBody.create(jsonEnviado,
                MediaType.parse("application/json"));
    }
}