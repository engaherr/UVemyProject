package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.CursoServices;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.UsuarioCursoDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoDetallesPrincipalViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<CursoDTO> cursoActual = new MutableLiveData<>();

    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public void setCursoActual(CursoDTO cursoActual){
        this.cursoActual.setValue(cursoActual);
    }

    public MutableLiveData<CursoDTO> getCursoActual() {
        return cursoActual;
    }

    //public CrearCursoDTO
    public void obtenerCurso(int idCurso){
        //Recuperar de API
        CursoDTO curso = new CursoDTO();
        curso.setIdCurso(idCurso);
        curso.setTitulo("Curso real");
        //curso.set("Profesor");
        ArrayList<ClaseDTO> clases = new ArrayList<ClaseDTO>();
        clases.add(new ClaseDTO(1, "Es una clase donde ponemos ciertas cosas", "Figuras como hacer figuras"));
        clases.add(new ClaseDTO(2, "Es una clase", "Fromas para mejorar objetcos"));
        //curso.setClases(clases);
        cursoActual.setValue(curso);
    }

    public void inscribirseCurso(){
        CursoServices services = ApiClient.getInstance().getCursoServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        UsuarioCursoDTO usuario = new UsuarioCursoDTO();
        usuario.setIdCurso(cursoActual.getValue().getIdCurso());
        services.inscribirseCurso(auth, cursoActual.getValue().getIdCurso(), convertirRequestBodyInscribirse(usuario)).enqueue(new Callback<UsuarioCursoDTO>() {
            @Override
            public void onResponse(Call<UsuarioCursoDTO> call, Response<UsuarioCursoDTO> response) {
                if(!response.isSuccessful()){
                    status.setValue(StatusRequest.ERROR);
                }else{
                    CursoDTO curso = cursoActual.getValue();
                    curso.setRol("Estudiante");
                    cursoActual.setValue(curso);
                }
            }
            @Override
            public void onFailure(Call<UsuarioCursoDTO> call, Throwable t) {
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });

    }
    private RequestBody convertirRequestBodyInscribirse(UsuarioCursoDTO usuariocurso){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UsuarioCursoDTO> jsonAdapter = moshi.adapter(UsuarioCursoDTO.class);
        String jsonEnviado = "";
        try {
            String json = jsonAdapter.toJson(usuariocurso);
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.remove("calificacion");
            jsonObject.remove("idUsuario");
            jsonEnviado = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonEnviado);
    }
}
