package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.CursoServices;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.UsuarioCursoDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoDetallesPrincipalViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<CursoDTO> cursoActual = new MutableLiveData<>();
    private MutableLiveData<List<ClaseDTO>> clases = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public MutableLiveData<List<ClaseDTO>> getClases() {
        return clases;
    }
    public void setCursoActual(CursoDTO cursoActual){
        this.cursoActual.setValue(cursoActual);
    }

    public void setClases(List<ClaseDTO> nuevasClases) {
        clases.setValue(nuevasClases);
    }

    public MutableLiveData<CursoDTO> getCursoActual() {
        return cursoActual;
    }

    public void obtenerCurso(CursoDTO cursoN){
        CursoServices services = ApiClient.getInstance().getCursoServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        services.recuperarCurso(auth, cursoN.getIdCurso()).enqueue(new Callback<CursoDTO>() {

            @Override
            public void onResponse(Call<CursoDTO> call, Response<CursoDTO> response) {
                if (response.isSuccessful()) {
                    CursoDTO curso = new CursoDTO();
                    curso.setIdCurso(cursoN.getIdCurso());
                    curso.setTitulo(cursoN.getTitulo());
                    curso.setArchivo(cursoN.getArchivo());
                    curso.setDescripcion(response.body().getDescripcion());
                    curso.setObjetivos(response.body().getObjetivos());

                    curso.setRequisitos(response.body().getRequisitos());
                    curso.setIdUsuario(response.body().getIdUsuario());
                    curso.setEtiquetas(response.body().getEtiquetas());
                    curso.setCalificacion(response.body().getCalificacion());
                    curso.setRol(response.body().getRol());
                    curso.setProfesor(response.body().getProfesor());
                    setCursoActual(curso);
                    cursoActual.setValue(curso);
                    status.setValue(StatusRequest.DONE);
                    Log.d("Log", "Respuesta exitosa: " +response.body().getRol());
                }else{
                    Log.d("Log", "responde: "+response.message()+" Codigo: "+response.code()+" Cuerpo: "+response.body());
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<CursoDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void recuperarClases(int idCurso) {
        ClaseServices services = ApiClient.getInstance().getClaseServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        services.recuperarClasesCurso(auth, idCurso).enqueue(new Callback<List<ClaseDTO>>() {

            @Override
            public void onResponse(Call<List<ClaseDTO>> call, Response<List<ClaseDTO>> response) {
                if (response.isSuccessful()) {
                    List<ClaseDTO> clases = response.body();
                    if (clases != null && !clases.isEmpty()) {
                        for (ClaseDTO clase : clases) {
                            Log.d("Log", "ID Clase: " + clase.getIdClase() + ", Nombre: " + clase.getNombre());
                        }
                    } else {
                        Log.d("Log", "No se encontraron clases");
                        clases = new ArrayList<>();
                    }
                    setClases(clases);
                } else {
                    Log.d("Log", "Error en la solicitud: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ClaseDTO>> call, Throwable t) {
                Log.e("Log", "Error en la solicitud: " + t.getMessage(), t);
            }
        });
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
