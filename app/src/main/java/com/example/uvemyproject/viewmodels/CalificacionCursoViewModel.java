package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.CursoServices;
import com.example.uvemyproject.dto.UsuarioCursoDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalificacionCursoViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<UsuarioCursoDTO> usuarioCalificacion = new MutableLiveData<>();

    public MutableLiveData<StatusRequest> getStatus(){
        return status;
    }
    public MutableLiveData<UsuarioCursoDTO> getUsuarioCalificacion() {
        return usuarioCalificacion;
    }
    public void obtenerUsuarioCalificacion(int idCurso){
        CursoServices services = ApiClient.getInstance().getCursoServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        services.obtenerCalificacion(auth, idCurso).enqueue(new Callback<UsuarioCursoDTO>() {
            @Override
            public void onResponse(Call<UsuarioCursoDTO> call, Response<UsuarioCursoDTO> response) {
                if(response.isSuccessful()){
                    usuarioCalificacion.setValue(response.body());
                }else{
                    status.setValue(StatusRequest.ERROR);
                }
            }
            @Override
            public void onFailure(Call<UsuarioCursoDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void guardarCalificacionCurso(){
        if(usuarioCalificacion.getValue().getCalificacion() == 0){
            UsuarioCursoDTO usuario = usuarioCalificacion.getValue();
            usuario.setCalificacion(10);
            usuarioCalificacion.setValue(usuario);
        }
        CursoServices services = ApiClient.getInstance().getCursoServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        services.calificarCurso(auth, usuarioCalificacion.getValue().getIdCurso(),
                convertirRequestBodyCalificar(usuarioCalificacion.getValue())).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    status.setValue(StatusRequest.DONE);
                }else{
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private RequestBody convertirRequestBodyCalificar(UsuarioCursoDTO usuariocurso){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UsuarioCursoDTO> jsonAdapter = moshi.adapter(UsuarioCursoDTO.class);
        String jsonEnviado = "";
        try {
            String json = jsonAdapter.toJson(usuariocurso);
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.remove("idUsuario");
            jsonEnviado = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonEnviado);
    }

    public void disminuirCalificacion(){
        UsuarioCursoDTO usuario = usuarioCalificacion.getValue();

        if(usuarioCalificacion.getValue().getCalificacion() == 0){
            usuario.setCalificacion(10);
        }
        if(usuarioCalificacion.getValue().getCalificacion() > 1){
            usuario.setCalificacion(usuario.getCalificacion() - 1);
        }
        usuarioCalificacion.setValue(usuario);

    }
    public void aumentarCalificacion(){
        UsuarioCursoDTO usuario = usuarioCalificacion.getValue();

        if(usuarioCalificacion.getValue().getCalificacion() == 0){
            usuario.setCalificacion(10);
        }
        if(usuarioCalificacion.getValue().getCalificacion() < 10){
            usuario.setCalificacion(usuario.getCalificacion() + 1);
        }
        usuarioCalificacion.setValue(usuario);

    }
}
