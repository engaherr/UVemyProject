package com.example.uvemyproject.viewmodels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.PerfilServices;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioUsuarioViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> statusGetImagen = new MutableLiveData<>();
    private MutableLiveData<UsuarioDTO> usuario = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> imagenPerfil = new MutableLiveData<>();
    private final MutableLiveData<StatusRequest> statusActualizarDatos = new MutableLiveData<>();
    private final MutableLiveData<StatusRequest> statusSubirFoto = new MutableLiveData<>();


    public MutableLiveData<StatusRequest> getstatusGetImagen() {
        return statusGetImagen;
    }

    public MutableLiveData<UsuarioDTO> getUsuario() {
        return usuario;
    }
    public MutableLiveData<Bitmap> getImagenPerfil() {
        return imagenPerfil;
    }
    public MutableLiveData<StatusRequest> getstatusActualizarDatos() {
        return statusActualizarDatos;
    }

    public void setUsuario(MutableLiveData<UsuarioDTO> usuario) {
        this.usuario = usuario;
    }

    public void obtenerFotoPerfil() {
        PerfilServices service = ApiClient.getInstance().getPerfilServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.obtenerFotoPerfil(auth).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    imagenPerfil.postValue(bitmap);
                    statusGetImagen.postValue(StatusRequest.DONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                statusGetImagen.postValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void actualizarDatosUsuario(UsuarioDTO usuarioDTO){
        PerfilServices service = ApiClient.getInstance().getPerfilServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        RequestBody requestBody;
        usuarioDTO.setIdUsuario(SingletonUsuario.getIdUsuario());
        usuarioDTO.setCorreoElectronico(SingletonUsuario.getCorreoElectronico());

        if (usuarioDTO.getContrasena() != null) {
            requestBody = convertirRequestBody(usuarioDTO, "contrasena");
        } else {
            requestBody = convertirRequestBody(usuarioDTO, null);
        }

        service.actualizarUsuario(auth, requestBody).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    statusActualizarDatos.postValue(StatusRequest.DONE);
                } else {
                    statusActualizarDatos.postValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                statusActualizarDatos.postValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private RequestBody convertirRequestBody(UsuarioDTO usuario, String campoEliminar) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UsuarioDTO> jsonAdapter = moshi.adapter(UsuarioDTO.class);

        String jsonEnviado = "";

        try {
            String json = jsonAdapter.toJson(usuario);
            JSONObject jsonObject = new JSONObject(json);
            if (campoEliminar != null) {
                jsonObject.remove(campoEliminar);
            }
            jsonObject.remove("jwt");
            jsonObject.remove("idsEtiqueta");

            jsonEnviado = jsonObject.toString();
        } catch (JSONException e) {
            Log.e("ViewModel", "Error al convertir el objeto a JSON", e);
        }

        return RequestBody.create(jsonEnviado,
                MediaType.parse("application/json; charset=utf-8"));
    }
}
